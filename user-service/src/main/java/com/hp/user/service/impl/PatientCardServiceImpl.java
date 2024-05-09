package com.hp.user.service.impl;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.AliOSSUtils;
import com.hp.common.utils.QRCodeUtil;
import com.hp.user.domain.po.PatientCard;
import com.hp.user.mapper.PatientCardMapper;
import com.hp.user.service.IPatientCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hp.user.util.BufferedImageToMultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-30
 */
@Service
@Slf4j
public class PatientCardServiceImpl extends ServiceImpl<PatientCardMapper, PatientCard> implements IPatientCardService {

    @Autowired
    private QRCodeUtil qrCodeUtil;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private PatientCardMapper patientCardMapper;
    @Override
    public void insertCart(PatientCard patientCard) {
        UUID uuid = UUID.randomUUID();
        BufferedImage image = qrCodeUtil.createCode(uuid.toString());
        patientCard.setId(uuid.toString());
        //生成二维码图片并上传
        try {
            MultipartFile img = BufferedImageToMultipartFile.convert(image, String.valueOf(uuid));
            String url = aliOSSUtils.upload(img);
            patientCard.setQrCode(url);
            log.info("已生成二维码图片并上传");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        patientCardMapper.insert(patientCard);
    }
}
