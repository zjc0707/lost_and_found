package com.jc.lost_and_found.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author zjc
 * @date 2019/10/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShiroUserVO extends UserBaseInfoDO {

    public ShiroUserVO(UserBaseInfoDO u){
        this.setId(u.getId());
        this.setLoginPassword(u.getLoginPassword());
        this.setLoginName(u.getLoginName());
        this.setUserName(u.getUserName());
        this.setQq(u.getQq());
        this.setWechat(u.getWechat());
        this.setTelephone(u.getTelephone());
        this.setRegisterTime(u.getRegisterTime());
    }
    private Set<String> roles;
    private Set<String> permissions;
}
