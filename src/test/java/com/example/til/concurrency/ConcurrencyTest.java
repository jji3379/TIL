package com.example.til.concurrency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrencyTest {

    private static final int NUM_THREADS = 100;
    private static final int INCREMENTS_PER_THREAD = 1000;

    private int unsafeCount = 0;
    private int synchronizedCount = 0;
    private volatile int volatileCount = 0;
    private AtomicInteger atomicCount = new AtomicInteger(0);

    @Test
    @DisplayName("동시성 제어 X")
    public void testUnsafeIncrement() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCount++;
                }
            });
        }

        shutdownExecutorServiceAndAwaitTermination(executorService);
        long endTime = System.currentTimeMillis();
        assertEquals(NUM_THREADS * INCREMENTS_PER_THREAD, unsafeCount);
        System.out.println("Unsafe time (ms): " + (endTime - startTime));
    }
    @Test
    @DisplayName("volatile 키워드를 사용한 경우")
    public void testVolatileIncrement() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementVolatile();
                }
            });
        }

        shutdownExecutorServiceAndAwaitTermination(executorService);
        long endTime = System.currentTimeMillis();
        assertEquals(NUM_THREADS * INCREMENTS_PER_THREAD, volatileCount);
        System.out.println("Volatile time (ms): " + (endTime - startTime));
    }

    @Test
    @DisplayName("synchronized 키워드를 사용한 경우")
    public void testSynchronizedIncrement() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementSynchronized();
                }
            });
        }

        shutdownExecutorServiceAndAwaitTermination(executorService);
        long endTime = System.currentTimeMillis();
        assertEquals(NUM_THREADS * INCREMENTS_PER_THREAD, synchronizedCount);
        System.out.println("Synchronized time (ms): " + (endTime - startTime));
    }


    @Test
    @DisplayName("Atomic을 사용한 경우")
    public void testAtomicIncrement() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementAtomic();
                }
            });
        }

        shutdownExecutorServiceAndAwaitTermination(executorService);
        long endTime = System.currentTimeMillis();
        assertEquals(NUM_THREADS * INCREMENTS_PER_THREAD, atomicCount.get());
        System.out.println("Atomic time (ms): " + (endTime - startTime));
    }

    private synchronized void incrementSynchronized() {
        try {
            synchronizedCount++;
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void incrementAtomic() {
        try {
            atomicCount.incrementAndGet();
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void incrementVolatile() {
        try {
            volatileCount++;
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void shutdownExecutorServiceAndAwaitTermination(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.MINUTES);
    }
}
