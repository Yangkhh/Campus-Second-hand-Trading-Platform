package com.campus.trade.service;

import com.campus.trade.dto.ProductDtos.ProductResponse;
import com.campus.trade.dto.SearchDtos.SearchProductResponse;
import com.campus.trade.entity.Product;
import com.campus.trade.entity.ProductDocument;
import com.campus.trade.entity.ProductStatus;
import com.campus.trade.repository.ProductRepository;
import com.campus.trade.repository.ProductSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductRepository productRepository;

    public SearchService(ProductSearchRepository productSearchRepository,
                         ElasticsearchOperations elasticsearchOperations,
                         ProductRepository productRepository) {
        this.productSearchRepository = productSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.productRepository = productRepository;
    }

    public void indexProduct(Product product) {
        if (product.getStatus() != ProductStatus.ON_SALE) {
            removeProduct(product.getId());
            return;
        }
        try {
            productSearchRepository.save(ProductDocument.from(product));
        } catch (RuntimeException ex) {
            log.warn("同步商品到 Elasticsearch 失败，productId={}", product.getId(), ex);
        }
    }

    public void removeProduct(Long productId) {
        try {
            productSearchRepository.deleteById(productId);
        } catch (RuntimeException ex) {
            log.warn("从 Elasticsearch 移除商品失败，productId={}", productId, ex);
        }
    }

    @Transactional(readOnly = true)
    public Page<SearchProductResponse> search(String keyword, String category, Pageable pageable) {
        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedCategory = normalize(category);
        try {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.multiMatchQuery(normalizedKeyword, "title", "description"))
                    .filter(QueryBuilders.termQuery("status", ProductStatus.ON_SALE.name()));
            if (StringUtils.hasText(normalizedCategory)) {
                boolQuery.filter(QueryBuilders.termQuery("category", normalizedCategory));
            }

            NativeSearchQuery query = new NativeSearchQueryBuilder()
                    .withQuery(boolQuery)
                    .withPageable(pageable)
                    .build();
            SearchHits<ProductDocument> hits = elasticsearchOperations.search(query, ProductDocument.class);
            List<SearchProductResponse> content = hits.stream()
                    .map(SearchHit::getContent)
                    .map(SearchProductResponse::from)
                    .collect(Collectors.toList());
            return new PageImpl<SearchProductResponse>(content, pageable, hits.getTotalHits());
        } catch (RuntimeException ex) {
            log.warn("Elasticsearch 搜索失败，降级为数据库模糊查询，keyword={}", normalizedKeyword, ex);
            Page<Product> fallback = productRepository.searchFallback(
                    ProductStatus.ON_SALE, normalizedKeyword, normalizedCategory, pageable);
            return fallback.map(product -> {
                ProductResponse productResponse = ProductResponse.from(product);
                SearchProductResponse response = new SearchProductResponse();
                response.setId(productResponse.getId());
                response.setTitle(productResponse.getTitle());
                response.setDescription(productResponse.getDescription());
                response.setCategory(productResponse.getCategory());
                response.setConditionText(productResponse.getConditionText());
                response.setTradeLocation(productResponse.getTradeLocation());
                response.setStatus(productResponse.getStatus());
                response.setPrice(productResponse.getPrice());
                response.setSellerId(productResponse.getSellerId());
                response.setSellerName(productResponse.getSellerName());
                response.setCreatedAt(productResponse.getCreatedAt());
                return response;
            });
        }
    }

    private String normalizeKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? keyword.trim() : "";
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
