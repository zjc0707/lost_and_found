package com.jc.lost_and_found.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import com.jc.lost_and_found.model.LoginVO;
import com.jc.lost_and_found.model.RegisterVO;
import com.jc.lost_and_found.model.RoleInfoDO;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import com.jc.lost_and_found.service.UserBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 系统登陆控制器
 * @author  sample
 * @date    2019/06/30
 */
@Slf4j
@RestController
@RequestMapping("/register")
public class LoginController {

    private static final Integer CAPTCHA_INVALID_TIME_GAP = 90 * 1000;
    private static final Integer TIMESTAMP_INVALID_TIME_GAP = 60 * 1000;
    private static final Integer ACCOUNT_MIN_LENGTH = 6;
    private static final Integer ACCOUNT_MAX_LENGTH = 11;
    private static final Integer PASSWORD_MIN_LENGTH = 6;
    private static final Integer PASSWORD_MAX_LENGTH = 11;
    @Autowired
    private Producer captchaProducer;

    @Autowired
    private UserBaseInfoService userBaseInfoService;
    /**
     * 获取验证码图片
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        // 生成验证码字符串
        String capText = captchaProducer.createText();
        // get the currently executing user:
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        log.info(session.getHost() +"_"+session.getId());
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        session.setAttribute("captchaTime", System.currentTimeMillis());
        // 生成验证码图片
        BufferedImage captchaImage = captchaProducer.createImage(capText);
        ServletOutputStream captchaImageOutputStream = response.getOutputStream();
        ImageIO.write(captchaImage, "jpg", captchaImageOutputStream);
        try {
            captchaImageOutputStream.flush();
        } finally {
            captchaImageOutputStream.close();
        }
        log.debug("save captcha time: " + session.getAttribute("captchaTime") + ",captchaCode = " + capText);
    }

    @PostMapping(value = "/register")
    public ResponseData register(@ModelAttribute RegisterVO registerVO){
        if(StringUtils.isEmpty(registerVO.getLoginName()) || StringUtils.isEmpty(registerVO.getLoginPassword()) || StringUtils.isEmpty(registerVO.getCaptchaCode()) || (null == registerVO.getTimeStamp()))
        {
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        //三选一
        if(StringUtils.isEmpty(registerVO.getQq()) && StringUtils.isEmpty(registerVO.getWechat()) && StringUtils.isEmpty(registerVO.getTelephone())){
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_NON_CONFORMITY);
        }
        //账号密码长度限制
        if(registerVO.getLoginName().length() < ACCOUNT_MIN_LENGTH
                || registerVO.getLoginName().length() > ACCOUNT_MAX_LENGTH
                || registerVO.getLoginPassword().length() > PASSWORD_MAX_LENGTH
                || registerVO.getLoginPassword().length() < PASSWORD_MIN_LENGTH){
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_NON_CONFORMITY);
        }

        if((System.currentTimeMillis() < registerVO.getTimeStamp()) || ((System.currentTimeMillis() - registerVO.getTimeStamp()) >= TIMESTAMP_INVALID_TIME_GAP))
        {
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        String captchaCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if(captchaCode == null){
            return ResponseDataUtil.buildSend(ResultEnums.VERIFY_CODE_EMPTY);
        }
        if(!registerVO.getCaptchaCode().equals(captchaCode)){
            return ResponseDataUtil.buildSend(ResultEnums.VERIFY_CODE_ERROR);
        }
        Long captchaTime = (Long) session.getAttribute("captchaTime");
        if(null == captchaTime || (System.currentTimeMillis() - captchaTime) >= CAPTCHA_INVALID_TIME_GAP) {
            log.error("null == captchaTime or outTime");
            return ResponseDataUtil.buildSend(ResultEnums.VERIFY_CODE_OUTTIME_ERROR);
        }
        log.debug("captchaTime gap: " + System.currentTimeMillis() + "," + (System.currentTimeMillis() - captchaTime) + ",captchaCode: " + captchaCode);
        if(userBaseInfoService.isLoginNameExist(registerVO.getLoginName())){
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_ACCOUNT_EXIST);
        }
        try {
            userBaseInfoService.register(new UserBaseInfoDO(registerVO));
            UsernamePasswordToken token = new UsernamePasswordToken(registerVO.getLoginName(), registerVO.getLoginPassword(), registerVO.getTimeStamp().toString());
            currentUser.login(token);
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_SUCCESS, currentUser.getPrincipal());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_ERROR);
        }
    }

    @PostMapping(value = "/login")
    public ResponseData login(@ModelAttribute LoginVO loginVO) {
        if ((null == loginVO.getLoginName()) || (null == loginVO.getLoginPassword()) || (null == loginVO.getCaptchaCode()) || (null == loginVO.getTimeStamp())) {
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }

        String resultData = "loginName: " + loginVO.getLoginName() + ",password：" + loginVO.getLoginPassword()
                + ",captchaCode: " + loginVO.getCaptchaCode() + ",timeStamp: " + loginVO.getTimeStamp() + ",[" + System.currentTimeMillis() + "]";
        log.debug(resultData);

        if ((System.currentTimeMillis() < loginVO.getTimeStamp()) || ((System.currentTimeMillis() - loginVO.getTimeStamp()) >= TIMESTAMP_INVALID_TIME_GAP)) {
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        ResultEnums loginResultEnum = ResultEnums.VERIFY_CODE_ERROR;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        log.info(session.getHost() + "_" + session.getId());
        String captchaCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (captchaCode == null) {
            loginResultEnum = ResultEnums.VERIFY_CODE_EMPTY;
        } else if (loginVO.getCaptchaCode().equals(captchaCode)) {
            Long captchaTime = (Long) session.getAttribute("captchaTime");
            if (null == captchaTime) {
                log.error("null == captchaTime");
            } else {
                log.debug("captchaTime gap: " + System.currentTimeMillis() + "," + (System.currentTimeMillis() - captchaTime) + ",captchaCode: " + captchaCode);
            }
            if ((null != captchaTime) && ((System.currentTimeMillis() - captchaTime) <= CAPTCHA_INVALID_TIME_GAP)) {
                try {
                    if (!currentUser.isAuthenticated()) {
                        //借用Host字段用于传递timeStamp参数
                        UsernamePasswordToken token = new UsernamePasswordToken(loginVO.getLoginName(), loginVO.getLoginPassword(), loginVO.getTimeStamp().toString());
                        //token.setRememberMe(true);
                        currentUser.login(token);
                    }
                    loginResultEnum = ResultEnums.LOGIN_SUCCESS;
                    return ResponseDataUtil.buildSend(loginResultEnum, currentUser.getPrincipal());
                } catch (IncorrectCredentialsException e) {
                    loginResultEnum = ResultEnums.LOGIN_PASSWORD_ERROR;
                } catch (LockedAccountException e) {
                    loginResultEnum = ResultEnums.LOGIN_ACCOUNT_LOCKED_ERROR;
                } catch (UnknownAccountException e) {
                    loginResultEnum = ResultEnums.LOGIN_NO_ACCOUNT_ERROR;
                } catch (AuthenticationException e) {
                    loginResultEnum = ResultEnums.LOGIN_ERROR;
                    log.error(e.getMessage());
                }
            } else {
                loginResultEnum = ResultEnums.VERIFY_CODE_OUTTIME_ERROR;
            }
        }

        return ResponseDataUtil.buildSend(loginResultEnum);
    }

    @GetMapping(value = "/logout")
    public ResponseData logout(){
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return ResponseDataUtil.buildSuccess();
    }

    @GetMapping(value = "/getCurrentUser")
    public ResponseData getCurrentUser(){
        Object obj = SecurityUtils.getSubject().getPrincipal();
        if(obj == null){
            return ResponseDataUtil.buildSend(ResultEnums.CURRENT_USER_NOT_EXIST);
        }else{
            return ResponseDataUtil.buildSend(ResultEnums.CURRENT_USER_EXIST,obj);
        }
    }
    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return LOGIN_USER_UNAUTHORIZED
     */
    @GetMapping(value = "/unauthorized")
    public ResponseData unauthorized() {
        return ResponseDataUtil.buildSend(ResultEnums.LOGIN_USER_UNAUTHORIZED);
    }

}
