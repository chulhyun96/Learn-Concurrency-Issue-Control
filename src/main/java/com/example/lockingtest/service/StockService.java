package com.example.lockingtest.service;

import com.example.lockingtest.domain.Stock;
import com.example.lockingtest.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    public void decrease(Long productId, Long quantity) {
        /**
         * Stock 조회
         * 재고 감소
         * 갱신된 값 저장
         */
        Stock stock = stockRepository.findById(productId).orElseThrow();
        stock.decrease(quantity);
        stockRepository.save(stock);
    }
}
