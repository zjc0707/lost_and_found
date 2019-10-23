package com.jc.lost_and_found.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zjc
 * @date 2019/9/27
 */
@Data
@NoArgsConstructor
@TableName("user_base_info")
@EqualsAndHashCode(callSuper = false)
public class UserBaseInfoDO extends BaseEntity<UserBaseInfoDO> {

    public UserBaseInfoDO(RegisterVO r){
        userName = r.getUserName();
        loginName = r.getLoginName();
        loginPassword = r.getLoginPassword();
        registerTime = r.getTimeStamp();
        qq = r.getQq();
        wechat = r.getWechat();
        telephone = r.getTelephone();
    }

    private String userName;
    private String loginName;
    private String loginPassword;
    private String qq;
    private String wechat;
    private String telephone;
    private Long registerTime;

    @JsonIgnore
    public String getLoginPassword() {return loginPassword;}
}
