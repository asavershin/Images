package com.github.asavershin.images;

import com.github.asavershin.images.out.CacheRepository;
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
        var cached = cache.getCache(
                task.getRequestId() + task.getImageId());
        if (task.getFilters().isEmpty()
                || !Objects.equals(
                task.getFilters().get(0),
                worker.whoAmI())
                || cached != null
        ) {
            return null;
        }

        var imageId = worker.doWork(task.getImageId(),
                task.getFilters().size() == 1
        );
        cache.addCache(
                task.getRequestId() + imageId,
                "",
                cacheExp
        );
        task.getFilters().remove(0);
        return task;
    }
}
