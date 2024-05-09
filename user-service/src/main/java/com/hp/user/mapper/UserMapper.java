package com.hp.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hp.user.domain.po.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-01-26
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> mySelect(String username,String phone,Integer age);
}
