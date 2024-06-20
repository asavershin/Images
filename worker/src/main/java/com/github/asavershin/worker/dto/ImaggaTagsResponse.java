package com.github.asavershin.worker.dto;

import lombok.Data;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Data
@Profile("imagga")
public class ImaggaTagsResponse {
    private Result result;

    @Data
    public static class Result {
        private List<Tag> tags;
    }

    @Data
    public static class Tag {
        private double confidence;
        private TagInfo tag;
    }

    @Data
    public static class TagInfo {
        private String en;
    }

    private ImagaStatus status;

}

