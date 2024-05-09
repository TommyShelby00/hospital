package com.hp.schedule.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Resource {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Integer remind;
}
