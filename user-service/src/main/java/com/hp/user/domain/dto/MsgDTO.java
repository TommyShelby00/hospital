package com.hp.user.domain.dto;

import lombok.Data;

@Data
public class MsgDTO {
    private Integer roomId;
    private String text;
    private String from1;
}
