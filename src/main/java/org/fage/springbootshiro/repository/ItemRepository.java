package org.fage.springbootshiro.repository;

import org.fage.springbootshiro.bean.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Caizhf
 * @version 1.0
 * @date 2019/6/4 11:00
 * @description
 **/
public interface ItemRepository extends JpaRepository<Item, BigDecimal> {

    @Query(nativeQuery = true, value = "select t1.* from t_item t1 join t_role_item t2 on t1.data_id=t2.item_id where t2.role_id in :collect")
    List<Item> findByRoleDataIds(@Param("collect") List<BigDecimal> collect);
}
