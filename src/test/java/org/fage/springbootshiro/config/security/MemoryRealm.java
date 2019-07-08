package org.fage.springbootshiro.config.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.fage.springbootshiro.bean.dto.UserLoginInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/3 16:26
 * @description
 **/
@Configuration
public class MemoryRealm extends AuthorizingRealm {

    private List<UserLoginInfo> dbUserList;

    {
        super.setName("memoryRealm");
        dbUserList = new ArrayList<>();
        /*dbUserList.add(new UserLoginInfo("fage", "123"));
        dbUserList.add(new UserLoginInfo("doudou", "111"));
        dbUserList.add(new UserLoginInfo("mama", "888"));*/
    }

    /**
     * 获取用户的角色信息并授权（权限认证）
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = ((UserLoginInfo) principals.getPrimaryPrincipal()).getUsername();
        //模拟从数据库获取角色和权限
        Set<String> roles = getRolesByUserName(username);
        Set<String> permissions = getPermissionsByUserName(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 模拟获取到菜单
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUserName(String username) {
        Set<String> set = new HashSet<>();
        set.add("user/query/list");
        set.add("user/info/update");
        return set;
    }

    /**
     * 模拟获取到角色
     * @param username
     * @return
     */
    private Set<String> getRolesByUserName(String username) {
        Set<String> set = new HashSet<>();
        set.add("admin");
        set.add("vip");
        return set;
    }

    /**
     * 获取用户的登录信息并认证（登录验证）
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //从传过来的认证信息获取用户名以及密码
        String username  = ((String)token.getPrincipal());
        String password = token.getCredentials().getClass().getSimpleName();
        //获取用户名
        if(StringUtils.isEmpty(username)){
            return null;
        }
        UserLoginInfo loginInfo = dbUserList.stream()
                .filter(info -> info.getUsername().equals(username))
                .collect(Collectors.toList())
                .get(0);

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(loginInfo, loginInfo.getPassword(), "memoryRealm");
        return authenticationInfo;
    }

}
