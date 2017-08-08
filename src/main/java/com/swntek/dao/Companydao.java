package com.swntek.dao;

import com.swntek.bean.Company;
import com.swntek.bean.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mac on 17/8/7.
 */
public interface Companydao extends JpaRepository<Company, Long> {

}
