package com.chudzick.expanses.domain.expanses;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transaction_group")
public class TransactionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String gorupName;
    private String groupDescription;

    @OneToMany(
            mappedBy = "transactionGroup"
    )
    private List<SingleTransaction> transactions;

    @OneToMany(
            mappedBy = "transactionGroup"
    )
    private List<ConstantTransaction> constantTransactions;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;


    public List<ConstantTransaction> getConstantTransactions() {
        return constantTransactions;
    }

    public void setConstantTransactions(List<ConstantTransaction> constantTransactions) {
        this.constantTransactions = constantTransactions;
    }

    public List<SingleTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<SingleTransaction> transactions) {
        this.transactions = transactions;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public String getGorupName() {
        return gorupName;
    }

    public void setGorupName(String gorupName) {
        this.gorupName = gorupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
}
