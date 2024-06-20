-- liquibase formatted sql

-- changeset asavershin:create-image-processing-event-filter

CREATE TABLE image_processing_event_filter
(
    image_filter_name VARCHAR(50) NOT NULL,
    image_filter_order smallint NOT NULL,
    image_processing_event_id UUID NOT NULL,
    CONSTRAINT fk_image_processing_image_filter_image_processing_event123 FOREIGN KEY (image_processing_event_id)
    REFERENCES image_processing_event (image_processing_event_id) ON DELETE CASCADE
);

CREATE INDEX idx_image_processing_event_filter_image_processing_event_id
ON image_processing_event_filter(image_processing_event_id)