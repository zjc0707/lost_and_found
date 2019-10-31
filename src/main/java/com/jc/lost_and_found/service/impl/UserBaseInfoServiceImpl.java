package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.UserBaseInfoMapper;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import com.jc.lost_and_found.service.UserBaseInfoService;
import com.jc.lost_and_found.utils.MyStringUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Service
public class UserBaseInfoServiceImpl  extends ServiceImpl<UserBaseInfoMapper, UserBaseInfoDO> implements UserBaseInfoService {

    @Cacheable(value = "userPage")
    @Override
    public IPage<UserBaseInfoDO> page(BaseSearchParam baseSearchParam) {
        Page<UserBaseInfoDO> page = new Page<>(baseSearchParam.getPageIndex(), baseSearchParam.getPageSize());
        return page.setRecords(super.baseMapper.pageByRoleId(baseSearchParam, page));
    }

    @Override
    public UserBaseInfoDO findByLoginName(String loginName) {
        return this.getOne(new QueryWrapper<UserBaseInfoDO>().eq("login_name", loginName));
    }

    @Override
    public boolean isLoginNameExist(String loginName) {
        return this.findByLoginName(loginName) != null;
    }

    @CacheEvict(value = "userPage", allEntries = true)
    @Override
    public void register(UserBaseInfoDO target) {
        target.setLoginPassword(MyStringUtil.match(target.getLoginPassword()));
        target.setRegisterTime(target.getRegisterTime()/1000);
        this.save(target);
    }

    @CacheEvict(value = "userPage", allEntries = true)
    @Override
    public void updateLoginTime(Long id, Long time) {
        UserBaseInfoDO target = new UserBaseInfoDO();
        target.setId(id);
        target.setLoginTime(time);
        super.updateById(target);
    }


}
