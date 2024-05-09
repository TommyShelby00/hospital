package com.hp.doctor.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.AliOSSUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.common.domain.PageResult;
import com.hp.common.domain.Result;
import com.hp.common.exception.BadRequestException;
import com.hp.common.utils.StpAdminUtil;
import com.hp.common.utils.StpUserUtil;
import com.hp.doctor.domain.po.Doctor;
import com.hp.doctor.domain.po.Organization;
import com.hp.doctor.domain.vo.DoctorVO;
import com.hp.doctor.domain.vo.LoginVO;
import com.hp.doctor.service.IDoctorService;
import com.hp.doctor.service.IOrganizationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {
    @Autowired
    IDoctorService iDoctorService;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    IOrganizationService iOrganizationService;
    @Autowired
    private RedisTemplate redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("新增医生")
    @PostMapping
    @SaCheckLogin(type = StpAdminUtil.TYPE)
    private Result insert(@RequestBody Doctor doctor) {
        iDoctorService.save(doctor);
        return Result.success();
    }

    @ApiOperation("修改医生")
    @PutMapping

    private Result update(@RequestBody Doctor doctor) {
        Doctor doc = iDoctorService.getById(doctor.getId());
        //通知排班表科室变化
        if (doc.getOrgId() != doctor.getOrgId()) {
            Map<String, Integer> map = new HashMap<>();
            map.put("docId", doctor.getId());
            map.put("orgId", doctor.getOrgId());
            rabbitTemplate.convertAndSend("doctor.topic", "orgId.change", map);
        }
        iDoctorService.updateById(doctor);
        //删除缓存数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete("doctor", doctor.getId());
        return Result.success();
    }

    @ApiOperation("删除医生")
    @DeleteMapping
    private Result delete(@RequestBody List<Integer> ids) {
        log.info("批量删除医生，医生id:{}", ids);
        iDoctorService.removeByIds(ids);
        //删除缓存数据
        HashOperations hashOperations = redisTemplate.opsForHash();
        ids.forEach(id -> {
            hashOperations.delete("doctor", id);
        });
        return Result.success();
    }

    @ApiOperation("根据id查询医生")
    @GetMapping("{id}")
    private Result<Doctor> getById(@PathVariable Integer id) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Doctor doctor = new Doctor();
        String s = (String) hashOperations.get("doctor", id);
        //查询是否有缓存数据
        if (s == null || s == "") {
            //缓存未命中，查询数据库并存入缓存
            doctor = iDoctorService.getById(id);
            String s1 = JSONUtil.toJsonStr(doctor);
            hashOperations.put("doctor", id, s1);
        } else {
            JSONObject jsonObject = JSONUtil.parseObj(s);
            doctor = jsonObject.toBean(Doctor.class);
        }
        return Result.success(doctor);
    }


    @ApiOperation("根据科室id批量查询医生")
    @GetMapping
    private Result<List<Doctor>> getByOrgId(@RequestParam Integer id) {
        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        wrapper.eq("org_id", id);
        List<Doctor> list = iDoctorService.list(wrapper);
        log.info("查询信息：{}", list);
        return Result.success(list);
    }

    @ApiOperation("图像上传")
    @PostMapping("/upload")
    private Result<String> upload(MultipartFile image) throws Exception {
        String url = aliOSSUtils.upload(image);
        return Result.success(url);
    }

    @ApiOperation("批量查询医生")
    @GetMapping("/page")
    private Result<PageResult> queryDoctor(int pageNo, int pageSize, String name, Integer orgId, String position) {
        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        if (name != null && name != "")
            wrapper.like("name", name);
        if (position != null && position != "")
            wrapper.like("position", position);
        if (orgId != null)
            wrapper.like("org_id", orgId);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<Doctor> p = iDoctorService.page(new Page<>(pageNo, pageSize), wrapper);
        // 4.数据
        List<Doctor> records = p.getRecords();
        List<DoctorVO> ls = new ArrayList<>();
        records.forEach(i -> {
            DoctorVO doctorVO = new DoctorVO();
            Organization org = iOrganizationService.getById(i.getOrgId());
            BeanUtils.copyProperties(i, doctorVO);
            doctorVO.setOrgName(org.getName());
            ls.add(doctorVO);
        });
        return Result.success(new PageResult(p.getTotal(), ls));
    }

    @ApiOperation("医生登录")
    @PostMapping("/login")
    @SaIgnore
    private Result<LoginVO> docLogin(@RequestBody Doctor doctor) {
        log.info("医生登录：{}", doctor);
        String name = doctor.getName();
        String password = doctor.getPassword();
        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Doctor doc = iDoctorService.getOne(wrapper);
        if (doc==null){
            throw new BadRequestException("用户信息不存在");
        }
        if (!Objects.equals(password, doc.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        StpUserUtil.login(doc.getId());
        LoginVO loginVO = new LoginVO();
        loginVO.setId(doc.getId());
        loginVO.setName(doc.getName());
        loginVO.setImg(doc.getImg());
        loginVO.setToken(StpUtil.getTokenValue());
        return Result.success(loginVO);
    }

    @ApiOperation("根据医生名模糊查询医生")
    @GetMapping("/name")
    private Result<List<Doctor>> name(String docName){
        log.info("根据科室名搜索科室，科室名:{}",docName);
        QueryWrapper<Doctor> wrapper=new QueryWrapper<>();
        wrapper.like("name",docName);
        List<Doctor> list = iDoctorService.list(wrapper);
        return Result.success(list);
    }
}
