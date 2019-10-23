package com.jc.lost_and_found.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jc.lost_and_found.model.ControllerUrlDO;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author zjc
 * @date 2019/7/7 15:26
 */
public interface ControllerUrlMapper extends BaseMapper<ControllerUrlDO> {

    /**
     * 获取用户URL资源权限
     * @param userId
     * @return
     */
    @Select("SELECT url FROM role_controller_url " +
            "INNER JOIN controller_url ON role_controller_url.controller_url_id = controller_url.id " +
            "where role_id in " +
            "   (SELECT role_id FROM user_role " +
            "   LEFT JOIN role_info ON user_role.role_id = role_info.id " +
            "   where user_id = #{userId}" +
            "   )")
    Set<String> getUrlNameByUserId(Long userId);

    @Select("SELECT url FROM controller_url")
    Set<String> getAllUrl();
}
