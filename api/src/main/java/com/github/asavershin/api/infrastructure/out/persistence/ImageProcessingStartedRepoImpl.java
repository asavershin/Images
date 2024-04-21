package com.github.asavershin.api.infrastructure.out.persistence;

import asavershin.generated.package_.tables.records.ImageProcessingEventFilterRecord;
import asavershin.generated.package_.tables.records.ImageProcessingEventRecord;
import com.github.asavershin.api.domain.filter.Filter;
import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.filter.ImageProcessingStartedRepo;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

import static asavershin.generated.package_.Tables.IMAGE_PROCESSING_EVENT_FILTER;
import static asavershin.generated.package_.tables.ImageProcessingEvent.IMAGE_PROCESSING_EVENT;

@Repository
@RequiredArgsConstructor
public class ImageProcessingStartedRepoImpl
        implements ImageProcessingStartedRepo {
    /**
     * The DSL context for database operations.
     */
    private final DSLContext dsl;

    /**
     * Saves an {@link ImageProcessingStarted} event to the database.
     *
     * @param event the event to be saved
     */
    public void save(final ImageProcessingStarted event) {
        var eventRecord = createEventRecord(event);
        eventRecord.store();

        var filterRecords = createFilterRecords(event);
        dsl.batchInsert(filterRecords).execute();
    }

    private ImageProcessingEventRecord createEventRecord(
            final ImageProcessingStarted event
    ) {
        var eventRecord = dsl.newRecord(IMAGE_PROCESSING_EVENT);
        eventRecord.setImageProcessingEventId(event.requestId().value());
        eventRecord.setStatusName(event.status().toString());
        eventRecord.setOriginalImageId(event.originalImageId().value());
        return eventRecord;
    }

    private List<ImageProcessingEventFilterRecord> createFilterRecords(
            final ImageProcessingStarted event) {
        List<ImageProcessingEventFilterRecord> records = new LinkedList<>();
        for (short i = 0; i < event.filters().size(); ++i) {
            records.add(createFilterRecord(
                    event.filters().get(i), event.requestId(), i)
            );
        }
        return records;
    }

    private ImageProcessingEventFilterRecord createFilterRecord(
            final Filter filter,
            final RequestId requestId,
            final Short order) {
        var filterRecord = dsl.newRecord(
                IMAGE_PROCESSING_EVENT_FILTER);
        filterRecord.setImageFilterName(filter.toString());
        filterRecord.setImageProcessingEventId(requestId.value());
        filterRecord.setImageFilterOrder(order);
        return filterRecord;
    }
}
