package com.paydaytrade.service;

import com.paydaytrade.data.dto.request.BalanceRequestDto;
import com.paydaytrade.data.dto.response.StockResponseDto;

import java.util.List;

public interface PaydayTradeService {
    List<StockResponseDto> showAllStockWithTheLastPrice();
    void loadBalance(BalanceRequestDto requestDto,Long id);
    void buyStock(BalanceRequestDto requestDto, Long id, Long accountId);
    void selStock(BalanceRequestDto requestDto, Long id, Long accountId);
}
