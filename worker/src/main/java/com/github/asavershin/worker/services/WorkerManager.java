package com.github.asavershin.worker.services;

import com.github.asavershin.worker.dto.Task;

public interface WorkerManager {
    Task start(Task filters);
}
