package com.paydaytrade.resource;

import lombok.Data;

@Data
public class StockClientResponseResource {
    private String symbol;
    private Double close;
}
