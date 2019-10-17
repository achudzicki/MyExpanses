package com.chudzick.expanses.beans.chart;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SavingsPerCycleChartBean {
    private BigDecimal[] planningSavings;
    private BigDecimal[] actualSavings;
    private String[] cycleName;

    public BigDecimal[] getPlanningSavings() {
        return planningSavings;
    }

    public void setPlanningSavings(BigDecimal[] planningSavings) {
        this.planningSavings = planningSavings;
    }

    public BigDecimal[] getActualSavings() {
        return actualSavings;
    }

    public void setActualSavings(BigDecimal[] actualSavings) {
        this.actualSavings = actualSavings;
    }

    public String[] getCycleName() {
        return cycleName;
    }

    public void setCycleName(String[] cycleName) {
        this.cycleName = cycleName;
    }
}
