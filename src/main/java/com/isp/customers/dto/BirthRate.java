package com.isp.customers.dto;

/**
 * Birth rate for a calendar month. The rate is the percentage of customers
 * born in that month over the total number of customers.
 */
public record BirthRate(Integer month, String monthName, Long count, Double rate) {}
