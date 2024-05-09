package com.hp.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.api.client.DoctorClient;
import com.hp.api.vo.Doctor;
import com.hp.api.vo.OrganizationVO;
import com.hp.schedule.domain.po.Resource;
import com.hp.schedule.domain.po.Schedule;
import com.hp.schedule.domain.vo.DateVO;
import com.hp.schedule.domain.vo.DocDetailVO;
import com.hp.schedule.domain.vo.DoctorVO;
import com.hp.schedule.domain.vo.OrgRemindVO;
import com.hp.schedule.mapper.ScheduleMapper;
import com.hp.schedule.service.IScheduleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-02-14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {
    @Autowired
    ScheduleMapper scheduleMapper;
    private final DoctorClient doctorClient;

    /**
     * 根据科室id查看有号医生
     * @param id
     * @param date
     * @return
     */
    @Override
    public List<DoctorVO> category(Integer id, LocalDate date) {
        //搜寻当日有号医生
        List<DoctorVO> list=scheduleMapper.selectResources(id,date);
        //遍历集合，调用doctor-service搜寻医生详细信息
        list.forEach(doctorVO -> {
            Doctor doctor = doctorClient.getById(doctorVO.getId()).getData();
            BeanUtils.copyProperties(doctor,doctorVO);
        });
        return list;
    }

    /**
     * 查看医生号大致信息
     * @param id
     * @return
     */
    @Override
    public DocDetailVO detail(Integer id) {
        DocDetailVO docDetailVO=new DocDetailVO();
        Doctor doctor = doctorClient.getById(id).getData();
        BeanUtils.copyProperties(doctor,docDetailVO);
        LocalDate date=LocalDate.now();
        List<Resource> resources=scheduleMapper.detail(id,date);
        docDetailVO.setResources(resources);
        return docDetailVO;
    }

    /**
     * 查看医生指定日期号资源
     * @param id
     * @param date
     * @return
     */
    @Override
    public List<DateVO> dateDetail(Integer id, LocalDate date) {
        List<DateVO> list=scheduleMapper.dateDetail(id,date);
        return list;
    }

    @Override
    public List<OrgRemindVO> queryOrgRemind(LocalDate date) {
        List<OrganizationVO> ls = doctorClient.getAll().getData();
        System.out.println(111);
        List<OrgRemindVO> reminds = new ArrayList<>();
        for (OrganizationVO organization : ls) {
            OrgRemindVO remind = new OrgRemindVO();
            BeanUtils.copyProperties(organization, remind);
            remind.setRemind(scheduleMapper.queryOrgRemind(remind.getId(), date));
            reminds.add(remind);
        }
        return reminds;
    }

    @Override
    public List<DoctorVO> queryDocRemind(Integer id,LocalDate date) {
        List<Doctor> ls = doctorClient.getByOrgId(id).getData();
        List<DoctorVO> reminds = new ArrayList<>();
        for (Doctor doc : ls) {
            DoctorVO remind = new DoctorVO();
            BeanUtils.copyProperties(doc, remind);
            remind.setRemind(scheduleMapper.queryDocRemind(remind.getId(), date));
            reminds.add(remind);
        }
        return reminds;
    }



}
