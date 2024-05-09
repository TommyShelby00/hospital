package com.hp.order.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order1")
@ApiModel(value="Order对象", description="")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "科室id")
    private Integer orgId;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "科室名称")
    private String orgName;

    @ApiModelProperty(value = "医生姓名")
    private String doctorName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "预约日期")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @ApiModelProperty(value = "开始时间")
    private LocalTime beginTime;

    @JsonFormat(pattern = "HH:mm")
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "就诊人姓名")
    private String name;

    @ApiModelProperty(value = "就诊人联系电话")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String identity;

    @ApiModelProperty(value = "支付费用")
    private Integer fee;

    @ApiModelProperty(value = "订单状态,0:未支付 ,1:已支付,2:已取消")
    private Integer status;

    @ApiModelProperty(value = "号源id")
    private Integer schId;
}
