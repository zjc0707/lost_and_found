package com.jc.lost_and_found.global;

import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zjc
 * @date 2019/10/8
 * 捕捉shiro跑出的权限异常
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class ShiroExceptionHandler {

    @ExceptionHandler(value = {AuthorizationException.class, UnauthorizedException.class})
    public ResponseData exceptionHandler(Exception e){
        log.info("权限不足：{}",e.getMessage());
        return ResponseDataUtil.buildSend(ResultEnums.LOGIN_USER_UNAUTHORIZED);
    }
}
