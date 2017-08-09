package com.swntek.dao;

import com.swntek.bean.Company;
import com.swntek.bean.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by mac on 17/8/7.
 */
public interface Companydao extends JpaRepository<Company, Long> {

    @Query(value = "select * from company where duty=?1 order by id desc limit 0,1",nativeQuery = true)
    Company findduty(String duty);
}
