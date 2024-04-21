package com.github.asavershin.api.domaintest;

import com.github.asavershin.api.domain.filter.Filter;
import com.github.asavershin.api.domain.filter.ImageProcessingResult;
import com.github.asavershin.api.domain.filter.ImageProcessingStarted;
import com.github.asavershin.api.domain.filter.Status;
import com.github.asavershin.api.domain.image.ImageId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.asavershin.api.common.EventHelper.MAX_FILTERS_COUNT;
import static com.github.asavershin.api.common.EventHelper.hugeList;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterEventTest {
    @Test
    public void testImageProcessingStartedConstructor() {
        // Given
        ImageId imageId = ImageId.nextIdentity();
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.CROP);
        // When
        var event = new ImageProcessingStarted(filters, imageId);

        // Then
        assertNotNull(event);
        assertNotNull(event.requestId());
        assertEquals(imageId, event.originalImageId());
        assertEquals(filters, event.filters());
        assertNotNull(event.status());
    }

    @Test
    public void testNullOriginalImageIdToNewEvent() {
        // Given
        var lotsOfFilters = hugeList();
        var zeroFilters = new ArrayList<Filter>();
        var normalFilters = List.of(Filter.CROP);

        // When
        var nullIdEx = assertThrows(NullPointerException.class,
                () -> new ImageProcessingStarted(normalFilters, null));

        // Then
        assertEquals(nullIdEx.getMessage(), "ImageId must not be null");
    }

    @Test
    public void testZeroFiltersToNewEvent() {
        // Given
        var zeroFilters = new ArrayList<Filter>();

        // When
        var nullIdEx = assertThrows(IllegalArgumentException.class,
                () -> new ImageProcessingStarted(zeroFilters, ImageId.nextIdentity()));

        // Then
        assertEquals(nullIdEx.getMessage(),
                "Must be more than zero filters and less than " + MAX_FILTERS_COUNT);
    }

    @Test
    void testPublish() {
        // Arrange
        var originalImageId = ImageId.nextIdentity();
        List<Filter> filters = List.of(Filter.CROP, Filter.REMOVE_BACKGROUND);
        ImageProcessingStarted imageProcessingStarted = new ImageProcessingStarted(filters, originalImageId);

        // Act
        var result = imageProcessingStarted.publish();

        // Assert
        assertEquals(originalImageId.value().toString(), result.imageId());
        assertEquals(imageProcessingStarted.requestId().value().toString(), result.requestId());
        assertEquals(filters.stream().map(Filter::toString).toList(), result.filters());
    }

    @Test
    public void testHugeListOfFiltersToNewEvent() {
        // Given
        var lotsOfFilters = hugeList();

        // When
        var nullIdEx = assertThrows(IllegalArgumentException.class,
                () -> new ImageProcessingStarted(lotsOfFilters, ImageId.nextIdentity()));

        // Then
        assertEquals(nullIdEx.getMessage(),
                "Must be more than zero filters and less than " + MAX_FILTERS_COUNT);
    }



    @Test
    public void testImageProcessingResultConstructor() {
        var id = ImageId.nextIdentity();
        var nullStatusEx = assertThrows(NullPointerException.class, () -> new ImageProcessingResult(null, id));
        var invalidStatus = assertThrows(NullPointerException.class, () -> new ImageProcessingResult(Status.DONE, null));
        assertDoesNotThrow(() -> new ImageProcessingResult(Status.WIP, id));
        assertEquals(nullStatusEx.getMessage(), "Status must not be null");
        assertEquals(invalidStatus.getMessage(), "ImageId must not be null");
    }

    private List<Filter> createFiltersWithMaxCount() {
        List<Filter> filters = new ArrayList<>();
        for (int i = 0; i < 32780; i++) {
            filters.add(Filter.CROP);
        }
        return filters;
    }
}
