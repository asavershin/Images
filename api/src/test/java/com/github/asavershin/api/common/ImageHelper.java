package com.github.asavershin.api.common;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageNameWithExtension;
import com.github.asavershin.api.domain.image.MetaData;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageHelper {
    public static String illegalExtension = ".pdf";
    public static ImageId imageId(){
        return ImageId.nextIdentity();
    }
    public static MetaData metaInfo1(){
        return new MetaData(ImageNameWithExtension.fromOriginalFileName("image.jpg"), 1L);
    }

    public static MetaData metaInfo3(){
        return new MetaData(ImageNameWithExtension.fromOriginalFileName("image3.jpg"), 3L);
    }

    public static MultipartFile multipartFile1() {
        return new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[]{0, 1});
    }

    public static MultipartFile multipartFileWithIllegalException() {
        return new MockMultipartFile("image" + illegalExtension,
                "image" + illegalExtension,
                "image/jpeg", new byte[]{0, 1});
    }
}
