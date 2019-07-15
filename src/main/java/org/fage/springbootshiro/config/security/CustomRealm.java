package org.fage.springbootshiro.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.fage.springbootshiro.bean.entity.RoleEntity;
import org.fage.springbootshiro.bean.entity.UserEntity;
import org.fage.springbootshiro.constant.UserStatusEnum;
import org.fage.springbootshiro.repository.ItemRepository;
import org.fage.springbootshiro.repository.RoleRepository;
import org.fage.springbootshiro.repository.UserRepository;
import org.fage.springbootshiro.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:58
 * @description
 **/
@Slf4j
@Configuration
public class CustomRealm extends AuthorizingRealm {

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
        UserSession user = (UserSession) principals.getPrimaryPrincipal();
        List<RoleEntity> roles = roleRepository.findByUserDataId(user.getDataId());
        //得出用户角色
        Set<String> rolesName = roles.stream().map(ent -> ent.getName()).collect(Collectors.toSet());
        //得出用户菜单
        Set<String> itemsName = itemRepository.findByRoleDataIds(roles.stream().map(ent -> ent.getDataId()).collect(Collectors.toList()))
                .stream().map(ent -> ent.getName()).collect(Collectors.toSet());

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

        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            throw new UnknownAccountException("用户名或密码错误!");
        }
        if(!UserStatusEnum.NORMAL.getValue().equals(userEntity.getStatus())){
            throw new LockedAccountException("账号状态异常,用户已锁定!");
        }
        UserSession user = CopyUtils.copyDifferentProp(userEntity, UserSession.class);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, userEntity.getPassword(), getName());
        //盐=平台公盐 + 用户私盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(PasswordHelper.PUB_SALT + userEntity.getSalt()));

        return authenticationInfo;
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    /**
     * 设置密码匹配器
     * @param credentialsMatcher
     */
    @Override
    @Autowired
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }
}
