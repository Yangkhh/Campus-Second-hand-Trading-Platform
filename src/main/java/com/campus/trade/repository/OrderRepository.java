package com.campus.trade.repository;

import com.campus.trade.entity.OrderStatus;
import com.campus.trade.entity.TradeOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<TradeOrder, Long> {

    @EntityGraph(attributePaths = {"product", "buyer", "seller"})
    @org.springframework.data.jpa.repository.Query("select o from TradeOrder o where o.id = :id")
    Optional<TradeOrder> findWithRelationsById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"product", "buyer", "seller"})
    Optional<TradeOrder> findByOrderNo(String orderNo);

    boolean existsByProductIdAndStatusIn(Long productId, Collection<OrderStatus> statuses);

    @EntityGraph(attributePaths = {"product", "buyer", "seller"})
    Page<TradeOrder> findByBuyerIdOrSellerId(Long buyerId, Long sellerId, Pageable pageable);
}
