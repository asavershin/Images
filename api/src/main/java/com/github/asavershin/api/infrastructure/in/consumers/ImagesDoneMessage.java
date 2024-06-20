package com.github.asavershin.api.infrastructure.in.consumers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImagesDoneMessage {
    /**
     * The unique identifier of the processed image.
     */
    private String imageId;
    /**
     * The unique identifier of the request that
     * initiated the image processing task.
     */
    private String requestId;
}
