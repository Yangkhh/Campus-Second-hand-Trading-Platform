package com.campus.trade.dto;

import com.campus.trade.entity.ProductDocument;
import com.campus.trade.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class SearchDtos {

    private SearchDtos() {
    }

    public static class SearchProductResponse {
        private Long id;
        private String title;
        private String description;
        private String category;
        private String conditionText;
        private String tradeLocation;
        private ProductStatus status;
        private BigDecimal price;
        private Long sellerId;
        private String sellerName;
        private LocalDateTime createdAt;

        public static SearchProductResponse from(ProductDocument document) {
            SearchProductResponse response = new SearchProductResponse();
            response.setId(document.getId());
            response.setTitle(document.getTitle());
            response.setDescription(document.getDescription());
            response.setCategory(document.getCategory());
            response.setConditionText(document.getConditionText());
            response.setTradeLocation(document.getTradeLocation());
            response.setStatus(document.getStatus());
            response.setPrice(document.getPrice());
            response.setSellerId(document.getSellerId());
            response.setSellerName(document.getSellerName());
            response.setCreatedAt(document.getCreatedAt());
            return response;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getConditionText() {
            return conditionText;
        }

        public void setConditionText(String conditionText) {
            this.conditionText = conditionText;
        }

        public String getTradeLocation() {
            return tradeLocation;
        }

        public void setTradeLocation(String tradeLocation) {
            this.tradeLocation = tradeLocation;
        }

        public ProductStatus getStatus() {
            return status;
        }

        public void setStatus(ProductStatus status) {
            this.status = status;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
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

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
