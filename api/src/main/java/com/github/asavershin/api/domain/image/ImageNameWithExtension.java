package com.github.asavershin.api.domain.image;

import com.github.asavershin.api.common.Validator;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
@Getter
@EqualsAndHashCode
public class ImageNameWithExtension {

    private static final int MAX_IMAGE_NAME = 50;
    private final String imageName;
    private final ImageExtension imageExtension;

    private ImageNameWithExtension(String imageName, ImageExtension imageExt){
        this.imageName = imageName;
        this.imageExtension = imageExt;
    }

    public static ImageNameWithExtension fromOriginalFileName(String originalFileName){
        notNullValidate(originalFileName);
        String[] parts = originalFileName.split("\\.");
        Validator.assertArrayLength(parts, 2, "Incorrect image format");
        var extension = ImageExtension.fromString("." + parts[parts.length-1]);
        var imageName = String.join(".", Arrays.copyOfRange(parts, 0, parts.length - 1));
        lengthNameValidate(imageName);

        return new ImageNameWithExtension(imageName, extension);
    }
    public static ImageNameWithExtension founded(String imageName, String extension) {
        notNullValidate(imageName);
        lengthNameValidate(imageName);
        return new ImageNameWithExtension(imageName, ImageExtension.fromString(extension));
    }

    private static void notNullValidate(String name){
        Objects.requireNonNull(name, "ImageName must not be null");
    }

    private static void lengthNameValidate(String name){
        Validator.assertArgumentLength(name,0, MAX_IMAGE_NAME,
                "ImageName must be " + 0 + "-" + MAX_IMAGE_NAME + " in length");
    }

    @Override
    public String toString() {
        return imageName + imageExtension;
    }
}
