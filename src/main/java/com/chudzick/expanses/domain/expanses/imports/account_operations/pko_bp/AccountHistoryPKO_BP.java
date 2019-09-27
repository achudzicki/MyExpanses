package com.chudzick.expanses.domain.expanses.imports.account_operations.pko_bp;

import com.chudzick.expanses.utils.ListUnmarshalable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "account-history")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountHistoryPKO_BP implements ListUnmarshalable<AccountOperationsPKO_BP> {

    @XmlElement(name = "operations")
    private AccountOperationList accountOperationList;

    @Override
    public List<AccountOperationsPKO_BP> getList() {
        return accountOperationList.operations;
    }

    @XmlRootElement(name = "operations")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class AccountOperationList {
        @XmlElement(name = "operation")
        private List<AccountOperationsPKO_BP> operations;

        public void setOperations(List<AccountOperationsPKO_BP> operations) {
            this.operations = operations;
        }
    }
}


