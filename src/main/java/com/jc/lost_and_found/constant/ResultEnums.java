package com.jc.lost_and_found.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 用于对后端返回前端的数据进行规范化封装
 * @author  sample
 * @date    2019/06/30
 */
public enum ResultEnums {

    //请求结果常数
    SUCCESS(0000, "业务请求成功！"),
    ERROR(1111, "业务请求失败！"),
    PARAM_ERROR(1112, "无效请求参数！"),
    ILLEGAL_ERROR(1113, "账号非法请求"),
    OUT_TIME(1114,"操作超时"),

    //登陆结果常数
    LOGIN_SUCCESS(2000, "登陆成功！"),
    LOGIN_ERROR(2001, "登陆失败！"),
    LOGIN_NO_ACCOUNT_ERROR(2101, "用户账号不存在!"),
    LOGIN_ACCOUNT_LOCKED_ERROR(2102, "用户账号禁用!"),
    LOGIN_PASSWORD_ERROR(2103, "密码错误!"),
    LOGIN_USER_UNAUTHORIZED(2201, "权限不足！"),

    //数据库查询结果常数
    RESULT_IS_NULL(3000, "数据库查询结果为空！"),
    DUPLICATE_DATA_INPUT(3005, "该数据已存在，不允许重复输入！"),

    //文件服务结果常数
    FILE_NO_FIND(4000, "文件没有找到！"),
    FILE_IS_UPLOAD(4001, "文件已经上传！"),

    //注册
    REGISTER_SUCCESS(5000,"注册成功"),
    REGISTER_ERROR(5001,"注册失败"),
    REGISTER_ACCOUNT_EXIST(5100,"账号已存在"),
    REGISTER_NON_CONFORMITY(5200,"账号不符合规范"),

    VERIFY_CODE_OUTTIME_ERROR(6001,"验证码已过期"),
    VERIFY_CODE_EMPTY(6002, "验证码session不存在！"),
    VERIFY_CODE_ERROR(6100, "验证码错误！"),

    CURRENT_USER_EXIST(7000,"已有登录信息"),
    CURRENT_USER_NOT_EXIST(7001,"未登录");

    @Getter @Setter private Integer code;
    @Getter @Setter private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
