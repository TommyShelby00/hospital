package com.hp.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class QRCodeUtil {
    @Autowired
    private QrConfig config;

    //生成到文件
    public void createCodeToFile(String content, String filePath) {
        try {
            QrCodeUtil.generate(content, config, FileUtil.file(filePath));
        } catch (QrCodeException e) {
            e.printStackTrace();
        }
    }
    //生成到流
    public void createCodeToStream(String content, HttpServletResponse response) {
        try {
            QrCodeUtil.generate(content,config, "png", response.getOutputStream());
        } catch (QrCodeException | IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage createCode(String content){
        return QrCodeUtil.generate(content, config);
    }
}
