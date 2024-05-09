package com.hp.schedule.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("schedule")
@ApiModel(value="Schedule对象", description="")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "排班日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ApiModelProperty(value = "开始时间")

    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @ApiModelProperty(value = "科室id")
    private Integer orgId;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "号剩余数量")
    private Integer remind;

    @ApiModelProperty(value = "费用")
    private Integer fee;

    private Integer status;
}
