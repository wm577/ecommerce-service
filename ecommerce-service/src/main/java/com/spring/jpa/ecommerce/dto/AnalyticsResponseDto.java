package com.spring.jpa.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponseDto {

    @JsonProperty("currentMonthOrders")
    private Long currentMonthOrders;

    @JsonProperty("currentMonthEarnings")
    private Long currentMonthEarnings;

    @JsonProperty("previousMonthOrders")
    private Long previousMonthOrders;

    @JsonProperty("previousMonthEarnings")
    private Long previousMonthEarnings;

    @JsonProperty("orderGrowth")
    private Double orderGrowth;

    @JsonProperty("earningGrowth")
    private Double earningGrowth;

    @JsonProperty("averageOrderValue")
    private Double averageOrderValue;

    @JsonProperty("placed")
    private Long placed;

    @JsonProperty("shipped")
    private Long shipped;

    @JsonProperty("delivered")
    private Long delivered;
}