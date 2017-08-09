package com.swntek.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String name;
    private String duty;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createtime;
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Invoice> invoiceList=new ArrayList<>();
    @ManyToMany(mappedBy = "companyList")
    @JsonIgnore
    private List<User> userList=new ArrayList<>();
}
