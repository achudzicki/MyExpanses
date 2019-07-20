package com.chudzick.expanses.domain.settings.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class UserSettingsDto {

    @Min(value = 1, message = "Dzień początku/końca cylku musi być po 1 dniem miesiąca")
    @Max(value = 28, message = "Dzień początku/końca cylku musi być przed 28 dniem miesiąca")
    private int cycleDays;

    @NotNull
    @DecimalMin(value = "0.00", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal cycleSaveGoal;

    private boolean automaticExtension;

    public boolean isAutomaticExtension() {
        return automaticExtension;
    }

    public void setAutomaticExtension(boolean automaticExtension) {
        this.automaticExtension = automaticExtension;
    }

    public int getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(int cycleDays) {
        this.cycleDays = cycleDays;
    }

    public BigDecimal getCycleSaveGoal() {
        return cycleSaveGoal;
    }

    public void setCycleSaveGoal(BigDecimal cycleSaveGoal) {
        this.cycleSaveGoal = cycleSaveGoal;
    }
}
