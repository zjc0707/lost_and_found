package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.lost_and_found.model.RoleInfoDO;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface RoleInfoMapper extends BaseMapper<RoleInfoDO> {
    /**
     * 获取用户角色权限
     * @param userId
     * @return
     */
    @Select("SELECT name FROM user_role " +
            "INNER JOIN role_info ON user_role.role_id = role_info.id " +
            "where user_id = #{userId}")
    Set<String> getRoleNameByUserId(Long userId);
}
