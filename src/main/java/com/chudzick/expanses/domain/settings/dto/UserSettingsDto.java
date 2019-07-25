package com.chudzick.expanses.domain.settings.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class UserSettingsDto {

    @Min(1)
    @Max(28)
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
