package org.fage.springbootshiro.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.fage.springbootshiro.bean.entity.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/12 11:16
 * @description
 **/
@Configuration
@Slf4j
public class PasswordHelper {

    //算法
    private static final String ALGO_MD5 = "md5";
    private static final String ALGO_SHA256 = "sha256";
    //散列次数
    private static final int HASH_TIMES = 256;
    //平台自己的公盐
    public static final String PUB_SALT = "gtrgrt*hZhn-ty~";


    /**
     * Shiro 密码匹配器配置
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(){
        RetryLimitHashedCredentialsMatcher matcher =  new RetryLimitHashedCredentialsMatcher();
        //使用md5算法
        matcher.setHashAlgorithmName(ALGO_MD5);
        //散列次数
        matcher.setHashIterations(HASH_TIMES);
        //是否存储散列后的密码为16进制
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

    /**
     * 密码加密工具(使用md5算法)
     *
     * @param password 等待加密的密码
     * @param userSalt 传入的用户私盐
     * @return
     */
    public static String encrypt(String password, String userSalt){
        String encryptPassword = "";
        //两者都不为空才帮加密
        if (!StringUtils.isEmpty(password) && !StringUtils.isEmpty(userSalt)) {
            //盐=平台公盐 + 用户私盐
            SimpleHash passwordHash = new SimpleHash(ALGO_MD5, password, PUB_SALT + userSalt, HASH_TIMES);
            //转成16进制字符串
            encryptPassword = passwordHash.toHex();
        }else {
            log.error("传入的密码或用户私盐为空 password={},userSalt={}", password, userSalt);
        }
        return encryptPassword;
    }

    /**
     * 密码加密工具（使用MD5算法）
     * @param userEntity 被加密的用户对象（该操作直接会改变userEntity对象的password属性以及salt属性）
     */
    public static void encrypt(UserEntity userEntity){
        String password = userEntity.getPassword();
        String userSalt = generateSalt();

        if (!StringUtils.isEmpty(password) && !StringUtils.isEmpty(userSalt)) {
            //盐=平台公盐 + 用户私盐
            SimpleHash passwordHash = new SimpleHash(ALGO_MD5, password, PUB_SALT + userSalt, HASH_TIMES);
            //转成16进制字符串
            String encryptPassword = passwordHash.toHex();
            userEntity.setPassword(encryptPassword);
            userEntity.setSalt(userSalt);
        }
    }

    /**
     * 生成用户私盐工具
     * @return
     */
    public static String generateSalt(){
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }


    /**
     * 自定义Shiro密码匹配器
     */
    private class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{
        RetryLimitHashedCredentialsMatcher(){
            super();
        }

        @Override
        public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
            //此处后期可以自己加入登录密码次数限制
            //TODO
            return super.doCredentialsMatch(token, info);
        }
    }
}
