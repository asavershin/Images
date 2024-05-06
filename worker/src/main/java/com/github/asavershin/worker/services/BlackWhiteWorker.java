package com.github.asavershin.worker.services;

import com.github.asavershin.worker.config.MinIOProperties;
import com.github.asavershin.worker.out.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
@Service
@Profile("blackwhite")
@RequiredArgsConstructor
@Slf4j
public class BlackWhiteWorker implements Worker {
    private final MinioService imageStorage;
    private final MinIOProperties props;

    @Override
    public String doWork(final String imageUUID, final boolean lastWorker) {
        InputStream is = imageStorage.getFile(imageUUID);
        byte[] rotatedImageData = convertToGrayscale(is);
        var id = UUID.randomUUID().toString();
        if (!lastWorker) {
            id = props.getTtlprefix() + id;
        }
        imageStorage.saveFile(rotatedImageData, id);
        return id;
    }

    @Override
    public String whoAmI() {
        return "BLACKWHITE";
    }

    private byte[] convertToGrayscale(final InputStream imageData) {
        try {
            BufferedImage originalImage = ImageIO.read(imageData);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            // Проход по каждому пикселю и преобразование его в черно-белый
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = originalImage.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                    int grayValue = (gray << 16) | (gray << 8) | gray;
                    grayscaleImage.setRGB(x, y, grayValue);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(grayscaleImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error converting image to grayscale: {}", e.getMessage());
            // Обработка ошибок, например, выброс исключения или возврат пустого массива
            return new byte[0];
        }
    }
}
