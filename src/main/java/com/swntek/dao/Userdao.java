package com.swntek.dao;

import com.swntek.bean.Shop;
import com.swntek.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 17/8/7.
 */
public interface Userdao extends JpaRepository<User, Long> {

}
