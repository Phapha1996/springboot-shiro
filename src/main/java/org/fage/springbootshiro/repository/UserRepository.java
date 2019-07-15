package org.fage.springbootshiro.repository;

import org.fage.springbootshiro.bean.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:59
 * @description
 **/
public interface UserRepository extends JpaRepository<UserEntity, BigDecimal> {
    UserEntity findByUsername(String username);

    UserEntity findByUsernameAndStatus(@Param("username") String username,@Param("status") String status);
}
