package com.swntek.dao;

import com.swntek.bean.Shop;
import com.swntek.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by mac on 17/8/7.
 */
public interface Userdao extends JpaRepository<User, Long> {
    @Query(value = "select * from user where openid=?1 order by id desc Limit 0,1",nativeQuery = true)
    User findOpenid(String openid);
}
