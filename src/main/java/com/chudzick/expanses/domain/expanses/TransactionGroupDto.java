package com.chudzick.expanses.domain.expanses;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TransactionGroupDto {

    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String gorupName;

    @NotNull
    @NotEmpty(message = "{form.validation.not.empty}")
    private String groupDescription;

    public String getGorupName() {
        return gorupName;
    }

    public void setGorupName(String gorupName) {
        this.gorupName = gorupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }
}
