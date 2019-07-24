package com.chudzick.expanses.domain.expanses;

import com.chudzick.expanses.domain.users.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Table(name = "cycle")
@Entity
public class Cycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BigDecimal saveGoal;
    private boolean active;

    @OneToMany(
            mappedBy = "cycle"
    )
    private List<SingleTransaction> cycleTransactions;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public boolean isActive() {
        return active;
    }

    public Long getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public BigDecimal getSaveGoal() {
        return saveGoal;
    }

    public void setSaveGoal(BigDecimal saveGoal) {
        this.saveGoal = saveGoal;
    }

    public List<SingleTransaction> getCycleTransactions() {
        return cycleTransactions;
    }

    public void setCycleTransactions(List<SingleTransaction> cycleTransactions) {
        this.cycleTransactions = cycleTransactions;
    }
}
