package com.chudzick.expanses.beans.chart;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AverageTransactionsPerGroupRadarChart {
    private double[] income;
    private double[] expanse;
    private String[] groupNames;

    public double[] getIncome() {
        return income;
    }

    public void setIncome(double[] income) {
        this.income = income;
    }

    public double[] getExpanse() {
        return expanse;
    }

    public void setExpanse(double[] expanse) {
        this.expanse = expanse;
    }

    public String[] getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String[] groupNames) {
        this.groupNames = groupNames;
    }
}
