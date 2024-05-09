package com.hp.doctor.service.impl;

import com.hp.doctor.domain.po.Doctor;
import com.hp.doctor.mapper.DoctorMapper;
import com.hp.doctor.service.IDoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {

}
