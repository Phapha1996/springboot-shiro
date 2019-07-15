package org.fage.springbootshiro.config.security;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.fage.springbootshiro.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/10 14:59
 * @description 根据角色字符串解析出权限通配字符串
 **/
@Configuration
public class CustomRolePermissionResolver implements RolePermissionResolver {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        /*Collection<Permission> permissions = Collections.emptySet();
        List<Item> items = itemRepository.findByRoleName();
        for (Item item : items) {
            WildcardPermission permission = new WildcardPermission(item.getName());
            permissions.add(permission);
        }
        return permissions;*/
        return null;
    }
}
