package com.hp.user.service.impl;

import com.hp.user.domain.po.Message;
import com.hp.user.mapper.MessageMapper;
import com.hp.user.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-06
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
