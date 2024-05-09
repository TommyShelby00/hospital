package com.hp.user.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2024-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("patient_card")
@ApiModel(value="PatientCard对象", description="")
public class PatientCard implements Serializable {

    @TableId(value = "id",type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "就诊人姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证")
    private String identity;

    @ApiModelProperty(value = "二维码")
    private String qrCode;
}
