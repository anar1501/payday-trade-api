package com.paydaytrade.service.impl;

import com.paydaytrade.client.RestClient;
import com.paydaytrade.config.ApplicationConfig;
import com.paydaytrade.config.URLConfiguration;
import com.paydaytrade.data.dao.AccountDao;
import com.paydaytrade.data.dto.request.BalanceRequestDto;
import com.paydaytrade.data.dto.response.StockResponseDto;
import com.paydaytrade.data.entity.Account;
import com.paydaytrade.data.entity.User;
import com.paydaytrade.data.repository.AccountRepository;
import com.paydaytrade.data.repository.UserRepository;
import com.paydaytrade.resource.StockClientResponseResource;
import com.paydaytrade.service.PaydayTradeService;
import com.paydaytrade.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaydayTradeServiceImpl implements PaydayTradeService {

    private final RestClient restClient;
    private final URLConfiguration urlConfiguration;
    private final ApplicationConfig applicationConfig;
    private final AccountRepository accountRepository;
    private final MessageUtils messageUtils;
    private final UserRepository userRepository;
    private final AccountDao accountDao;
    @Value("${order-message.order-sold}")
    private String soldOrder;
    @Value("${order-message.order-bought}")
    private String boughtOrder;
    @Value("${order-message.order-subject}")
    private String orderSubject;

    @Transactional(readOnly = true)
    @Override
    public List<StockResponseDto> showAllStockWithTheLastPrice() {
        List<StockResponseDto> newStockResponseDtoList = new ArrayList<>();
        StockResponseDto responseDtoWithAAPL = new StockResponseDto();
        StockResponseDto responseDtoWithTSLA = new StockResponseDto();
        ResponseEntity<StockClientResponseResource> stockLikeAAPL = restClient.getForEntity(urlConfiguration.getAaplStockUrl() + applicationConfig.getApiKey(), StockClientResponseResource.class);
        ResponseEntity<StockClientResponseResource> stockLikeTSLA = restClient.getForEntity(urlConfiguration.getTslaStockUrl() + applicationConfig.getApiKey(), StockClientResponseResource.class);
        if (stockLikeAAPL != null && stockLikeAAPL.getBody() != null && stockLikeAAPL.getStatusCode().equals(HttpStatus.OK) &&
                stockLikeTSLA != null && stockLikeTSLA.getBody() != null && stockLikeTSLA.getStatusCode().equals(HttpStatus.OK)) {
            responseDtoWithAAPL.setStockName(stockLikeAAPL.getBody().getSymbol());
            responseDtoWithAAPL.setLatestPrice(stockLikeAAPL.getBody().getClose());
            responseDtoWithTSLA.setStockName(stockLikeTSLA.getBody().getSymbol());
            responseDtoWithTSLA.setLatestPrice(stockLikeTSLA.getBody().getClose());
            newStockResponseDtoList.add(responseDtoWithAAPL);
            newStockResponseDtoList.add(responseDtoWithTSLA);
        }
        return newStockResponseDtoList;
    }

    @Override
    public void loadBalance(BalanceRequestDto requestDto, Long id) {
        Account account = accountRepository.findAccountById(id);
        if (account.getBalance() == 0.0 && account == null) {
            account.setBalance(requestDto.getAmount());
            accountRepository.save(account);
        }
        account.setBalance(account.getBalance()+requestDto.getAmount());
        accountRepository.save(account);
    }

    @Override
    public void buyStock(BalanceRequestDto requestDto, Long id, Long accountId) {
        Optional<StockResponseDto> AAPL = showAllStockWithTheLastPrice().stream().filter(stock -> stock.getStockName().equals("AAPL") && stock.getLatestPrice().doubleValue() == requestDto.getAmount() || stock.getLatestPrice() >= requestDto.getAmount()).findFirst();
        User userById = userRepository.findUserById(id);
        Account account = accountRepository.findAccountById(accountId);
        if (AAPL.isPresent()) {
            account.setBalance(account.getBalance() - requestDto.getAmount());
            accountDao.update(account.getId(), account);
            messageUtils.sendAsync(userById.getEmail(), orderSubject, boughtOrder);
        }
    }

    @Override
    public void selStock(BalanceRequestDto requestDto, Long id, Long accountId) {
        Optional<StockResponseDto> TSLA = showAllStockWithTheLastPrice().stream().filter(stock -> stock.getStockName().equals("TSLA")).filter(stock -> stock.getLatestPrice() == 200 || stock.getLatestPrice() >= 200).findFirst();
        User userById = userRepository.findUserById(id);
        Account account = accountRepository.findAccountById(accountId);
        if (TSLA.isPresent()) {
            account.setBalance(account.getBalance() + requestDto.getAmount());
            accountDao.update(account.getId(), account);
            messageUtils.sendAsync(userById.getEmail(), orderSubject, soldOrder);
        }
    }

}
