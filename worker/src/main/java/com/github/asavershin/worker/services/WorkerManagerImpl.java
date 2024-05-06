package com.github.asavershin.worker;

import com.github.asavershin.worker.out.CacheRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WorkerManagerImpl implements WorkerManager {
    private final Worker worker;
    private final CacheRepository cache;
    @Value("${cache-expiration}")
    @NotNull
    private Long cacheExp;

    @Override
    public Task start(final Task task) {
        if (!Objects.equals(
                task.getFilters().get(0),
                worker.whoAmI())) {
            throw new RuntimeException("Invalid worker");
        }
        var cached = cache.getCache(
                task.getRequestId() + task.getImageId());
        if (task.getFilters().isEmpty()
                || cached != null
        ) {
            return null;
        }
        cache.addCache(
                task.getRequestId() + task.getImageId(),
                "",
                cacheExp
        );
        var imageId = worker.doWork(task.getImageId(),
                task.getFilters().size() == 1
        );
        task.getFilters().remove(0);
        return task;
    }
}
