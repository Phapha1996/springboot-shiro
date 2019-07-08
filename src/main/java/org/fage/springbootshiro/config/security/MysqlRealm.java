package org.fage.springbootshiro.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.fage.springbootshiro.bean.dto.UserLoginInfo;
import org.fage.springbootshiro.bean.entity.Role;
import org.fage.springbootshiro.bean.entity.User;
import org.fage.springbootshiro.repository.ItemRepository;
import org.fage.springbootshiro.repository.RoleRepository;
import org.fage.springbootshiro.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:58
 * @description
 **/
@Slf4j
public class MysqlRealm extends AuthorizingRealm {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 权限验证
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //能进入这里说明用户已经通过认证（登录）
        UserLoginInfo loginInfo = (UserLoginInfo) principals.getPrimaryPrincipal();
        //查找该用户的角色以及菜单
        List<Role> roles = roleRepository.findByUserDataId(loginInfo.getDataId());
        List<String> rolesName = roles.stream().map(ent -> ent.getName()).collect(Collectors.toList());
        List<String> itemsName = itemRepository.findByRoleDataIds(roles.stream().map(ent -> ent.getDataId()).collect(Collectors.toList()))
                .stream().map(ent -> ent.getName()).collect(Collectors.toList());

        //设置返回值
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(rolesName);
        authorizationInfo.addStringPermissions(itemsName);
        return authorizationInfo;
    }

    /**
     * 登录认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //执行登录操作
        String username = (String) token.getPrincipal();
        if(StringUtils.isEmpty(username)){
            return null;
        }
        User user = userRepository.findByUsername(username);
        if(user == null){
            return null;
        }

        UserLoginInfo userInfo = new UserLoginInfo();
        BeanUtils.copyProperties(user, userInfo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo, user.getPassword()/*, ByteSource.Util.bytes(user.getSalt())*/, getName());

        return authenticationInfo;
    }

}
