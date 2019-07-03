package com.chudzick.expanses.domain.expanses;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "transaction_group")
public class TransactionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String gorupName;

    @NotNull
    @NotEmpty
    private String groupDescription;

    @OneToMany(
            mappedBy = "transactionGroup"
    )
    private List<SingleTransaction> transactions;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

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
