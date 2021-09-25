package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:37 AM
 */

@Entity
@Table(name = "cscs_transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String transactionId;

    private String cntrlId;

    private Date transactionDate;

    private String company;

    private Long unit;

    private String CHN;

    private String sell;

    private String sellOrBuy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cscs_transaction_request_id")
    @JsonIgnore
    private TransactionRequest transactionRequest;

    @OneToOne(mappedBy="transaction", cascade=CascadeType.ALL)
    @JsonIgnore
    private ProcessedTransaction processedTransaction;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCntrlId() {
        return cntrlId;
    }

    public void setCntrlId(String cntrlId) {
        this.cntrlId = cntrlId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public String getCHN() {
        return CHN;
    }

    public void setCHN(String CHN) {
        this.CHN = CHN;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSellOrBuy() {
        return sellOrBuy;
    }

    public void setSellOrBuy(String sellOrBuy) {
        this.sellOrBuy = sellOrBuy;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }


    public ProcessedTransaction getProcessedTransaction() {
        return processedTransaction;
    }

    public void setProcessedTransaction(ProcessedTransaction processedTransaction) {
        this.processedTransaction = processedTransaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", cntrlId='" + cntrlId + '\'' +
                ", transactionDate=" + transactionDate +
                ", company='" + company + '\'' +
                ", unit=" + unit +
                ", CHN='" + CHN + '\'' +
                ", sell='" + sell + '\'' +
                ", sellOrBuy='" + sellOrBuy + '\'' +
                '}';
    }
}
