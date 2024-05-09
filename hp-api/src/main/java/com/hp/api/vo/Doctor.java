package com.hp.api.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Doctor对象", description="")
@NoArgsConstructor
@AllArgsConstructor
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "密码")
    private String password;


}
