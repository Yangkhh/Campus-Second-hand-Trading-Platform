package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import com.campus.trade.dto.ProductDtos.ProductRequest;
import com.campus.trade.dto.ProductDtos.ProductResponse;
import com.campus.trade.security.SecurityUtils;
import com.campus.trade.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> list(@RequestParam(required = false) String category,
                                                   @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResponse.ok(productService.listVisible(category, pageable));
    }

    @GetMapping("/home")
    public ApiResponse<List<ProductResponse>> homeProducts() {
        return ApiResponse.ok(productService.homeProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(productService.detail(id));
    }

    @GetMapping("/mine")
    public ApiResponse<Page<ProductResponse>> mine(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResponse.ok(productService.mine(SecurityUtils.currentUserId(), pageable));
    }

    @PostMapping
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok(productService.create(SecurityUtils.currentUserId(), request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok(productService.update(id, SecurityUtils.currentUserId(), request));
    }

    @PostMapping("/{id}/publish")
    public ApiResponse<ProductResponse> publish(@PathVariable Long id) {
        return ApiResponse.ok(productService.publish(id, SecurityUtils.currentUserId()));
    }

    @PostMapping("/{id}/off-shelf")
    public ApiResponse<ProductResponse> offShelf(@PathVariable Long id) {
        return ApiResponse.ok(productService.offShelf(id, SecurityUtils.currentUserId()));
    }
}
