package com.github.asavershin.api.config;

import com.github.asavershin.api.common.annotations.Command;
import com.github.asavershin.api.common.annotations.DomainService;
import com.github.asavershin.api.common.annotations.Query;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.github.asavershin.api",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = DomainService.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = Query.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        value = Command.class)
        }
)
public class AnnotationsConfig {
}
