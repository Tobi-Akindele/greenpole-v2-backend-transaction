package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/4/2020 5:11 PM
 */

@Entity
@Table(name = "support_account")
public class SupportAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chn;

    private long supportUnit;

    private String clientCompany;

    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_transaction_id")
    @JsonIgnore
    private ProcessedTransaction processedTransaction;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public long getSupportUnit() {
        return supportUnit;
    }

    public void setSupportUnit(long supportUnit) {
        this.supportUnit = supportUnit;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public ProcessedTransaction getProcessedTransaction() {
        return processedTransaction;
    }

    public void setProcessedTransaction(ProcessedTransaction processedTransaction) {
        this.processedTransaction = processedTransaction;
    }

    @Override
    public String toString() {
        return "SupportAccount{" +
                "id=" + id +
                ", chn='" + chn + '\'' +
                ", supportUnit='" + supportUnit + '\'' +
                ", clientCompany='" + clientCompany + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", processedTransaction=" + processedTransaction +
                '}';
    }
}
