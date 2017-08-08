package com.swntek.api;

import com.swntek.Utils.Constact;
import com.swntek.bean.Company;
import com.swntek.bean.Invoice;
import com.swntek.bean.User;
import com.swntek.dao.Companydao;
import com.swntek.dao.Invoicedao;
import com.swntek.dao.Userdao;
import io.goeasy.GoEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by mac on 17/8/7.
 */
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    @Autowired
    Companydao companydao;
    @Autowired
    Userdao userdao;
    @Autowired
    Invoicedao invoicedao;

    @PostMapping("/add")
    public String add(@RequestParam long shopid,@RequestParam String openid,@RequestParam String phone,@RequestParam String cname,@RequestParam String duty,@RequestParam double price){
        User user=new User();
        user.setPhone(phone);
        user.setOpenid(openid);
        Company company=new Company();
        company.setName(cname);
        company.setDuty(duty);
        Company savecompany = companydao.save(company);
        user.getCompanyList().add(savecompany);
        User saveuser = userdao.save(user);
        Invoice invoice=new Invoice();
        invoice.setShopid(shopid);
        invoice.setSerialnumber(UUID.randomUUID().toString());
        invoice.setPrice(price);
        invoice.setUser(saveuser);
        invoice.setCompany(company);
        invoicedao.save(invoice);
        GoEasy goEasy = new GoEasy(Constact.pubkey);
        goEasy.publish("shopid", "Hello, GoEasy!");
        return "success";
    }
    @GetMapping("/list")
    public List<Invoice> list(@RequestParam long shopid){
        List<Invoice> all = invoicedao.findstate(0,shopid);
        return all;
    }
    @GetMapping("/listmerge")
    public List<Invoice> listmerge(@RequestParam long shopid){
        List<Invoice> all = invoicedao.findstate(1,shopid);
        return all;
    }
    @GetMapping("/listalread")
    public List<Invoice> listalread(@RequestParam long shopid){
        List<Invoice> all = invoicedao.findstate(2,shopid);
        return all;
    }
    @PostMapping("/changestate")
    public boolean changstate(int state,long invoiceid){
        Invoice one = invoicedao.findOne(invoiceid);
        one.setState(state);
        invoicedao.saveAndFlush(one);
        return true;
    }
}
