package com.hp.doctor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.common.domain.PageResult;
import com.hp.doctor.domain.po.Organization;
import com.hp.doctor.mapper.OrganizationMapper;
import com.hp.doctor.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
@Service
@Slf4j
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public PageResult getPage(int pageNo, int pageSize) {
        ListOperations listOperations = redisTemplate.opsForList();
        Long size = 0L;
        if(redisTemplate.hasKey("organization")) {
            size = listOperations.size("organization");
        }
        //判断是否有缓存
        if (size!=0&&size!=null){
            //缓存命中
            int start=(pageNo-1)*pageSize;
            List<Organization> ls= listOperations.range("organization", start, start + pageSize - 1);
            log.info("查询到缓存科室数据:{}",ls);
            return new PageResult(size,ls);
        }
        else{
            //缓存未命中
            QueryWrapper<Organization> wrapper=new QueryWrapper<>();
            wrapper.orderByAsc("name");
            Page<Organization> p=new Page<>(pageNo,pageSize);
            organizationMapper.selectPage(p,wrapper);
            List<Organization> list = organizationMapper.selectList(wrapper);
            listOperations.leftPushAll("organization",list);
            log.info("查询到科室数据:{}",p.getRecords());
            return new PageResult(p.getTotal(),p.getRecords());
        }
    }
}
