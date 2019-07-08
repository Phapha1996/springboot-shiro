package org.fage.springbootshiro.repository;

import org.fage.springbootshiro.bean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:59
 * @description
 **/
public interface UserRepository extends JpaRepository<User, BigDecimal> {
    User findByUsername(String username);
}
