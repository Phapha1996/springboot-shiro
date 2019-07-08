package org.fage.springbootshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fage.springbootshiro.repository.ItemRepository;
import org.fage.springbootshiro.repository.RoleRepository;
import org.fage.springbootshiro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 16:46
 * @description
 **/
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping("/login")
    public String login(){
       return "你还没有登录,请先去登录";
    }

    @RequestMapping("/doLogin")
    public String doLogin(@RequestParam String username,@RequestParam String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        return "success" + token.toString();
    }

    @RequestMapping({"/", "/index"})
    public String index(){
        return "你已经登录成功啦,现在是在主页面中";
    }

    @RequestMapping("/403")
    public String unAuth(){
        return "你没有访问权限";
    }

    @RequestMapping("/list")
    @RequiresPermissions("userInfo:list")
    public String list(){
        return "你看到了列表页面，说明你有权限!";
    }
}
