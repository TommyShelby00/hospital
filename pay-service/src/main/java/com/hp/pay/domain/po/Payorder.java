package com.hp.pay.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payorder")
@ApiModel(value="Payorder对象", description="")
public class Payorder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

    @ApiModelProperty(value = "病人id")
    private String userId;

    @ApiModelProperty(value = "科室id")
    private Integer orgId;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "支付,,2:支付宝，3.未支付")
    private Integer type;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "金额")
    private Float amount;

}
