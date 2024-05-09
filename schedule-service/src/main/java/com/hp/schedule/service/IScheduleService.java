package com.hp.schedule.service;

import com.hp.schedule.domain.po.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hp.schedule.domain.vo.DateVO;
import com.hp.schedule.domain.vo.DocDetailVO;
import com.hp.schedule.domain.vo.DoctorVO;
import com.hp.schedule.domain.vo.OrgRemindVO;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-02-14
 */
public interface IScheduleService extends IService<Schedule> {


    List<DoctorVO> category(Integer id, LocalDate date);

    DocDetailVO detail(Integer id);

    List<DateVO> dateDetail(Integer id, LocalDate date);

    List<OrgRemindVO> queryOrgRemind(LocalDate date);

    List<DoctorVO> queryDocRemind(Integer id,LocalDate date);
}
