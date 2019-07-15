package org.fage.springbootshiro.config.security;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.fage.springbootshiro.bean.entity.ItemEntity;
import org.fage.springbootshiro.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 14:57
 * @description
 **/
@Configuration
@RestController
@Slf4j
public class ShiroConfig {

    @Autowired
    private ItemRepository itemRepository;

    /**
     * securityManager组装
     * @return
     */
    @Bean
    public SecurityManager securityManager(ModularRealmAuthenticator authenticator,ModularRealmAuthorizer authorizer, Realm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(authenticator);
        securityManager.setAuthorizer(authorizer);
        securityManager.setRealm(customRealm);
        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //拦截器（配置有顺序，所以是LinkedHashMap）
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        List<ItemEntity> itemEntities = itemRepository.findAll();
        //配置字符串拦截器
        Map<String, String> map = Maps.newLinkedHashMap();
        for(ItemEntity item : itemEntities){
            map.put(item.getUrl(), item.getFilter());
        }


        //配置不会被拦截的链接
        /*filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/user/doLogin", "anon");
        filterChainDefinitionMap.put("/user/reg", "anon");
        //swaggerUI过滤
        filterChainDefinitionMap.put("/swagger-ui.html/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");

        //配置退出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/**", "authc");*/


        //设置登录url
        shiroFilterFactoryBean.setLoginUrl("/login");
        //登录成功后的主页
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 设置Authenticator与AuthenticationStrategy
     * @return
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        //设置登录认证器
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //多Realm时的策略设置
        FirstSuccessfulStrategy strategy = new FirstSuccessfulStrategy();
        authenticator.setAuthenticationStrategy(strategy);
        return authenticator;
    }

    /**
     * 权限授权器
     * @return
     */
    @Bean
    public ModularRealmAuthorizer modularRealmAuthorizer(){
        //设置权限认证器
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        //设置权限以字符串进行解析
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        return authorizer;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
   /* @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }*/


    /**
     * 对密码MD5的逻辑
     * @return
     */
   /* @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1024); // 散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }*/


    /**
     * 重新加载所有组件
     * @return
     */
    @RequestMapping("/flush")
    public String flush(){
        return "success";
    }
}
