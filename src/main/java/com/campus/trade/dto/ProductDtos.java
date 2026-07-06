package com.campus.trade.dto;

import com.campus.trade.entity.Product;
import com.campus.trade.entity.ProductStatus;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ProductDtos {

    private ProductDtos() {
    }

    public static class ProductRequest {
        @NotBlank
        @Size(max = 120)
        private String title;

        @NotBlank
        @Size(max = 4000)
        private String description;

        @NotNull
        @DecimalMin("0.01")
        private BigDecimal price;

        @NotBlank
        @Size(max = 60)
        private String category;

        @Size(max = 60)
        private String conditionText;

        @Size(max = 1200)
        private String imageUrls;

        @Size(max = 120)
        private String tradeLocation;

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

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
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

        public String getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(String imageUrls) {
            this.imageUrls = imageUrls;
        }

        public String getTradeLocation() {
            return tradeLocation;
        }

        public void setTradeLocation(String tradeLocation) {
            this.tradeLocation = tradeLocation;
        }
    }

    public static class ProductResponse {
        private Long id;
        private Long sellerId;
        private String sellerName;
        private String title;
        private String description;
        private BigDecimal price;
        private String category;
        private String conditionText;
        private String imageUrls;
        private String tradeLocation;
        private ProductStatus status;
        private Integer viewCount;
        private Integer favoriteCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ProductResponse from(Product product) {
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setSellerId(product.getSeller().getId());
            response.setSellerName(product.getSeller().getNickname());
            response.setTitle(product.getTitle());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setCategory(product.getCategory());
            response.setConditionText(product.getConditionText());
            response.setImageUrls(product.getImageUrls());
            response.setTradeLocation(product.getTradeLocation());
            response.setStatus(product.getStatus());
            response.setViewCount(product.getViewCount());
            response.setFavoriteCount(product.getFavoriteCount());
            response.setCreatedAt(product.getCreatedAt());
            response.setUpdatedAt(product.getUpdatedAt());
            return response;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
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

        public String getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(String imageUrls) {
            this.imageUrls = imageUrls;
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

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Integer getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(Integer favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
