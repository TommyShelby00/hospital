package com.hp.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.api.client.DoctorClient;
import com.hp.api.vo.Doctor;
import com.hp.common.domain.Result;
import com.hp.user.domain.dto.RoomDTO;
import com.hp.user.domain.po.ChatRoom;
import com.hp.user.domain.po.Message;
import com.hp.user.domain.po.User;
import com.hp.user.domain.vo.ChatVO;
import com.hp.user.service.IChatRoomService;
import com.hp.user.service.IMessageService;
import com.hp.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@SaCheckLogin
@Slf4j
public class ChatController {
    @Autowired
    private IChatRoomService iChatRoomService;

    private final DoctorClient doctorClient;
    @Autowired
    private IMessageService iMessageService;

    @Autowired
    private IUserService iUserService;

    @PostMapping("/enterRoom")
    public Result<ChatVO> createRoom(@RequestBody RoomDTO roomDTO) {
        log.info("创建聊天室,{}", roomDTO);
        //判断是否存在聊天室
        QueryWrapper<ChatRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", roomDTO.getUserId());
        wrapper.eq("doctor_id", roomDTO.getDoctorId());
        ChatVO chatVO = new ChatVO();
        if (iChatRoomService.count(wrapper) > 0) {
            //存在聊天室
            //查找聊天室信息
            ChatRoom one = iChatRoomService.getOne(wrapper);
            QueryWrapper<Message> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("room_id", one.getId());
            List<Message> list=iMessageService.list(wrapper1);
            BeanUtils.copyProperties(one, chatVO);
            chatVO.setMessages(list);
            //清空未读消息
            return Result.success(chatVO);
        } else {
            //创建聊天室
            ChatRoom chatRoom = new ChatRoom();
            BeanUtils.copyProperties(roomDTO, chatRoom);
            iChatRoomService.save(chatRoom);
            chatVO.setId(chatRoom.getId());
            Doctor doctor = doctorClient.getById(chatRoom.getDoctorId()).getData();
            chatVO.setDoctor(doctor);
            User user = iUserService.getById(chatRoom.getUserId());
            chatVO.setUser(user);
            return Result.success(chatVO);
        }
    }

    @GetMapping("/queryUserList")
    private Result<List<ChatVO>> queryUserList(String id){
        log.info("查询用户聊天列表:{}",id);
        QueryWrapper<ChatRoom> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",id);
        List<ChatRoom> list = iChatRoomService.list(wrapper);
        List<ChatVO> ls=new ArrayList<>();
        list.forEach(item->{
            ChatVO chatVO=new ChatVO();
            chatVO.setId(item.getId());
            chatVO.setUserUnread(item.getUserUnread());
            Doctor doctor = doctorClient.getById(item.getDoctorId()).getData();
            chatVO.setDoctor(doctor);
            User user = iUserService.getById(item.getUserId());
            chatVO.setUser(user);
            ls.add(chatVO);
        });
        return Result.success(ls);
    }

    @GetMapping("/queryDoctorList")
    private Result<List<ChatVO>> queryDoctorList(Integer id){
        QueryWrapper<ChatRoom> wrapper=new QueryWrapper<>();
        wrapper.eq("doctor_id",id);
        List<ChatRoom> list = iChatRoomService.list(wrapper);
        log.info("查询医生聊天列表:{}",list);
        List<ChatVO> ls=new ArrayList<>();
        list.forEach(item->{
            ChatVO chatVO=new ChatVO();
            chatVO.setId(item.getId());
            chatVO.setDocUnread(item.getDocUnread());
            Doctor doctor = doctorClient.getById(item.getDoctorId()).getData();
            chatVO.setDoctor(doctor);
            User user = iUserService.getById(item.getUserId());
            chatVO.setUser(user);
            ls.add(chatVO);
        });
        return Result.success(ls);
    }

    @PutMapping("/delUser")
    private Result delUserUnread(Integer id){
        log.info("清除用户未读消息数,{}",id);
        UpdateWrapper<ChatRoom> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",id);
        wrapper.set("user_unread",0);
        iChatRoomService.update(wrapper);
        return Result.success();
    }

    @PutMapping("/delDoctor")
    private Result delDocUnread(Integer id){
        log.info("清除医生未读消息数,{}",id);
        UpdateWrapper<ChatRoom> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",id);
        wrapper.set("doc_unread",0);
        iChatRoomService.update(wrapper);
        return Result.success();
    }

}
