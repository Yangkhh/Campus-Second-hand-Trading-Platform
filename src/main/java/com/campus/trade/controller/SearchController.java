package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import com.campus.trade.dto.SearchDtos.SearchProductResponse;
import com.campus.trade.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/products")
    public ApiResponse<Page<SearchProductResponse>> products(@RequestParam(defaultValue = "") String keyword,
                                                             @RequestParam(required = false) String category,
                                                             @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(searchService.search(keyword, category, pageable));
    }
}
