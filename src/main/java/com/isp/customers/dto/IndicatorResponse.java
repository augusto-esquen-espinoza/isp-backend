package com.isp.customers.dto;

import java.util.List;

/**
 * Aggregated birth indicators exposed by the indicator endpoint.
 *
 * @param birthsByMonthYear number of customers born per month/year
 * @param peakMonthYear month/year with the highest number of births
 * @param lowestMonthYear month/year with the lowest number of births
 * @param birthRateByMonth birth rate (percentage) for each calendar month
 * @param totalCustomers total number of registered customers
 */
public record IndicatorResponse(
        List<MonthYearCount> birthsByMonthYear,
        MonthYearCount peakMonthYear,
        MonthYearCount lowestMonthYear,
        List<BirthRate> birthRateByMonth,
        long totalCustomers) {}
