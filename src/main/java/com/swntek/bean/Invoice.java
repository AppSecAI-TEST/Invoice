package com.swntek.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mac on 17/8/7.
 */
@Entity
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String serialnumber;//编号
    private long shopid;//店铺id
    private int state;//0待确认 1下次合开 2已经开过
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createtime;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "company_id",referencedColumnName = "id")
    private Company company;
    private double price;
}
