package com.hp.schedule.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleVO {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private Integer orgId;

    private Integer doctorId;

    private Integer remind;

    private Integer fee;

    private String orgName;

    private String docName;
}
