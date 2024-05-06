package com.github.asavershin.images;

import java.io.IOException;

public interface Worker {
    String doWork(String imageId, boolean lastWorker);
    String whoAmI();
}
