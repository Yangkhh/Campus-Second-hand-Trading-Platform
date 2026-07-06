package com.campus.trade.repository;

import com.campus.trade.entity.Product;
import com.campus.trade.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "seller")
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "seller")
    Page<Product> findBySellerId(Long sellerId, Pageable pageable);

    @EntityGraph(attributePaths = "seller")
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findWithSellerById(@Param("id") Long id);

    @EntityGraph(attributePaths = "seller")
    List<Product> findTop20ByStatusOrderByFavoriteCountDescViewCountDescCreatedAtDesc(ProductStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);

    @EntityGraph(attributePaths = "seller")
    @Query("select p from Product p where p.status = :status and (:category is null or p.category = :category)")
    Page<Product> findVisibleProducts(@Param("status") ProductStatus status,
                                      @Param("category") String category,
                                      Pageable pageable);

    @EntityGraph(attributePaths = "seller")
    @Query("select p from Product p where p.status = :status "
            + "and (:category is null or p.category = :category) "
            + "and (lower(p.title) like lower(concat('%', :keyword, '%')) "
            + "or lower(p.description) like lower(concat('%', :keyword, '%')))")
    Page<Product> searchFallback(@Param("status") ProductStatus status,
                                 @Param("keyword") String keyword,
                                 @Param("category") String category,
                                 Pageable pageable);
}
