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
@AllArgsConstructor
@TableName("user_role")
@EqualsAndHashCode(callSuper = false)
public class UserRoleDO extends BaseEntity<UserRoleDO>{

    private Long userId;
    private Long roleId;
}
