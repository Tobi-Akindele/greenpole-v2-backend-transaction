package com.ap.greenpole.transactioncomponent.dto;

import com.ap.greenpole.transactioncomponent.util.Utils;

import java.util.Date;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/13/2020 8:46 PM
 */
public class ModuleRequestDTO {

    private String transactionType;

    private String depository;

    private Date transactionDate;

    private Date uploadDate = new Date();

    private String clientCompany;

    private String transactionFileAddress;

    private String transactionMasterFileAddress;

    private boolean isTransactionBalance;

    private String balanceTransactionMessage;


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

    public boolean isTransactionBalance() {
        return isTransactionBalance;
    }

    public void setTransactionBalance(boolean transactionBalance) {
        isTransactionBalance = transactionBalance;
    }

    public String getBalanceTransactionMessage() {
        return balanceTransactionMessage;
    }

    public void setBalanceTransactionMessage(String balanceTransactionMessage) {
        this.balanceTransactionMessage = balanceTransactionMessage;
    }

    @Override
    public String toString() {
        return "ModuleRequestDTO{" +
                "transactionType='" + transactionType + '\'' +
                ", depository='" + depository + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", uploadDate=" + uploadDate +
                ", clientCompany=" + clientCompany +
                ", transactionFileAddress='" + transactionFileAddress + '\'' +
                ", transactionMasterFileAddress='" + transactionMasterFileAddress + '\'' +
                ", isTransactionBalance=" + isTransactionBalance +
                ", balanceTransactionMessage='" + balanceTransactionMessage + '\'' +
                '}';
    }

}
