package com.hp.user.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomDTO implements Serializable {
    private String userId;
    private Integer doctorId;
}
