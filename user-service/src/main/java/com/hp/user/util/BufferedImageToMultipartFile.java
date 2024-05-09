package com.hp.user.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BufferedImageToMultipartFile {
    public static MultipartFile convert(BufferedImage image, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        if (!filename.toLowerCase().endsWith(".png")) {
            filename += ".png";
        }
        MultipartFile multipartFile = new MockMultipartFile(
                filename,
                filename,
                "image/png",
                inputStream
        );
        return multipartFile;
    }
}
