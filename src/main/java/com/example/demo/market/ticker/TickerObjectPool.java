/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.ticker;

import com.example.demo.model.Ticker;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import stormpot.Allocator;
import stormpot.Pool;
import stormpot.Pooled;
import stormpot.Slot;

public class TickerObjectPool implements InitializingBean, DisposableBean {

    Allocator<Pooled<Ticker>> allocator = new Allocator<Pooled<Ticker>>() {

        @Override
        public Pooled<Ticker> allocate(Slot slot) throws Exception {
            return new Pooled<>(slot, new Ticker());
        }

        @Override
        public void deallocate(Pooled<Ticker> pooled) throws Exception {}
    };

    private final Pool<Pooled<Ticker>> pool;

    public TickerObjectPool(int size) {
        pool = Pool.from(allocator).setSize(size).build();
    }

    public Pooled<Ticker> borrowObject() {
        return pool.tryClaim();
    }

    public void returnObject(Pooled<Ticker> object) {
        object.release();
    }

    public void warmUp(int count) {}

    public long size() {
        return pool.getTargetSize();
    }

    public void shutdown() {
        pool.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        warmUp(100_000);
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }
}
