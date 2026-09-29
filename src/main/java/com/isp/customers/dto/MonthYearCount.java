package com.isp.customers.dto;

/**
 * Number of customers born in a given year/month.
 */
public record MonthYearCount(Integer year, Integer month, Long count) {}
