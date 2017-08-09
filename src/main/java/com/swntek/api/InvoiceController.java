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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
        User finduser = userdao.findOpenid(openid);
        User user;
        Company company;
        if(finduser==null){
             user=new User();
        }else{
            user=finduser;
        }
        Company findcompany = companydao.findduty(duty);
        if(findcompany==null) {
            company=new Company();
        }else{
            company=findcompany;
        }
        user.setPhone(phone);
        user.setOpenid(openid);
        company.setName(cname);
        company.setDuty(duty);
        Company savecompany=companydao.saveAndFlush(company);
        user.getCompanySet().add(savecompany);
        User saveuser= userdao.saveAndFlush(user);
        Invoice invoice=new Invoice();
        invoice.setShopid(shopid);
        invoice.setSerialnumber(UUID.randomUUID().toString());
        invoice.setPrice(price);
        invoice.setUser(saveuser);
        invoice.setCompany(savecompany);
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
    public boolean changstate(int state,long invoiceid,double price){
        Invoice one = invoicedao.findOne(invoiceid);
        one.setPrice(price);
        one.setState(state);
        invoicedao.saveAndFlush(one);
        return true;
    }
    @GetMapping("/listbyphone")
    public List<Invoice> listbyphone(@RequestParam String phone,@RequestParam long shopid){
        List<Invoice> all = invoicedao.findstate(1,shopid);
        List<Invoice> res=new ArrayList<>();
        for(Invoice invoice:all){
            if(invoice.getUser().getPhone().equals(phone)){
                res.add(invoice);
            }
        }
        return res;
    }
    @PostMapping("/mergestart")
    public List<Invoice> mergestart(@RequestParam String ids){
        List<Invoice> res=new ArrayList<>();
        String[] idsplit = ids.split(";");
        List<Long> idslong=new ArrayList<>();
        for(String id:idsplit){
            idslong.add(Long.parseLong(id));
        }
        List<BigInteger> groupcompanyids = invoicedao.groupcompanyids(idslong);
        for(BigInteger companyid:groupcompanyids){
            Invoice invoice=new Invoice();
            for(Long invoiceid:idslong){
                Invoice one = invoicedao.findOne(invoiceid);
                if(one.getCompany().getId()==companyid.longValue()){
                    BigDecimal bigDecimal=new BigDecimal(invoice.getPrice());
                    BigDecimal result = bigDecimal.add(new BigDecimal(one.getPrice()));
                    invoice.setCompany(one.getCompany());
                    invoice.setUser(one.getUser());
                    invoice.setPrice(result.doubleValue());
                    one.setState(2);
                    invoicedao.saveAndFlush(one);
                    break;
                }
            }
            res.add(invoice);
        }
        return res;
    }
}
