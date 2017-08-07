package com.swntek.bean;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mac on 17/8/7.
 */
@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String phone;
    private String openid;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createtime;
    @OneToMany(mappedBy = "user")
    private List<Invoice> invoiceList=new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "user_company",joinColumns ={@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "company_id")})
    private List<Company> companyList=new ArrayList<>();
}
