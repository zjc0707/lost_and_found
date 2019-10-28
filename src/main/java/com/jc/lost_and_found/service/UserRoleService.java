package com.jc.lost_and_found.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.UserRoleDO;

/**
 * @author zjc
 * @date 2019/10/28
 */
public interface UserRoleService extends IService<UserRoleDO> {
    void editRole(BaseSearchParam baseSearchParam);
}
