package com.isp.customers.controller;

import com.isp.customers.dto.IndicatorResponse;
import com.isp.customers.service.IndicatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indicators")
@Tag(name = "Indicators", description = "Customer birth indicators")
public class IndicatorController {

    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping
    @Operation(summary = "Get all customer birth indicators")
    public IndicatorResponse getIndicators() {
        return indicatorService.getIndicators();
    }
}
