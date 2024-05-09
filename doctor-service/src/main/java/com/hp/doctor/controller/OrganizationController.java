package com.hp.doctor.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hp.common.domain.PageResult;
import com.hp.common.domain.Result;
import com.hp.doctor.domain.po.Organization;
import com.hp.doctor.service.IOrganizationService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
@RestController
@RequestMapping("/organization")
@Slf4j
public class OrganizationController {
    @Autowired
    IOrganizationService iOrganizationService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("添加科室")
    @PostMapping
    public Result insert(@RequestBody Organization organization){
        organization.setCreateTime(LocalDateTime.now());
        organization.setUpdateTime(LocalDateTime.now());
        iOrganizationService.save(organization);
        //删除缓存数据
        redisTemplate.delete("organization");
        return Result.success();
    }
    @ApiOperation("删除科室")
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id){
        iOrganizationService.removeById(id);
        //删除缓存数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete("org",id);
        redisTemplate.delete("organization");
        return Result.success();
    }
    @ApiOperation("修改科室")
    @PutMapping
    private Result update(@RequestBody Organization organization){
        System.out.println(111);
        log.info("修改科室信息:{}",organization);
        organization.setUpdateTime(LocalDateTime.now());
        iOrganizationService.updateById(organization);
        //删除缓存数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete("org", organization.getId());
        redisTemplate.delete("organization");
        return Result.success();
    }
    @ApiOperation("查询全部科室")
    @GetMapping
    private Result<List<Organization>> getAll(){
        ListOperations listOperations = redisTemplate.opsForList();
        Long size=listOperations.size("organization");
        if (size!=null&&size!=0){
            List<Organization> list=listOperations.range("organization",0,-1);
            log.info("查询科室缓存信息:{}",list);
            return Result.success(list);
        }else {
            List<Organization> list = iOrganizationService.list();
            log.info("查询科室信息:{}",list);
            listOperations.leftPushAll("organization",list);
            return Result.success(list);
        }
    }
    @ApiOperation("根据id查询科室")
    @GetMapping("{id}")
    private Result<Organization> getById(@PathVariable Integer id){
        Organization org = iOrganizationService.getById(id);
        return Result.success(org);
    }

    @ApiOperation("分页查询科室信息")
    @GetMapping("/page")
    private Result<PageResult> page(@RequestParam(defaultValue = "1") int pageNo,@RequestParam(defaultValue = "10") int pageSize){
        PageResult page=iOrganizationService.getPage(pageNo,pageSize);
        return Result.success(page);
    }

    @ApiOperation("批量删除科室")
    @DeleteMapping("/del")
    private Result delUser(@RequestBody List<Integer> ids){
        log.info("批量删除科室，科室id:{}",ids);
        iOrganizationService.removeByIds(ids);
        //删除缓存数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        ids.forEach(id->{
            hashOperations.delete("org", id);
        });
        redisTemplate.delete("organization");
        return Result.success();
    }

    @ApiOperation("根据科室名模糊查询科室")
    @GetMapping("/name")
    private Result<List<Organization>> name(String orgName){
        log.info("根据科室名搜索科室，科室名:{}",orgName);
        QueryWrapper<Organization> wrapper=new QueryWrapper<>();
        wrapper.like("name",orgName);
        List<Organization> list = iOrganizationService.list(wrapper);
        return Result.success(list);
    }
}
