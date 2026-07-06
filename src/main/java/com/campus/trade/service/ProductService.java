package com.campus.trade.service;

import com.campus.trade.common.BusinessException;
import com.campus.trade.common.ErrorCode;
import com.campus.trade.dto.ProductDtos.ProductRequest;
import com.campus.trade.dto.ProductDtos.ProductResponse;
import com.campus.trade.entity.Product;
import com.campus.trade.entity.ProductStatus;
import com.campus.trade.entity.User;
import com.campus.trade.repository.ProductRepository;
import com.campus.trade.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SearchService searchService;

    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          SearchService searchService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.searchService = searchService;
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public ProductResponse create(Long sellerId, ProductRequest request) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
        Product product = new Product();
        product.setSeller(seller);
        fillProduct(product, request);
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public ProductResponse update(Long productId, Long userId, ProductRequest request) {
        Product product = productRepository.findWithSellerById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商品不存在"));
        ensureOwner(product, userId);
        if (product.getStatus() == ProductStatus.SOLD || product.getStatus() == ProductStatus.LOCKED) {
            throw new BusinessException(ErrorCode.CONFLICT, "当前状态不允许修改商品");
        }
        fillProduct(product, request);
        Product saved = productRepository.save(product);
        searchService.indexProduct(saved);
        return ProductResponse.from(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public ProductResponse publish(Long productId, Long userId) {
        Product product = productRepository.findWithSellerById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商品不存在"));
        ensureOwner(product, userId);
        if (product.getStatus() != ProductStatus.DRAFT && product.getStatus() != ProductStatus.OFF_SHELF) {
            throw new BusinessException(ErrorCode.CONFLICT, "只有草稿或已下架商品可以发布");
        }
        product.setStatus(ProductStatus.ON_SALE);
        Product saved = productRepository.save(product);
        searchService.indexProduct(saved);
        return ProductResponse.from(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "homeProducts", allEntries = true)
    public ProductResponse offShelf(Long productId, Long userId) {
        Product product = productRepository.findWithSellerById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商品不存在"));
        ensureOwner(product, userId);
        if (product.getStatus() != ProductStatus.ON_SALE && product.getStatus() != ProductStatus.DRAFT) {
            throw new BusinessException(ErrorCode.CONFLICT, "当前状态不允许下架");
        }
        product.setStatus(ProductStatus.OFF_SHELF);
        Product saved = productRepository.save(product);
        searchService.removeProduct(saved.getId());
        return ProductResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> listVisible(String category, Pageable pageable) {
        String normalizedCategory = normalize(category);
        return productRepository.findVisibleProducts(ProductStatus.ON_SALE, normalizedCategory, pageable)
                .map(ProductResponse::from);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "homeProducts", key = "'top20'")
    public List<ProductResponse> homeProducts() {
        return productRepository.findTop20ByStatusOrderByFavoriteCountDescViewCountDescCreatedAtDesc(ProductStatus.ON_SALE)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse detail(Long productId) {
        Product product = productRepository.findWithSellerById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "商品不存在"));
        product.setViewCount(product.getViewCount() + 1);
        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> mine(Long sellerId, Pageable pageable) {
        return productRepository.findBySellerId(sellerId, pageable).map(ProductResponse::from);
    }

    private void fillProduct(Product product, ProductRequest request) {
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setConditionText(request.getConditionText());
        product.setImageUrls(request.getImageUrls());
        product.setTradeLocation(request.getTradeLocation());
    }

    private void ensureOwner(Product product, Long userId) {
        if (!product.getSeller().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能操作自己发布的商品");
        }
    }

    private String normalize(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}
