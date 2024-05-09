package com.hp.schedule.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CategoryDTO {
    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
