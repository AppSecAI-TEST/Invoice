package com.swntek.api;

import com.swntek.bean.User;
import com.swntek.dao.Companydao;
import com.swntek.dao.Invoicedao;
import com.swntek.dao.Userdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mac on 17/8/9.
 */
@RestController
@RequestMapping("/api/pre")
public class PreController  {
    @Autowired
    Companydao companydao;
    @Autowired
    Userdao userdao;
    @Autowired
    Invoicedao invoicedao;

    @PostMapping("/findbyopenid")
    public User findopenid(@RequestParam String openid){
        User user = userdao.findOpenid(openid);
        return user;
    }
}
