package com.hp.user.service.impl;

import com.hp.user.domain.po.Admin;
import com.hp.user.mapper.AdminMapper;
import com.hp.user.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
