package com.jc.lost_and_found.mapper.provider;

import com.jc.lost_and_found.model.BaseSearchParam;

/**
 * @author zjc
 * @date 2019/10/28
 */
public class UserBaseInfoProvider {

    public String pageByRoleId(BaseSearchParam baseSearchParam){
        String sql = "SELECT u.id, u.user_name, u.login_name, u.register_time, u.login_time, u.qq, u.wechat, u.telephone " +
                "FROM user_base_info u " +
                "LEFT JOIN user_role ur ON ur.user_id = u.id " +
                "WHERE u.delete_flag = 0 " +
                "AND ur.role_id ";
        sql = baseSearchParam.getRoleId() == null ? sql.concat("is NULL ") : sql.concat("= #{baseSearchParam.roleId} ");
        return sql.concat("ORDER BY u.login_time DESC");
    }
}
