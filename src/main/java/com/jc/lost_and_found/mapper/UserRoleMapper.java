package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.lost_and_found.model.UserRoleDO;
import org.apache.ibatis.annotations.Delete;

/**
 * @author zjc
 * @date 2019/9/27
 */
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    @Delete("DELETE FROM user_role " +
            "WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
