package com.github.asavershin.worker;

public interface Worker {
    String doWork(String imageId, boolean lastWorker);
    String whoAmI();
}
