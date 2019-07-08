package org.fage.springbootshiro.bean.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:37
 * @description
 **/
@Entity
@Table(name = "t_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal dataId;

    //用户名
    private String username;

    //昵称
    private String nickname;

    //密码
    private String password;

    //盐
    private String salt;

   /* @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_role", joinColumns = {@JoinColumn(name="user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;*/

}
