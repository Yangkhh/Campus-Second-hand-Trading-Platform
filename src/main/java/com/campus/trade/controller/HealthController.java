package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> status = new HashMap<String, String>();
        status.put("status", "UP");
        status.put("service", "campus-second-hand-trading-platform");
        return ApiResponse.ok(status);
    }
}
