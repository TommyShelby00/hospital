package com.hp.user.domain.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String id;
    private String username;
    private String avatar;
    private String token;
}
