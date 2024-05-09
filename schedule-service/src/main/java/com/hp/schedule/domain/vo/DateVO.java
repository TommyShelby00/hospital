package com.hp.schedule.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class DateVO implements Serializable {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Integer id;
    private Integer fee;
    private Integer remind;
}
