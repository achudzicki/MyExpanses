package com.chudzick.expanses.domain.savings.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingGoalDto {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 50, message = "Opis celu musi być dłuższy niż 3 znaki i krótszy od 50")
    private String name;

    @NotNull
    @DecimalMin(value = "0.00", message = "Kwota nie może być mniejsza od zera")
    private BigDecimal totalAmount;

    private BigDecimal initialAmount = BigDecimal.ZERO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @Future
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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
}
