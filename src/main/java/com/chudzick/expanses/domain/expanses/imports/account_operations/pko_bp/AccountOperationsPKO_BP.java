package com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp;

import com.chudzick.expanses.domain.expanses.imports.account_operations.AccountOperation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public final class AccountOperationsPKO_BP implements AccountOperation {

    @XmlElement(name = "exec-date")
    private String date;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "type")
    private String operationType;
    @XmlElement(name = "amount")
    private double amount;

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getOperationType() {
        return this.operationType;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}
