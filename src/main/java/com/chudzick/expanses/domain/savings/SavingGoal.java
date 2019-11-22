package com.chudzick.expanses.domain.savings;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "saving_goal")
public class SavingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Timestamp version;

    @ManyToMany
    @JoinTable(
            name = "appUser_savings",
            joinColumns = @JoinColumn(name = "appuser_id")
    )
    private Set<AppUser> appUsers;

    private BigDecimal goal;
    private BigDecimal startAmount = BigDecimal.ZERO;
    private String goalName;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "savingGoal",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE
    )
    private List<SavingPayment> savingPayments;

    @Transient
    private BigDecimal currentlySaved;

    @PostLoad
    public void setUpCurrentlySaved() {
        BigDecimal allPayments = BigDecimal.ZERO;
        for (SavingPayment payment : savingPayments) {
            if (payment.getSavingPaymentType().equals(SavingPaymentType.ADD)) {
                allPayments = allPayments.add(payment.getAmount());
            } else {
                allPayments = allPayments.subtract(payment.getAmount());
            }
        }
        currentlySaved = allPayments.add(startAmount);
    }

    public BigDecimal getCurrentlySaved() {
        return currentlySaved;
    }

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    public Long getId() {
        return id;
    }


    public Timestamp getVersion() {
        return version;
    }


    public Set<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    public BigDecimal getGoal() {
        return goal;
    }

    public void setGoal(BigDecimal goal) {
        this.goal = goal;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public List<SavingPayment> getSavingPayments() {
        return savingPayments;
    }

    public void setSavingPayments(List<SavingPayment> savingPayments) {
        this.savingPayments = savingPayments;
    }
}
