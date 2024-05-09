package com.hp.user.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.common.domain.PageResult;
import com.hp.common.domain.Result;
import com.hp.common.exception.UnauthorizedException;
import com.hp.common.utils.StpAdminUtil;
import com.hp.user.domain.po.Admin;
import com.hp.user.domain.po.PatientCard;
import com.hp.user.domain.po.User;
import com.hp.user.service.IAdminService;
import com.hp.user.service.IPatientCardService;
import com.hp.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Wrapper;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@RestController
@RequestMapping("/admin")
@SaCheckLogin(type = StpAdminUtil.TYPE)
@Slf4j
public class AdminController {
    @Autowired
    IUserService iUserService;
    @Autowired
    IAdminService iAdminService;
    @Autowired
    IPatientCardService iPatientCardService;

    @PostMapping("/login")
    @SaIgnore
    private Result<String> adminLogin(@RequestBody Admin admin) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", admin.getUsername());
        Admin one = iAdminService.getOne(wrapper);
        if (one == null || !Objects.equals(admin.getPassword(), one.getPassword())) {
            throw new UnauthorizedException("账号密码错误");
        }
        StpAdminUtil.login(one.getId());
        return Result.success(StpUtil.getTokenValue());
    }

    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @GetMapping("/queryUser")
    private Result<PageResult> queryUser(Integer pageNo, Integer pageSize, String username, String phone, Integer age) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (username != null && username != "")
            wrapper.like("username", username);
        if (phone != null && phone != "")
            wrapper.like("phone", phone);
        if (age != null)
            wrapper.eq("age", age);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<User> p = iUserService.page(new Page<>(pageNo, pageSize), wrapper);
        // 4.数据
        List<User> records = p.getRecords();
        System.out.println(records);
        return Result.success(new PageResult(p.getTotal(), records));
    }

    @ApiOperation("批量删除用户")
    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @DeleteMapping("/user")
    private Result delUser(@RequestBody List<String> ids) {
        log.info("批量删除用户，用户id:{}", ids);
        iUserService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation("批量删除就诊卡")
    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @DeleteMapping("/card")
    private Result delCard(@RequestBody List<String> ids) {
        log.info("批量删除就诊卡，就诊卡id:{}", ids);
        iPatientCardService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation("封禁账号")
    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @PostMapping("/ban")
    private Result banUser(@RequestBody List<String> ids) {
        log.info("封禁账号:{}", ids);
        ids.forEach(id -> {
            StpUtil.kickout(id);
            StpUtil.disable(id, 86400);
        });
        return Result.success();
    }

    @ApiOperation("更改用户信息")
    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @PutMapping("/2")
    private Result update(@RequestBody User user) {
        log.info("更改用户信息:{}", user);
        iUserService.updateById(user);
        return Result.success();
    }

    @ApiOperation("查询所有就诊卡")
    @SaCheckRole(value = "admin", type = StpAdminUtil.TYPE)
    @GetMapping("/queryCard")
    private Result<PageResult> queryCard(Integer pageNo, Integer pageSize, String name, String phone, String identity) {
        QueryWrapper<PatientCard> wrapper = new QueryWrapper<>();
        if (name != null && name != "")
            wrapper.like("name", name);
        if (phone != null && phone != "")
            wrapper.like("phone", phone);
        if (identity != null && identity != "")
            wrapper.eq("identity", identity);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<PatientCard> p = iPatientCardService.page(new Page<>(pageNo, pageSize), wrapper);
        // 4.数据
        List<PatientCard> records = p.getRecords();
        System.out.println(records);
        return Result.success(new PageResult(p.getTotal(), records));
    }

}