package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.UserBaseInfoMapper;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import com.jc.lost_and_found.service.UserBaseInfoService;
import com.jc.lost_and_found.utils.MyStringUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Service
public class UserBaseInfoServiceImpl  extends ServiceImpl<UserBaseInfoMapper, UserBaseInfoDO> implements UserBaseInfoService {
    @Override
    public UserBaseInfoDO findByLoginName(String loginName) {
        return this.getOne(new QueryWrapper<UserBaseInfoDO>().eq("login_name", loginName));
    }

    @Override
    public boolean isLoginNameExist(String loginName) {
        return this.findByLoginName(loginName) != null;
    }

    @Override
    public void register(UserBaseInfoDO target) {
        target.setLoginPassword(MyStringUtil.match(target.getLoginPassword()));
        target.setRegisterTime(target.getRegisterTime()/1000);
        this.save(target);
    }
}
