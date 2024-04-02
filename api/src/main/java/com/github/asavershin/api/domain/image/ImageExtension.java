package com.github.asavershin.api.domain.image;

import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
public enum ImageExtension {
    JPG(".jpg"),
    PNG(".png"),
    JPEG(".jpeg");


    private final String extension;
    private ImageExtension(String extension){
        this.extension = extension;
    }

    @Override
    public String toString() {
        return extension;
    }

    private static final Map<String, ImageExtension> stringToEnum
            = Stream.of(values()).collect(toMap(Object::toString, e -> e));

    public static ImageExtension fromString(String extension){
        var imageExtension = stringToEnum.get(extension);
        if(imageExtension == null){
            throw new IllegalArgumentException("Invalid extension: " + extension);
        }
        return imageExtension;
    }
}
