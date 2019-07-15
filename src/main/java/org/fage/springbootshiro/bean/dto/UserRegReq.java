package org.fage.springbootshiro.bean.dto;

import lombok.Data;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/12 15:51
 * @description 用户注册请求
 **/
@Data
public class UserRegReq {
    //用户名
    private String username;

    //昵称
    private String nickname;

    //密码
    private String password;

}