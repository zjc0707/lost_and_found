package com.jc.lost_and_found.model;

import lombok.Data;

/**
 * @author zjc
 * @date 2019/9/28
 */
@Data
public class RegisterVO{
    private String  loginName;
    private String  loginPassword;
    private String  captchaCode;
    private Long timeStamp;
    private String userName;
    private String qq;
    private String wechat;
    private String telephone;
}
