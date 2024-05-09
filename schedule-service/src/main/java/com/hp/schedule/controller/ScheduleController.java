package com.hp.schedule.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.api.client.DoctorClient;
import com.hp.api.vo.Doctor;
import com.hp.api.vo.Organization;
import com.hp.common.domain.PageResult;
import com.hp.common.domain.Result;
import com.hp.common.exception.DbException;
import com.hp.schedule.domain.po.Schedule;
import com.hp.schedule.domain.vo.*;
import com.hp.schedule.service.IScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-02-14
 */
@RestController
@RequestMapping("/schedule")
@SaCheckLogin
@RequiredArgsConstructor
@Api("排班管理")
@Slf4j
public class ScheduleController {
    @Autowired
    IScheduleService iScheduleService;

    private final DoctorClient doctorClient;

    private final RedisTemplate redisTemplate;

    @ApiOperation("新增排班信息")
    @PostMapping
    private Result save(@RequestBody Schedule schedule){
        iScheduleService.save(schedule);
        String key="sch:"+schedule.getDoctorId()+","+schedule.getDate();
        redisTemplate.delete(key);
        return Result.success();
    }

    @ApiOperation("删除排班信息")
    @DeleteMapping
    private Result delete(Integer id){
        Schedule sch = iScheduleService.getById(id);
        iScheduleService.removeById(id);
        String key="sch:"+sch.getDoctorId()+","+sch.getDate();
        redisTemplate.delete(key);
        return Result.success();
    }

    @ApiOperation("修改排班信息")
    @PutMapping
    private Result update(@RequestBody Schedule schedule){
        Schedule sch = iScheduleService.getById(schedule.getId());
        iScheduleService.updateById(schedule);
        String key="sch:"+sch.getDoctorId()+","+sch.getDate();
        redisTemplate.delete(key);
        return Result.success();
    }

    @ApiOperation("根据id查看排班信息")
    @GetMapping("{id}")
    private Result<Schedule> getById(@PathVariable Integer id){
        Schedule sch = iScheduleService.getById(id);
        return Result.success(sch);
    }

    @ApiOperation("扣减号资源")
    @PutMapping("/deduct")
    private Result deduct(@RequestParam Integer id){
        log.info("扣减号资源:{}",id);
        Schedule sch = iScheduleService.getById(id);
        if (sch.getRemind()>0){
            UpdateWrapper<Schedule> wrapper=new UpdateWrapper<Schedule>()
                    .setSql("remind=remind-1")
                    .eq("id",id);
            iScheduleService.update(wrapper);
            //清除redis数据
            LocalDate date = sch.getDate();
            Integer doctorId = sch.getDoctorId();
            redisTemplate.delete("sch:"+doctorId+","+date);
            return Result.success();
        }
        else {
            throw new DbException("剩余号资源不足");
        }
    }



    @ApiOperation("根据科室id查看有号医生")
    @GetMapping("/category")
    private Result<List<DoctorVO>> category(Integer id,@DateTimeFormat (pattern = "yyyy-MM-dd") LocalDate date){
        List<DoctorVO> list=iScheduleService.category(id,date);
        return Result.success(list);
    }

    @ApiOperation("查看医生号大致信息")
    @GetMapping("/doctor")
    private Result<DocDetailVO> detail(Integer id){
        log.info("查看医生号大致信息");
        DocDetailVO docDetailVO= iScheduleService.detail(id);
        return Result.success(docDetailVO);
    }

    @ApiOperation("查看医生指定日期号详细信息")
    @GetMapping("/date")
    private Result<List<DateVO>> dateDetail(Integer id, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        ListOperations listOperations = redisTemplate.opsForList();
        String key="sch:"+id+","+date;
        //查询缓存，未命中则查询数据库
        Long size=listOperations.size(key);
        if (size!=null&&size!=0){
            List<DateVO> ls=listOperations.range(key,0,-1);
            log.info("查询到医生指定日期号缓存信息:{}",ls);
            return Result.success(ls);
        }
        List<DateVO> list=iScheduleService.dateDetail(id,date);
        listOperations.leftPushAll(key,list);
        log.info("查询到医生指定日期号信息:{}",list);
        return Result.success(list);
    }

    @ApiOperation("查询所有科室指定日期剩余号源")
    @GetMapping("/orgRemind")
    private Result<List<OrgRemindVO>> queryOrgRemind(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        log.info("查询所有科室指定日期剩余号源,日期:{}",date);
        List<OrgRemindVO> ls=iScheduleService.queryOrgRemind(date);
        log.info("获取信息：{}",ls);
        return Result.success(ls);
    }

    @ApiOperation("查询某科室所有医生指定日期剩余号源")
    @GetMapping("/docRemind")
    private Result<List<DoctorVO>> queryOrgRemind(Integer id,@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        log.info("根查询所有科室指定日期剩余号源,科室id:{},日期:{}",id,date);
        List<DoctorVO> ls=iScheduleService.queryDocRemind(id,date);
        log.info("获取信息：{}",ls);
        return Result.success(ls);
    }

    @ApiOperation("批量查询排班信息")
    @GetMapping("/page")
    private Result<PageResult> queryDoctor(int pageNo, int pageSize, Integer orgId, Integer doctorId, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date){
        QueryWrapper<Schedule> wrapper=new QueryWrapper<>();
        if (orgId!=null)
            wrapper.eq("org_id",orgId);
        if (doctorId!=null)
            wrapper.eq("doctor_id",doctorId);
        if (date!=null)
            wrapper.eq("date",date);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<Schedule> p = iScheduleService.page(new Page<>(pageNo,pageSize),wrapper);
        // 4.数据
        List<Schedule> records = p.getRecords();
        List<ScheduleVO> ls=new ArrayList<>();
        records.forEach(i->{
            Doctor doc = doctorClient.getById(i.getDoctorId()).getData();
            Organization org = doctorClient.getById1(i.getOrgId()).getData();
            ScheduleVO scheduleVO=new ScheduleVO();
            BeanUtils.copyProperties(i,scheduleVO);
            scheduleVO.setDocName(doc.getName());
            scheduleVO.setOrgName(org.getName());
            ls.add(scheduleVO);
        });
        return Result.success(new PageResult(p.getTotal(),ls));
    }
}
