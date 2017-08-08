package com.swntek.dao;

import com.swntek.bean.Invoice;
import com.swntek.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by mac on 17/8/7.
 */
public interface Invoicedao extends JpaRepository<Invoice, Long> {
    @Query(value = "select * from invoice where state=?1 and shopid=?2 order by id desc",nativeQuery = true)
    List<Invoice> findstate(int state,long shopid);
}
