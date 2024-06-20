package com.github.asavershin.worker.services;

public interface Worker {
    String doWork(String imageId, boolean lastWorker);
    String whoAmI();
}
