package com.jc.lost_and_found.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jc.lost_and_found.model.UserBaseInfoDO;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface UserBaseInfoService extends IService<UserBaseInfoDO> {
    UserBaseInfoDO findByLoginName(String loginName);
    boolean isLoginNameExist(String loginName);
    void register(UserBaseInfoDO target);
}
