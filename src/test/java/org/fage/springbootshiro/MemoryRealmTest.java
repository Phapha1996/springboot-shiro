package org.fage.springbootshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.fage.springbootshiro.config.security.MemoryRealm;
import org.junit.Test;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/3 17:04
 * @description
 **/
public class MemoryRealmTest {

    @Test
    public void testAuth(){

        //自定义的realm
        Realm realm = new MemoryRealm();
        //初始化securityManager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);
       /* ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);*/
        SecurityUtils.setSecurityManager(securityManager);
        //登录
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken("fage", "123"));

        //权限校验
        subject.checkRoles("admin","vip");
        //判断用户是否登录成功
        System.out.println("是否登录成功：" + subject.isAuthenticated());
        System.out.println("登录用户的信息：" + subject.getPrincipal().toString());
        //注销
        subject.logout();

        //查看是否注销
        System.out.println("是否已经登录：" + subject.isAuthenticated());
    }
}
