package com.github.asavershin.worker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImaggaUploadResponse {
    private Result result;
    private ImagaStatus status;
    @Data
    public static class Result {
        @JsonProperty("upload_id")
        private String uploadId;
    }
}
