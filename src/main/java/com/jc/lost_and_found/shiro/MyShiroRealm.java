package com.jc.lost_and_found.shiro;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jc.lost_and_found.mapper.ControllerUrlMapper;
import com.jc.lost_and_found.mapper.RoleInfoMapper;
import com.jc.lost_and_found.model.ControllerUrlDO;
import com.jc.lost_and_found.model.ShiroUserVO;
import com.jc.lost_and_found.model.UserBaseInfoDO;
import com.jc.lost_and_found.service.UserBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 验证实体类
 * @author hx
 * @date 2019/7/22 19:42
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserBaseInfoService userBaseInfoService;

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Resource
    private ControllerUrlMapper controllerUrlMapper;
    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String loginName = token.getUsername();
        UserBaseInfoDO userBaseInfoDO = userBaseInfoService.findByLoginName(loginName);
        if (null == userBaseInfoDO) {
            throw new UnknownAccountException();
        }
        ShiroUserVO userVO = new ShiroUserVO(userBaseInfoDO);
        userVO.setRoles(roleInfoMapper.getRoleNameByUserId(userVO.getId()));
        if(!userVO.getRoles().isEmpty()){
            boolean isSuper = false;
            for(String str : userVO.getRoles()){
                if (str.equals("超级管理员")) {
                    isSuper = true;
                    break;
                }
            }
            Set<String> permissionSet;
            if(isSuper){
                permissionSet = controllerUrlMapper.getAllUrl();
            }else{
                permissionSet = controllerUrlMapper.getUrlNameByUserId(userVO.getId());
            }
            userVO.setPermissions(permissionSet);
        }

        return new SimpleAuthenticationInfo(userVO, userVO.getLoginPassword(), getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUserVO userVO = (ShiroUserVO) SecurityUtils.getSubject().getPrincipal();
        log.debug(userVO.toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo =  new SimpleAuthorizationInfo();
        //根据用户ID查询角色（role），放入到Authorization里
        Set<String> roleSet = userVO.getRoles();
        if(!roleSet.isEmpty()) {
            simpleAuthorizationInfo.setRoles(roleSet);
            //根据用户角色ID查询权限（permission），放入到Authorization里
            Set<String> permissionSet = userVO.getPermissions();
            if(!permissionSet.isEmpty()) {
                simpleAuthorizationInfo.setStringPermissions(permissionSet);
            }
            log.info(permissionSet.isEmpty() + "," + permissionSet.toString());
        }
        log.info(roleSet.isEmpty() + "," + roleSet.toString());
        return simpleAuthorizationInfo;
    }

    /**
     * 清除当前用户的授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        ShiroUserVO shiroUserVO = (ShiroUserVO) principals.getPrimaryPrincipal();
        return shiroUserVO.getLoginName();
    }

    /**
     * 清除当前用户的认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }
    /**
     * 清除当前用户的认证及授权缓存
     * @param principals
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
//        super.clearCache(principals);
        this.clearCachedAuthenticationInfo(principals);
        this.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 自定义方法：清除所有授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的认证缓存和授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}

