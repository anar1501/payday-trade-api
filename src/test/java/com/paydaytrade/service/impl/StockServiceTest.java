package com.paydaytrade.service.impl;

import com.paydaytrade.data.entity.StockWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Test
    void invoke() throws IOException {
        final StockWrapper stock = stockService.findStock("UU.L");
        System.out.println(stock.getStock());

        final BigDecimal price = stockService.findPrice(stock);
        System.out.println(price);

        final BigDecimal change = stockService.findLastChangePercent(stock);
        System.out.println(change);

        final BigDecimal mean200DayPercent = stockService.findChangeFrom200MeanPercent(stock);
        System.out.println(mean200DayPercent);
    }

    @Test
    void multiple() throws IOException, InterruptedException{
        final List<StockWrapper> stocks = stockService.findStocks(Arrays.asList("GOOG","AMZN"));
        findPrices(stocks);

        Thread.sleep(16000);

        final StockWrapper aa = stockService.findStock("ABF.L");
        stocks.add(aa);

        System.out.println(stockService.findPrice(aa));

        findPrices(stocks);
    }

    private void findPrices(List<StockWrapper>stocks){
        stocks.forEach(stock->{
            try {
                System.out.println(stockService.findPrice(stock));
            } catch (IOException e) {
                //ignore
            }
        });
    }

}