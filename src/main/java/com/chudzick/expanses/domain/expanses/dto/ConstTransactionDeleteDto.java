package com.chudzick.expanses.domain.expanses.dto;

public class ConstTransactionDeleteDto {
    private Long cycleId;
    private boolean delete;

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
