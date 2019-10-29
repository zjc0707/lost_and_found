package com.jc.lost_and_found.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jc.lost_and_found.component.ResponseData;
import com.jc.lost_and_found.component.ResponseDataUtil;
import com.jc.lost_and_found.constant.ResultEnums;
import com.jc.lost_and_found.model.BaseSearchParam;
import com.jc.lost_and_found.model.MessageDO;
import com.jc.lost_and_found.model.ShiroUserVO;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import com.jc.lost_and_found.service.MessageService;
import com.jc.lost_and_found.service.UserBaseInfoService;
import com.jc.lost_and_found.shiro.MyShiroRealm;
import com.jc.lost_and_found.utils.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zjc
 * @date 2019/9/27
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserBaseInfoController extends AbstractCrudController<UserBaseInfoService, UserBaseInfoDO> {

    @Autowired
    private UserBaseInfoService userBaseInfoService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MyShiroRealm myShiroRealm;

    @PostMapping("page")
    @RequiresPermissions("/user/page")
    public ResponseData page(@ModelAttribute BaseSearchParam baseSearchParam){
        IPage page = userBaseInfoService.page(baseSearchParam);
        if(page.getRecords().isEmpty()){
            return ResponseDataUtil.buildSend(ResultEnums.RESULT_IS_NULL);
        }
        return ResponseDataUtil.buildSuccess(page);
    }

    @PostMapping("editPassword")
    public ResponseData editPassword(Long id, String oldPassword, String newPassword){
        if(StringUtils.isEmpty(newPassword)){
            return ResponseDataUtil.buildSend(ResultEnums.PARAM_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        ShiroUserVO obj = (ShiroUserVO) subject.getPrincipal();
        if(!obj.getId().equals(id)){
            log.error("非法账号修改请求:request:[" + id + "],login:[" + obj.getId() + "]");
            subject.logout();
            return ResponseDataUtil.buildIllegal();
        }
        if(!MyStringUtil.match(oldPassword).equals(obj.getLoginPassword())){
            return ResponseDataUtil.buildSend(ResultEnums.LOGIN_PASSWORD_ERROR);
        }
        try{
            UserBaseInfoDO userBaseInfoDO = new UserBaseInfoDO();
            userBaseInfoDO.setId(id);
            userBaseInfoDO.setLoginPassword(MyStringUtil.match(newPassword));
            userBaseInfoService.updateById(userBaseInfoDO);
            subject.logout();
            myShiroRealm.clearCachedAuthenticationInfo(subject.getPrincipals());
            UsernamePasswordToken token = new UsernamePasswordToken(obj.getLoginName(), newPassword, String.valueOf(System.currentTimeMillis()));
            subject.login(token);
            return ResponseDataUtil.buildSuccess();
        }catch (Exception e){
            log.error("editPassword fail:" + e.getMessage());
            return ResponseDataUtil.buildError();
        }
    }

    @PostMapping("editContact")
    public ResponseData editContact(Long id, String userName, String qq, String wechat, String telephone){
        boolean isEmptyQq = StringUtils.isEmpty(qq),
                isEmptyWechat = StringUtils.isEmpty(wechat),
                isEmptyTel = StringUtils.isEmpty(telephone),
                isEmptyUserName = StringUtils.isEmpty(userName);
        if(isEmptyQq && isEmptyWechat && isEmptyTel && isEmptyUserName){
            return ResponseDataUtil.buildSend(ResultEnums.REGISTER_NON_CONFORMITY);
        }
        Subject subject = SecurityUtils.getSubject();
        ShiroUserVO obj = (ShiroUserVO) subject.getPrincipal();
        if(!obj.getId().equals(id)){
            log.error("非法账号修改请求:request:[" + id + "],login:[" + obj.getId() + "]");
            subject.logout();
            return ResponseDataUtil.buildIllegal();
        }
        try{
            UserBaseInfoDO userBaseInfoDO = new UserBaseInfoDO();
            userBaseInfoDO.setId(id);
            userBaseInfoDO.setUserName(isEmptyUserName ? null : userName);
            userBaseInfoDO.setQq(isEmptyQq ? null : qq);
            userBaseInfoDO.setWechat(isEmptyWechat ? null : wechat);
            userBaseInfoDO.setTelephone(isEmptyTel ? null : telephone);
            userBaseInfoService.updateById(userBaseInfoDO);
            myShiroRealm.clearCachedAuthenticationInfo(subject.getPrincipals());
            return ResponseDataUtil.buildSuccess();
        }catch (Exception e){
            log.error("editContact fail:" + e.getMessage());
            return ResponseDataUtil.buildError();
        }
    }

    /**
     * 将自己发布的信息转为解决状态
     */
    @PostMapping("overMyMessage")
    public ResponseData overMyMessage(Long userId, Long messageId){
        Subject subject = SecurityUtils.getSubject();
        ShiroUserVO obj = (ShiroUserVO) subject.getPrincipal();
        if(!obj.getId().equals(userId)){
            log.error("非法账号修改请求:request:[" + userId + "],login:[" + obj.getId() + "]");
            subject.logout();
            return ResponseDataUtil.buildIllegal();
        }
        try{
            messageService.updateState(messageId, MessageDO.STATE_OVER);
        }catch (Exception e){
            log.error("deploy fail : {}", e.getMessage());
            return ResponseDataUtil.buildError();
        }
        return ResponseDataUtil.buildSuccess();
    }

}
