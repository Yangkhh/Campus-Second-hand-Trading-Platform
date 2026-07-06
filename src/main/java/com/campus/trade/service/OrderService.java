package com.campus.trade.service;

import com.campus.trade.common.BusinessException;
import com.campus.trade.common.ErrorCode;
import com.campus.trade.dto.OrderDtos.CreateOrderRequest;
import com.campus.trade.dto.OrderDtos.OrderResponse;
import com.campus.trade.entity.OrderStatus;
import com.campus.trade.entity.Product;
import com.campus.trade.entity.ProductStatus;
import com.campus.trade.entity.TradeOrder;
import com.campus.trade.entity.User;
import com.campus.trade.repository.OrderRepository;
import com.campus.trade.repository.ProductRepository;
import com.campus.trade.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SearchService searchService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        SearchService searchService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.searchService = searchService;
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public OrderResponse create(Long buyerId, CreateOrderRequest request) {
        Product product = productRepository.findByIdForUpdate(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商品不存在"));
        if (product.getStatus() != ProductStatus.ON_SALE) {
            throw new BusinessException(ErrorCode.CONFLICT, "商品当前不可下单");
        }
        if (product.getSeller().getId().equals(buyerId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "不能购买自己发布的商品");
        }
        boolean locked = orderRepository.existsByProductIdAndStatusIn(product.getId(),
                Arrays.asList(OrderStatus.PENDING, OrderStatus.CONFIRMED));
        if (locked) {
            throw new BusinessException(ErrorCode.CONFLICT, "商品已有进行中的订单");
        }

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        TradeOrder order = new TradeOrder();
        order.setOrderNo(generateOrderNo());
        order.setProduct(product);
        order.setBuyer(buyer);
        order.setSeller(product.getSeller());
        order.setPrice(product.getPrice());
        order.setMeetLocation(request.getMeetLocation());
        order.setRemark(request.getRemark());
        order.setStatus(OrderStatus.PENDING);

        product.setStatus(ProductStatus.LOCKED);
        productRepository.save(product);
        TradeOrder saved = orderRepository.save(order);
        searchService.removeProduct(product.getId());
        return OrderResponse.from(saved);
    }

    @Transactional
    public OrderResponse confirm(Long orderId, Long operatorId) {
        TradeOrder order = loadOrder(orderId);
        ensureSeller(order, operatorId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.CONFLICT, "只有待确认订单可以确认");
        }
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        return OrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public OrderResponse complete(Long orderId, Long operatorId) {
        TradeOrder order = loadOrder(orderId);
        ensureParticipant(order, operatorId);
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException(ErrorCode.CONFLICT, "只有已确认订单可以完成交易");
        }
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());
        order.getProduct().setStatus(ProductStatus.SOLD);
        TradeOrder saved = orderRepository.save(order);
        productRepository.save(order.getProduct());
        searchService.removeProduct(order.getProduct().getId());
        return OrderResponse.from(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public OrderResponse cancel(Long orderId, Long operatorId) {
        TradeOrder order = loadOrder(orderId);
        ensureParticipant(order, operatorId);
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.CONFLICT, "当前订单状态不能取消");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.getProduct().setStatus(ProductStatus.ON_SALE);
        TradeOrder saved = orderRepository.save(order);
        productRepository.save(order.getProduct());
        searchService.indexProduct(order.getProduct());
        return OrderResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> mine(Long userId, Pageable pageable) {
        return orderRepository.findByBuyerIdOrSellerId(userId, userId, pageable).map(OrderResponse::from);
    }

    @Transactional(readOnly = true)
    public OrderResponse detail(Long orderId, Long userId) {
        TradeOrder order = loadOrder(orderId);
        ensureParticipant(order, userId);
        return OrderResponse.from(order);
    }

    private TradeOrder loadOrder(Long orderId) {
        return orderRepository.findWithRelationsById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "订单不存在"));
    }

    private void ensureSeller(TradeOrder order, Long operatorId) {
        if (!order.getSeller().getId().equals(operatorId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只有卖家可以执行该操作");
        }
    }

    private void ensureParticipant(TradeOrder order, Long operatorId) {
        boolean buyer = order.getBuyer().getId().equals(operatorId);
        boolean seller = order.getSeller().getId().equals(operatorId);
        if (!buyer && !seller) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能查看或操作自己的订单");
        }
    }

    private String generateOrderNo() {
        String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "T" + time + suffix;
    }
}
