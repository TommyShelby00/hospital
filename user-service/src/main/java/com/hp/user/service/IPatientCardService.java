package com.hp.user.service;

import com.hp.user.domain.po.PatientCard;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-04-30
 */
public interface IPatientCardService extends IService<PatientCard> {
    void insertCart(PatientCard patientCard);
}
