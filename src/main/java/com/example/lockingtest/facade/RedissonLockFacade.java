package com.example.lockingtest.facade;

import com.example.lockingtest.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockFacade {
    private RedissonClient redissonClient;
    private StockService stockService;

    public RedissonLockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) {
        RLock lock = redissonClient.getLock(id.toString());

        try {
            //몇초동안 락 획득을 위해 기다릴 건지 10초, 몇초 동안 1초 점유 할 건지
            boolean isLock = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!isLock) {
                System.out.println("Lock 획득 실패");
                return;
            }
            //락 획득 시 재고 감소
            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
