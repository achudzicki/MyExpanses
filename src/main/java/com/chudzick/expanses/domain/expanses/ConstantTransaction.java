package com.chudzick.expanses.domain.expanses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "constant_transactions")
public class ConstantTransaction extends UserTransactions {

    @Column(name = "is_permanent")
    private boolean permanentDuration;

    //DURING HOW MANY CYCLES SHOULD TRANSACTION BE INJECTED (NULL IF TRANSACTION IS PERMANENT)
    @Column(name = "cycles_count")
    private Integer cyclesCount;

    @Column(name = "appears")
    private long cyclesAppears;


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

    public long getCyclesAppears() {
        return cyclesAppears;
    }

    public void setCyclesAppears(long cyclesAppears) {
        this.cyclesAppears = cyclesAppears;
    }
}
