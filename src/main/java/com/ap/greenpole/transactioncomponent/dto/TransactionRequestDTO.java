package com.ap.greenpole.transactioncomponent.dto;

import com.ap.greenpole.transactioncomponent.entity.ShareHolderTemp;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:13 PM
 */

public class TransactionRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String transactionType;

 	private String depository;

    private Date transactionDate;

    private Date uploadDate;

 	private String clientCompany;

    private String transactionFileAddress;

    private String transactionMasterFileAddress;

    private String clientCompanyName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDepository() {
        return depository;
    }

    public void setDepository(String depository) {
        this.depository = depository;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getTransactionFileAddress() {
        return transactionFileAddress;
    }

    public void setTransactionFileAddress(String transactionFileAddress) {
        this.transactionFileAddress = transactionFileAddress;
    }

    public String getTransactionMasterFileAddress() {
        return transactionMasterFileAddress;
    }

    public void setTransactionMasterFileAddress(String transactionMasterFileAddress) {
        this.transactionMasterFileAddress = transactionMasterFileAddress;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public void setClientCompanyName(String clientCompanyName) {
        this.clientCompanyName = clientCompanyName;
    }
}
