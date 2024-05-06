package com.github.asavershin.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String imageId;
    private String requestId;
    private List<String> filters;
}
