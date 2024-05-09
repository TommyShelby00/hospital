package com.hp.user.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.user.domain.dto.MsgDTO;
import com.hp.user.domain.po.ChatRoom;
import com.hp.user.domain.po.Message;
import com.hp.user.service.IChatRoomService;
import com.hp.user.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IMessageService iMessageService;

    @Autowired
    private IChatRoomService iChatRoomService;

//        messagingTemplate.convertAndSendToUser(
//                String.valueOf(1),
//                "/queue/test",
//                "sb"
//        );

    @MessageMapping("/sendDoctorMessage")
    public void sendMessage(@RequestBody MsgDTO msgDTO) {
        log.info("收到医生发送消息:{}", msgDTO);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msgDTO.getRoomId()),
                "/queue/msg",
                msgDTO
        );
        //保存消息
        Message message=new Message();
        BeanUtils.copyProperties(msgDTO,message);
        iMessageService.save(message);
        //增加未读消息数
        UpdateWrapper<ChatRoom> wrapper=new UpdateWrapper<>();
        wrapper.setSql("user_unread=user_unread+1");
        wrapper.eq("id",msgDTO.getRoomId());
        iChatRoomService.update(wrapper);
    }

    @MessageMapping("/sendUserMessage")
    public void sendUserMessage(@RequestBody MsgDTO msgDTO) {
        log.info("收到用户发送消息:{}", msgDTO);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msgDTO.getRoomId()),
                "/queue/msg",
                msgDTO
        );
        //保存消息
        Message message=new Message();
        BeanUtils.copyProperties(msgDTO,message);
        iMessageService.save(message);
        //增加未读消息数
        UpdateWrapper<ChatRoom> wrapper=new UpdateWrapper<>();
        wrapper.setSql("doc_unread=doc_unread+1");
        wrapper.eq("id",msgDTO.getRoomId());
        iChatRoomService.update(wrapper);
    }
}
