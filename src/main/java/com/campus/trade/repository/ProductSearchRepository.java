package com.campus.trade.repository;

import com.campus.trade.entity.ProductDocument;
import com.campus.trade.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    Page<ProductDocument> findByStatusAndTitleContainingOrStatusAndDescriptionContaining(
            ProductStatus statusForTitle,
            String titleKeyword,
            ProductStatus statusForDescription,
            String descriptionKeyword,
            Pageable pageable);
}
