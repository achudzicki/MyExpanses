package com.chudzick.expanses.domain.expanses;

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
    private boolean isActive;

    @OneToMany(
            mappedBy = "cycle"
    )
    private List<SingleTransaction> cycleTransactions;

    public boolean isActive() {
        return isActive;
    }

    public Long getId() {
        return id;
    }

    public void setActive(boolean active) {
        isActive = active;
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
