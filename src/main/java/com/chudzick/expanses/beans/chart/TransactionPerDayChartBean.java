package com.chudzick.expanses.beans.chart;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionPerDayChartBean {

    private BigDecimal[] expanses;
    private BigDecimal[] incomes;
    private String[] dates;

    public BigDecimal[] getExpanses() {
        return expanses;
    }

    public void setExpanses(BigDecimal[] expanses) {
        this.expanses = expanses;
    }

    public BigDecimal[] getIncomes() {
        return incomes;
    }

    public void setIncomes(BigDecimal[] incomes) {
        this.incomes = incomes;
    }

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }
}
