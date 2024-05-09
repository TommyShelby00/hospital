package com.hp.schedule.mapper;

import com.hp.schedule.domain.po.Resource;
import com.hp.schedule.domain.po.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hp.schedule.domain.vo.DateVO;
import com.hp.schedule.domain.vo.DoctorVO;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-02-14
 */
public interface ScheduleMapper extends BaseMapper<Schedule> {

    @Select("select sum(remind) as remind ,doctor_id from schedule where org_id=#{org_id} and date=#{date} group by doctor_id")
    List<DoctorVO> selectResources(Integer org_id, LocalDate date);


    @Select("select sum(remind) as remind,date from schedule where doctor_id=#{doctorId} and date>=#{date} group by date")
    List<Resource> detail(Integer doctorId,LocalDate date);

    @Select("select * from schedule where doctor_id=#{doctorId} and date=#{date}")
    List<DateVO> dateDetail(Integer doctorId, LocalDate date);

    @Select("select sum(remind) as remind from schedule where org_id=#{id} and date=#{date}")
    Integer queryOrgRemind(Integer id, LocalDate date);

    @Select("select sum(remind) as remind from schedule where doctor_id=#{id} and date=#{date}")
    Integer queryDocRemind(Integer id, LocalDate date);
}
