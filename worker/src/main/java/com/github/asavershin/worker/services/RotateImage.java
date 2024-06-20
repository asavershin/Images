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
@Profile("rotate")
@RequiredArgsConstructor
@Slf4j
public class RotateImage implements Worker {
    private final MinioService imageStorage;
    private final MinIOProperties props;

    @Override
    public String doWork(final String imageUUID, final boolean lastWorker) {
        InputStream is = imageStorage.getFile(imageUUID);
        byte[] rotatedImageData = rotateImage90Degrees(is);

        var id = UUID.randomUUID().toString();
        if (!lastWorker) {
            id = props.getTtlprefix() + id;
        }
        imageStorage.saveFile(rotatedImageData, id);
        return id;
    }

    @Override
    public String whoAmI() {
        return "ROTATE";
    }

    private byte[] rotateImage90Degrees(final InputStream imageData) {
        try {
            BufferedImage originalImage = ImageIO.read(imageData);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            BufferedImage rotatedImage = new BufferedImage(height, width, originalImage.getType());

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    rotatedImage.setRGB(height - 1 - j, i, originalImage.getRGB(i, j));
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error rotating image: {}", e.getMessage());
            // Обработка ошибок, например, выброс исключения или возврат пустого массива
            return new byte[0];
        }
    }
}