package com.jc.lost_and_found.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description 登陆界面传递的参数
 * @Author hx
 * @Date 2019/7/22 23:19
 */
@Getter
@Setter
@ToString
public class LoginVO {
    private String  loginName;
    private String  loginPassword;
    private String  captchaCode;
    private Long timeStamp;
}

