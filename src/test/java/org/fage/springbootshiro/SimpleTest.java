package org.fage.springbootshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/3 15:13
 * @description
 **/
public class SimpleTest {
    SimpleAccountRealm accountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        //初始化一个realm
        accountRealm.addAccount("fage", "123","admin");
    }

    @Test
    public void testAuthHelloworld(){
        //创建securityManagr以及Realm
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(accountRealm);

        //让SecurityUtils来管理SecurityManager
        SecurityUtils.setSecurityManager(securityManager);

        //获取当前用户
        Subject subject = SecurityUtils.getSubject();

        //模拟用户输入账号密码登录
        UsernamePasswordToken token = new UsernamePasswordToken("fage", "123");
        subject.login(token);
        //验证用户是否有admin这个角色
        subject.checkRole("admin");
        //判断用户是否登录成功
        System.out.println("是否登录成功：" + subject.isAuthenticated());

        //注销
        subject.logout();

        //查看是否注销
        System.out.println("是否已经登录：" + subject.isAuthenticated());

    }

    @Test
    public void testEncrypt(){
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashAlgorithmName("SHA-256");    //sha256算法
        hashService.setPrivateSalt(new SimpleByteSource("123"));    //私盐
//        hashService.setGeneratePublicSalt(true);    //是否生成公盐
//        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator());    //用于生成默认公盐
        hashService.setHashIterations(1); //生成hash的迭代次数

        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes("helloworld"))
                .setSalt("123#sdf")
                .setIterations(256)
                .build();

        String hex = hashService.computeHash(request).toHex();
        System.out.println(hex);
    }
}
