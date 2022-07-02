package com.paydaytrade.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class URLConfiguration {
    @Value("${payday-trade.stock-aapl-url}")
    private String aaplStockUrl;
    @Value("${payday-trade.stock-tsla-url}")
    private String tslaStockUrl;
}