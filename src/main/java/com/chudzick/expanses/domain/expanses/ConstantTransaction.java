package com.chudzick.expanses.domain.expanses;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "constant_transactions")
public final class ConstantTransaction extends UserTransactions {

    @Column(name = "is_permanent")
    private boolean permanentDuration;

    //DURING HOW MANY CYCLES SHOULD TRANSACTION BE INJECTED (NULL IF TRANSACTION IS PERMANENT)
    @Column(name = "cycles_count")
    private Integer cyclesCount;

    @Column(name = "appears")
    private Integer cyclesAppears;

    @Column(name = "active")
    private boolean active;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cycle_constant",
            joinColumns = @JoinColumn(name = "constant_transactions_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "cycle_id", referencedColumnName = "id"))
    private Set<Cycle> cycles;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(Set<Cycle> cycles) {
        this.cycles = cycles;
    }

    public boolean isPermanentDuration() {
        return permanentDuration;
    }

    public void setPermanentDuration(boolean permanentDuration) {
        this.permanentDuration = permanentDuration;
    }

    public Integer getCyclesCount() {
        return cyclesCount;
    }

    public void setCyclesCount(Integer cyclesCount) {
        this.cyclesCount = cyclesCount;
    }

    public Integer getCyclesAppears() {
        return cyclesAppears;
    }

    public void setCyclesAppears(Integer cyclesAppears) {
        this.cyclesAppears = cyclesAppears;
    }
}
