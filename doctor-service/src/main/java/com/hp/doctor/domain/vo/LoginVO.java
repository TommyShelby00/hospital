package com.hp.doctor.domain.vo;

import lombok.Data;

@Data
public class LoginVO {
    private Integer id;
    private String token;
    private String name;
    private String img;
}
