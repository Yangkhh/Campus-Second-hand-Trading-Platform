package com.campus.trade.dto;

import com.campus.trade.entity.OrderStatus;
import com.campus.trade.entity.TradeOrder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class OrderDtos {

    private OrderDtos() {
    }

    public static class CreateOrderRequest {
        @NotNull
        private Long productId;

        @Size(max = 120)
        private String meetLocation;

        @Size(max = 500)
        private String remark;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getMeetLocation() {
            return meetLocation;
        }

        public void setMeetLocation(String meetLocation) {
            this.meetLocation = meetLocation;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class OrderResponse {
        private Long id;
        private String orderNo;
        private Long productId;
        private String productTitle;
        private Long buyerId;
        private String buyerName;
        private Long sellerId;
        private String sellerName;
        private BigDecimal price;
        private OrderStatus status;
        private String meetLocation;
        private String remark;
        private LocalDateTime createdAt;
        private LocalDateTime confirmedAt;
        private LocalDateTime completedAt;
        private LocalDateTime cancelledAt;

        public static OrderResponse from(TradeOrder order) {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setOrderNo(order.getOrderNo());
            response.setProductId(order.getProduct().getId());
            response.setProductTitle(order.getProduct().getTitle());
            response.setBuyerId(order.getBuyer().getId());
            response.setBuyerName(order.getBuyer().getNickname());
            response.setSellerId(order.getSeller().getId());
            response.setSellerName(order.getSeller().getNickname());
            response.setPrice(order.getPrice());
            response.setStatus(order.getStatus());
            response.setMeetLocation(order.getMeetLocation());
            response.setRemark(order.getRemark());
            response.setCreatedAt(order.getCreatedAt());
            response.setConfirmedAt(order.getConfirmedAt());
            response.setCompletedAt(order.getCompletedAt());
            response.setCancelledAt(order.getCancelledAt());
            return response;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductTitle() {
            return productTitle;
        }

        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }

        public Long getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(Long buyerId) {
            this.buyerId = buyerId;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public Long getSellerId() {
            return sellerId;
        }

        public void setSellerId(Long sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }

        public String getMeetLocation() {
            return meetLocation;
        }

        public void setMeetLocation(String meetLocation) {
            this.meetLocation = meetLocation;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getConfirmedAt() {
            return confirmedAt;
        }

        public void setConfirmedAt(LocalDateTime confirmedAt) {
            this.confirmedAt = confirmedAt;
        }

        public LocalDateTime getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
        }

        public LocalDateTime getCancelledAt() {
            return cancelledAt;
        }

        public void setCancelledAt(LocalDateTime cancelledAt) {
            this.cancelledAt = cancelledAt;
        }
    }
}
