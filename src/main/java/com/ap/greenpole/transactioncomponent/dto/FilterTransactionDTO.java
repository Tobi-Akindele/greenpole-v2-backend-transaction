package com.ap.greenpole.transactioncomponent.dto;

import java.util.Date;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/23/2020 3:45 PM
 */
public class FilterTransactionDTO {

    private String transactionRequestId;
    private String transactionType;
    private String clientCompany;
    private Date transactionDate;
    private String cscsTransactionId;
    private String sellerChn;
    private String buyerChn;
    private String sellerUnitBefore;
    private String sellerUnitAfter;
    private String buyerUnitBefore;
    private String buyerUnitAfter;
    private String unitTransacted;
    private String isCancelled;
    private String isProcessed;
    private Date actionDate;


    public String getTransactionRequestId() {
        return transactionRequestId;
    }

    public void setTransactionRequestId(String transactionRequestId) {
        this.transactionRequestId = transactionRequestId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(String clientCompany) {
        this.clientCompany = clientCompany;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCscsTransactionId() {
        return cscsTransactionId;
    }

    public void setCscsTransactionId(String cscsTransactionId) {
        this.cscsTransactionId = cscsTransactionId;
    }

    public String getSellerChn() {
        return sellerChn;
    }

    public void setSellerChn(String sellerChn) {
        this.sellerChn = sellerChn;
    }

    public String getBuyerChn() {
        return buyerChn;
    }

    public String getSellerUnitBefore() {
        return sellerUnitBefore;
    }

    public void setSellerUnitBefore(String sellerUnitBefore) {
        this.sellerUnitBefore = sellerUnitBefore;
    }

    public String getSellerUnitAfter() {
        return sellerUnitAfter;
    }

    public void setSellerUnitAfter(String sellerUnitAfter) {
        this.sellerUnitAfter = sellerUnitAfter;
    }

    public String getBuyerUnitBefore() {
        return buyerUnitBefore;
    }

    public void setBuyerUnitBefore(String buyerUnitBefore) {
        this.buyerUnitBefore = buyerUnitBefore;
    }

    public String getBuyerUnitAfter() {
        return buyerUnitAfter;
    }

    public void setBuyerUnitAfter(String buyerUnitAfter) {
        this.buyerUnitAfter = buyerUnitAfter;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public void setBuyerChn(String buyerChn) {
        this.buyerChn = buyerChn;
    }

    public String getUnitTransacted() {
        return unitTransacted;
    }

    public void setUnitTransacted(String unitTransacted) {
        this.unitTransacted = unitTransacted;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    @Override
    public String toString() {
        return "FilterTransactionDTO{" +
                "transactionRequestId='" + transactionRequestId + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", clientCompany='" + clientCompany + '\'' +
                ", transactionDate=" + transactionDate +
                ", cscsTransactionId='" + cscsTransactionId + '\'' +
                ", sellerChn='" + sellerChn + '\'' +
                ", buyerChn='" + buyerChn + '\'' +
                ", sellerUnitBefore='" + sellerUnitBefore + '\'' +
                ", sellerUnitAfter='" + sellerUnitAfter + '\'' +
                ", buyerUnitBefore='" + buyerUnitBefore + '\'' +
                ", buyerUnitAfter='" + buyerUnitAfter + '\'' +
                ", unitTransacted='" + unitTransacted + '\'' +
                ", isCancelled='" + isCancelled + '\'' +
                ", isProcessed='" + isProcessed + '\'' +
                ", actionDate=" + actionDate +
                '}';
    }
}
