package com.chudzick.expanses.beans.transactions;

import com.chudzick.expanses.domain.expanses.dto.ConstTransactionDeleteDto;

import java.util.List;

public class DeleteConstantTransactionBean {
    private List<ConstTransactionDeleteDto> constantTransDto;
    private long constantTransactionId;

    public long getConstantTransactionId() {
        return constantTransactionId;
    }

    public void setConstantTransactionId(long constantTransactionId) {
        this.constantTransactionId = constantTransactionId;
    }

    public List<ConstTransactionDeleteDto> getConstantTransDto() {
        return constantTransDto;
    }

    public void setConstantTransDto(List<ConstTransactionDeleteDto> constantTransDto) {
        this.constantTransDto = constantTransDto;
    }
}
