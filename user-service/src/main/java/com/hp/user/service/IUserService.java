package com.hp.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hp.user.domain.dto.LoginDTO;
import com.hp.user.domain.po.PatientCard;
import com.hp.user.domain.po.User;
import com.hp.user.domain.vo.LoginVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-01-26
 */
public interface IUserService extends IService<User> {

    LoginVO login(LoginDTO loginDTO);


}
