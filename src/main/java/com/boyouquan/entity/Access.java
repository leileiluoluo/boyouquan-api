package com.boyouquan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "access")
public class Access {

    @EmbeddedId
    private AccessPrimaryKey id;

    @Column(name = "amount")
    private Integer amount;

}
