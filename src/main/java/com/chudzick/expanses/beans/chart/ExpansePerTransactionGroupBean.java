package com.chudzick.expanses.beans.chart;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExpansePerTransactionGroupBean {
    private String[] transactionGroupNames;
    private BigDecimal[] expansePerGroup;

    public String[] getTransactionGroupNames() {
        return transactionGroupNames;
    }

    public void setTransactionGroupNames(String[] transactionGroupNames) {
        this.transactionGroupNames = transactionGroupNames;
    }

    public BigDecimal[] getExpansePerGroup() {
        return expansePerGroup;
    }

    public void setExpansePerGroup(BigDecimal[] expansePerGroup) {
        this.expansePerGroup = expansePerGroup;
    }
}
