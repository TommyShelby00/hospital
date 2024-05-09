package com.hp.user.service.impl;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PageUtil;
import com.aliyun.oss.AliOSSUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.common.exception.BadRequestException;
import com.hp.common.exception.UnauthorizedException;
import com.hp.common.utils.QRCodeUtil;
import com.hp.user.config.JwtProperties;
import com.hp.user.domain.dto.LoginDTO;
import com.hp.user.domain.po.PatientCard;
import com.hp.user.domain.po.User;
import com.hp.user.domain.vo.LoginVO;
import com.hp.user.mapper.PatientCardMapper;
import com.hp.user.mapper.UserMapper;
import com.hp.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hp.user.util.BufferedImageToMultipartFile;
import com.hp.user.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-01-26
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 1.数据校验
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 2.根据用户名查询
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (user==null){
            throw new BadRequestException("用户名不存在");
        }
        //3.校检密码是否正确
        if (!password.equals(user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        StpUtil.checkDisable(user.getId());
        StpUtil.login(user.getId());
        // 5.封装VO返回
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setToken(StpUtil.getTokenValue());
        return vo;

    }
}
