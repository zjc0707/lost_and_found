package com.jc.lost_and_found.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.lost_and_found.mapper.UserRoleMapper;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.UserRoleDO;
import com.jc.lost_and_found.service.UserRoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * @author zjc
 * @date 2019/10/28
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {

    @CacheEvict(value = {"userPage","messagePage","messagePageDetail","authorizationCache","authenticationCache"}, allEntries = true)
    @Override
    public void editRole(BaseSearchParam baseSearchParam) {
        UserRoleDO userRoleDO = new UserRoleDO(baseSearchParam.getUserId(), baseSearchParam.getRoleId());
        if(userRoleDO.getRoleId() == null){
//            super.remove(Wrappers.query(userRoleDO));
            //用伪删除不方便查找，改为直接delete
            super.baseMapper.deleteByUserId(userRoleDO.getUserId());
        }else{
            super.save(userRoleDO);
        }
    }
}
