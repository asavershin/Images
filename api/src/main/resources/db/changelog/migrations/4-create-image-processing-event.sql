-- liquibase formatted sql

-- changeset asavershin:create-image_processing_event

CREATE TABLE image_processing_event
(
    image_processing_event_id UUID PRIMARY KEY,
    status_name VARCHAR(10) NOT NULL,
    original_image_id UUID NOT NULL,
    processed_image_id UUID,
    CONSTRAINT fk_image_processing_events_original_image FOREIGN KEY (original_image_id) REFERENCES image (image_id) ON DELETE CASCADE
);

CREATE INDEX idx_image_processing_events_original_image ON image_processing_event (original_image_id);
CREATE INDEX idx_image_processing_events_processed_image ON image_processing_event (processed_image_id);