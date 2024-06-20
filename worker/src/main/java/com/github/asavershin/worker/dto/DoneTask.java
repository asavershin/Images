package com.github.asavershin.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoneTask {
    private String imageId;
    private String requestId;
}
