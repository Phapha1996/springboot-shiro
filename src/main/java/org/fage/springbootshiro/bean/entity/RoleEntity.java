package org.fage.springbootshiro.bean.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:39
 * @description
 **/
@Entity
@Table(name = "t_role")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal dataId;

    //角色名
    private String name;

    //角色描述
    private String description;

    //可用状态
    private String available;

    /*@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_item", joinColumns = {@JoinColumn(name="role_id")}, inverseJoinColumns = {@JoinColumn(name = "item_id")})
    private List<Item> items;*/
}
