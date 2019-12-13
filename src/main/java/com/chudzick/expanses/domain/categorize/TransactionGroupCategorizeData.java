package com.chudzick.expanses.domain.categorize;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;

@Entity
@Table(name = "group_categorize")
public class TransactionGroupCategorizeData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String key;

    @OneToOne
    @JoinColumn(name = "transaction_group_id")
    private TransactionGroup transactionGroup;
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;


    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    private long strength;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }
}
