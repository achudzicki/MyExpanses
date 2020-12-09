package com.chudzick.expanses.domain.application.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ApplicationAccessDto {

    @NotNull
    @NotEmpty
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
