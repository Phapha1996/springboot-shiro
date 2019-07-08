package org.fage.springbootshiro.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/3 16:29
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginInfo {
    private BigDecimal dataId;

    private String username;

    private String password;
}