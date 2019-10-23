package com.jc.lost_and_found.shiro;

import com.jc.lost_and_found.utils.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author zjc
 * @date 2019/10/8
 */
@Slf4j
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String inputPwd = new String(usernamePasswordToken.getPassword());
        String matchPwd = MyStringUtil.match(inputPwd);
        String dbPwd = info.getCredentials().toString();

        log.info("自定义密码比较器:input:db["+matchPwd+","+dbPwd+"]");
        return equals(matchPwd, dbPwd);
    }
}
