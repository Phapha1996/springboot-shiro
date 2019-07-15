package org.fage.springbootshiro.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fage.springbootshiro.bean.Response;
import org.fage.springbootshiro.bean.ResponseEnum;
import org.fage.springbootshiro.bean.dto.UserRegReq;
import org.fage.springbootshiro.bean.entity.UserEntity;
import org.fage.springbootshiro.config.security.PasswordHelper;
import org.fage.springbootshiro.constant.UserStatusEnum;
import org.fage.springbootshiro.repository.ItemRepository;
import org.fage.springbootshiro.repository.RoleRepository;
import org.fage.springbootshiro.repository.UserRepository;
import org.fage.springbootshiro.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 16:46
 * @description
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "P01-UserController", description = "用户模块")
public class UserController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/login")
    public String login(){
       return "你还没有登录,请先去登录";
    }

    @GetMapping("/doLogin")
    @ApiOperation(value = "102-用户登录", notes = "用户登录")
    public String doLogin(@RequestParam String username,@RequestParam String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        return "success" + token.toString();
    }

    @GetMapping({"/", "/index"})
    public String index(){
        return "你已经登录成功啦,现在是在主页面中";
    }

    @GetMapping("/403")
    public String unAuth(){
        return "你没有访问权限";
    }

    @GetMapping("/list")
    @RequiresPermissions("userInfo:list")
    @ApiOperation(value = "101-用户注册", notes = "用户注册")
    public String list(){
        return "你看到了列表页面，说明你有权限!";
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/reg")
    @ApiOperation(value = "101-用户注册", notes = "用户注册")
    public Response<String> register(@RequestBody UserRegReq user){
        //验证用户是否存在
        if(userRepository.findByUsername(user.getUsername()) != null){
            return Response.busiFailed(ResponseEnum.BUSI_FAILED_USER_REG.getCode(),"用户名已存在");
        }
        UserEntity userEntity = CopyUtils.copySameProp(user, UserEntity.class);
        //密码加密
        PasswordHelper.encrypt(userEntity);
        //入库
        userEntity.setStatus(UserStatusEnum.NORMAL.getValue());
        userRepository.save(userEntity);

        return Response.success(userEntity!=null ? userEntity.getDataId().toString() : "");
    }
}
