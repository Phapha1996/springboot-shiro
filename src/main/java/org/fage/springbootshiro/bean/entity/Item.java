package org.fage.springbootshiro.bean.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:41
 * @description
 **/
@Entity
@Table(name = "t_item")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal dataId;

    //父菜单id
    private BigDecimal parentId;

    //菜单名称
    private String name;

    //菜单url
    private String url;

    //菜单描述
    private String description;

}
