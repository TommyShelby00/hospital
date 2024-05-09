package com.hp.api.vo;

import lombok.Data;

@Data
public class DoctorVO {
    private Integer id;
    private Integer remind;
    private String specialty;
    private String name;
    private String position;
}
