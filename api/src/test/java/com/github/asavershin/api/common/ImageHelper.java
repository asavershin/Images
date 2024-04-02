package com.github.asavershin.api.common;

import com.github.asavershin.api.domain.image.ImageId;
import com.github.asavershin.api.domain.image.ImageNameWithExtension;
import com.github.asavershin.api.domain.image.MetaInfo;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageHelper {
    public static String illegalExtension = ".pdf";
    public static ImageId imageId(){
        return ImageId.nextIdentity();
    }
    public static MetaInfo metaInfo1(){
        return new MetaInfo(ImageNameWithExtension.fromOriginalFileName("image.jpg"), 1L);
    }

    public static MetaInfo metaInfo3(){
        return new MetaInfo(ImageNameWithExtension.fromOriginalFileName("image3.jpg"), 3L);
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
