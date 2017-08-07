package com.swntek.bean;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mac on 17/8/7.
 */
@Getter
@Setter
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String name;
    private String serialnumber;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createtime;
}
