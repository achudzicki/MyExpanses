package com.chudzick.expanses.domain.savings;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "saving_payment")
public class SavingPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Version
    private Timestamp version;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    private BigDecimal amount;
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private SavingPaymentType savingPaymentType;

    @ManyToOne
    @JoinColumn(name = "saving_goal_id")
    private SavingGoal savingGoal;

    public SavingGoal getSavingGoal() {
        return savingGoal;
    }

    public void setSavingGoal(SavingGoal savingGoal) {
        this.savingGoal = savingGoal;
    }

    public long getId() {
        return id;
    }

    public Timestamp getVersion() {
        return version;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public SavingPaymentType getSavingPaymentType() {
        return savingPaymentType;
    }

    public void setSavingPaymentType(SavingPaymentType savingPaymentType) {
        this.savingPaymentType = savingPaymentType;
    }
}
