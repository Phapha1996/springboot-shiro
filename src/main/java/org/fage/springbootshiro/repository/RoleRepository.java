package org.fage.springbootshiro.repository;

import org.fage.springbootshiro.bean.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 10:59
 * @description
 **/
public interface RoleRepository extends JpaRepository<RoleEntity, BigDecimal> {

    /**
     * 查一个用户正常状态的角色
     * @param dataId
     * @return
     */
    @Query(nativeQuery = true, value = "select t1.* from t_role t1 join t_user_role t2 on t1.data_id=t2.role_id where t2.user_id=?1 and t1.available='0'")
    List<RoleEntity> findByUserDataId(BigDecimal dataId);
}
