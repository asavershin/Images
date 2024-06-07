-- liquibase formatted sql

-- changeset asavershin:createImage
CREATE TABLE image
(
    image_id   UUID PRIMARY KEY,
    image_name varchar(50)  NOT NULL,
    image_size bigint       not null
);

CREATE INDEX idx_image_id ON image(image_id);

CREATE TABLE user_images
(
    image_id UUID,
    user_id UUID,
    CONSTRAINT fk_user_images_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_users_image_image FOREIGN KEY (image_id) REFERENCES image (image_id) ON DELETE CASCADE
);

CREATE INDEX idx_users_image_user_id ON user_images (user_id);
CREATE INDEX idx_users_image_image_id ON user_images (image_id);

