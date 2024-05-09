package com.hp.user.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.aliyun.oss.AliOSSUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hp.api.client.DoctorClient;
import com.hp.api.vo.Doctor;
import com.hp.common.domain.Result;
import com.hp.user.domain.dto.LoginDTO;
import com.hp.user.domain.po.PatientCard;
import com.hp.user.domain.po.User;
import com.hp.user.domain.vo.LoginVO;
import com.hp.user.service.IPatientCardService;
import com.hp.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-01-26
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@SaCheckLogin
public class UserController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private IPatientCardService iPatientCardService;
    @ApiOperation("登录")
    @PostMapping("/login")
    @SaIgnore
    private Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO);
        LoginVO loginVO = iUserService.login(loginDTO);
        return Result.success(loginVO);
    }

    @ApiOperation("注册")
    @PostMapping("/1")
    @SaIgnore
    private Result register(@RequestBody User user) {
        log.info("用户注册：{}", user);
        iUserService.save(user);
        return Result.success();
    }

    @ApiOperation("更改用户信息")
    @PutMapping("/2")
    private Result update(@RequestBody User user){
        user.setId((String) StpUtil.getLoginId());
        log.info("更改用户信息:{}",user);
        iUserService.updateById(user);
        return Result.success();
    }

    @ApiOperation("查询用户信息")
    @GetMapping
    private Result<User> queryUser(){
        String id= (String) StpUtil.getLoginId();
        log.info("查询用户信息,用户id{}",id);
        User user = iUserService.getById(id);
        return Result.success(user);
    }

    @ApiOperation("头像上传")
    @PostMapping("/upload")
    @SaIgnore
    private Result<String> upload(MultipartFile image) throws Exception{
        String url=aliOSSUtils.upload(image);
        log.info("上传成功");
        return Result.success(url);
    }

    @ApiOperation("添加就诊卡")
    @PostMapping("/addCard")
    private Result<PatientCard> insertCart(@RequestBody PatientCard patientCard){
        iPatientCardService.insertCart(patientCard);
        return Result.success(patientCard);
    }

    @ApiOperation("查询就诊卡")
    @GetMapping("/queryCard")
    private Result<PatientCard> queryCart(String id){
        QueryWrapper<PatientCard> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",id);
        PatientCard card = iPatientCardService.getOne(wrapper);
        return Result.success(card);
    }

    @ApiOperation("编辑就诊卡")
    @PutMapping("/updateCard")
    private Result<PatientCard> updateCart(@RequestBody PatientCard patientCard){
        log.info("编辑就诊卡:{}",patientCard);
        iPatientCardService.updateById(patientCard);
        return Result.success(patientCard);
    }

    @ApiOperation("退出登录")
    @GetMapping("/exit")
    private Result exit(String id){
        StpUtil.kickout(id);
        return Result.success();
    }


}