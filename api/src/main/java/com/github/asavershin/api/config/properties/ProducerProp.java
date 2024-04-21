package com.github.asavershin.api.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class ProducerProp {
    /**
     * Count of publisher tries.
     */
    @Value("${app.retries}")
    @NotNull(message = "Producer properties are invalid")
    private String retries;
}
