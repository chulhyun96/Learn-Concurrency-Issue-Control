package com.example.lockingtest.facade;

import com.example.lockingtest.repository.RedisLockRepository;
import com.example.lockingtest.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockFacade {
    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public LettuceLockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        //락 획득 시도
        while(!redisLockRepository.lock(id)) {
            //락 획득 실패 시 Sleep을 두고 획득 재시도
            // -> Lettuce같은 경우 구현이 쉽지만 재시도 획득 시도에 대한 것 때문에 텀을 둬야함
            Thread.sleep(100);
        }
        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
