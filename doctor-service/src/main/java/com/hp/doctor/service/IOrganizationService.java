package com.hp.doctor.service;

import com.hp.common.domain.PageResult;
import com.hp.doctor.domain.po.Organization;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-02-12
 */
public interface IOrganizationService extends IService<Organization> {

    PageResult getPage(int pageNo, int pageSize);
}
