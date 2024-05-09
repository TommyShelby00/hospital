package com.hp.user.domain.vo;

import com.hp.api.vo.Doctor;
import com.hp.user.domain.po.Message;
import com.hp.user.domain.po.User;
import lombok.Data;

import java.util.List;

@Data
public class ChatVO {
    private Integer id;
    private List<Message> messages;
    private User user;
    private Doctor doctor;
    private Integer userUnread;
    private Integer docUnread;
}
