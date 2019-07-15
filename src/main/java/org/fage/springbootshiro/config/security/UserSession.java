package org.fage.springbootshiro.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/7/12 16:06
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    private BigDecimal dataId;

    private String username;
}
