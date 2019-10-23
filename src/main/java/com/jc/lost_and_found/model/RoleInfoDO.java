package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Data
@NoArgsConstructor
@TableName("role_info")
@EqualsAndHashCode(callSuper = false)
public class RoleInfoDO extends BaseEntity<RoleInfoDO> {

    public static final Integer LEVEL_NORMAL_USER = 0;
    public static final Integer LEVEL_MANAGER = 1;
    public static final Integer LEVEL_SUPER_MANAGER = 2;


    private String name;
    /**
     * 0-普通用户，1-管理员, 2-超级管理员
     */
    private Integer level;
}
