package com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public final class AccountOperationsPKO_BP {

    @XmlElement(name = "exec-date")
    private String date;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "type")
    private String operationType;
    @XmlElement(name = "amount")
    private double amount;

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }
}
