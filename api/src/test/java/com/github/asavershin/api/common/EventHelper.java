package com.github.asavershin.api.common;

import com.github.asavershin.api.domain.filter.Filter;
import com.github.asavershin.api.domain.filter.RequestId;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.image.ImageId;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;

import static asavershin.generated.package_.Tables.IMAGE_PROCESSING_EVENT_FILTER;
import static asavershin.generated.package_.tables.ImageProcessingEvent.IMAGE_PROCESSING_EVENT;

public class EventHelper {
    public static final Integer MAX_FILTERS_COUNT = 32767;

    public static List<Filter> hugeList() {
        var result = new ArrayList<Filter>();
        for (int i = 0; i < 32780; i++) {
            result.add(Filter.ROTATE);
        }
        return result;
    }

    public static List<Filter> filters() {
        return List.of(Filter.ROTATE,
                Filter.BLACKWHITE);
    }

    public static ImageProcessingStarted started(final ImageId id) {
        return new ImageProcessingStarted(filters(), id);
    }

    public static @NotNull Result<Record> getEventFromDB(DSLContext dsl, RequestId id){
        return dsl.select(IMAGE_PROCESSING_EVENT.PROCESSED_IMAGE_ID,
                        IMAGE_PROCESSING_EVENT.ORIGINAL_IMAGE_ID)
                .select(IMAGE_PROCESSING_EVENT_FILTER.fields())
                .from(IMAGE_PROCESSING_EVENT)
                .join(IMAGE_PROCESSING_EVENT_FILTER).using(IMAGE_PROCESSING_EVENT_FILTER.IMAGE_PROCESSING_EVENT_ID)
                .where(IMAGE_PROCESSING_EVENT.IMAGE_PROCESSING_EVENT_ID.eq(id.value()))
                .orderBy(IMAGE_PROCESSING_EVENT_FILTER.IMAGE_FILTER_ORDER)
                .fetch();
    }
}
