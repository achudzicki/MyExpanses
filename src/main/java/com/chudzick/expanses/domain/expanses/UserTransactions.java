package com.chudzick.expanses.domain.expanses;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;


@MappedSuperclass
public class UserTransactions {

    @Id
    @GeneratedValue
    protected Long id;

    protected BigDecimal amound;

    @Enumerated(EnumType.STRING)
    protected TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    protected TransactionDuration transactionDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_group_id")
    protected TransactionGroup transactionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuser_id")
    protected AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmound() {
        return amound;
    }

    public void setAmound(BigDecimal amound) {
        this.amound = amound;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionGroup getTransactionGroup() {
        return transactionGroup;
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        this.transactionGroup = transactionGroup;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public boolean isConstantTransaction() {
        return this.transactionDuration.equals(TransactionDuration.CONSTANT);
    }

    public TransactionDuration getTransactionDuration() {
        return transactionDuration;
    }

    public void setTransactionDuration(TransactionDuration transactionDuration) {
        this.transactionDuration = transactionDuration;
    }
}
