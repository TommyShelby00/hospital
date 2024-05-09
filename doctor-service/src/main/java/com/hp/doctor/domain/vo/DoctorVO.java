package com.hp.doctor.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorVO {
    private Integer id;

    private Integer orgId;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate comeTime;

    private String specialty;

    private String position;

    private String img;

    private Integer status;

    private String orgName;
}
