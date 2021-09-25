package com.ap.greenpole.transactioncomponent.dto;

import com.ap.greenpole.transactioncomponent.entity.SupportAccount;

import java.util.List;

public class AddSupportAccountDTO {

    private long transactionRecordId;

    private List<SupportAccount> accountNumbers;

    public long getTransactionRecordId() {
        return transactionRecordId;
    }

    public void setTransactionRecordId(long transactionRecordId) {
        this.transactionRecordId = transactionRecordId;
    }

    public List<SupportAccount> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<SupportAccount> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }

    @Override
    public String toString() {
        return "AddSupportAccountDTO{" +
                "transactionRecordId=" + transactionRecordId +
                ", accountNumbers=" + accountNumbers +
                '}';
    }
}
