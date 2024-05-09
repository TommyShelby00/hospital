package com.hp.schedule.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hp.schedule.domain.po.Resource;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DocDetailVO {
    @ApiModelProperty(value = "医生id")
    private Integer id;

    @ApiModelProperty(value = "所属科室id")
    private Integer orgId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "入院时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate comeTime;

    @ApiModelProperty(value = "专长")
    private String specialty;

    @ApiModelProperty(value = "职称")
    private String position;

    @ApiModelProperty(value = "照片")
    private String img;

    @ApiModelProperty(value = "状态")
    private Integer status;

    private List<Resource> resources;
}
