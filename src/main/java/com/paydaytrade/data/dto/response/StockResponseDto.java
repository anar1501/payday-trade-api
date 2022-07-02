package com.paydaytrade.data.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StockResponseDto implements Serializable {
    private String stockName;
    private Double latestPrice;
}
