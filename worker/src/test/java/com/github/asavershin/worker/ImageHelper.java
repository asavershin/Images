package com.github.asavershin.worker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Slf4j
public class ImageHelper {
    public static byte[] createTestImage(int width, int height) {
        try {
            // Создаем пустое изображение с заданными размерами
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Задаем цвета для полос на изображении
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};

            // Рисуем полосы разных цветов
            Graphics2D graphics = image.createGraphics();
            for (int i = 0; i < colors.length; i++) {
                graphics.setColor(colors[i]);
                graphics.fillRect(0, i * height / colors.length, width, (i + 1) * height / colors.length);
            }
            graphics.dispose();

            // Конвертируем изображение в массив байтов
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageData = outputStream.toByteArray();
            outputStream.close();

            return imageData;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error with creating test image");
        }
    }
}
