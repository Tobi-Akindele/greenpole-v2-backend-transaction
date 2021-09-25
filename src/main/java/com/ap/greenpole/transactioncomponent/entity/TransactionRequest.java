package com.ap.greenpole.transactioncomponent.entity;

import com.ap.greenpole.transactioncomponent.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:13 PM
 */
@Entity
@Table(name = "cscs_transaction_request")
public class TransactionRequest {

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

    @OneToMany(mappedBy="transactionRequest", cascade=CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transaction> transactionData;

    @OneToMany(mappedBy="transactionRequest", cascade=CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TransactionMaster> transactionMasterData;

    @OneToMany(mappedBy="transactionRequest", cascade=CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ShareHolderTemp> shareHolderTempData;


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


    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public List<Transaction> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<Transaction> transactionData) {
        this.transactionData = transactionData;
    }

    public List<TransactionMaster> getTransactionMasterData() {
        return transactionMasterData;
    }

    public void setTransactionMasterData(List<TransactionMaster> transactionMasterData) {
        this.transactionMasterData = transactionMasterData;
    }

    public List<ShareHolderTemp> getShareHolderTempData() {
        return shareHolderTempData;
    }

    public void setShareHolderTempData(List<ShareHolderTemp> shareHolderTempData) {
        this.shareHolderTempData = shareHolderTempData;
    }


    public void addTransactionData(Transaction transaction) {
        if(transaction != null){
            if(transactionData == null ){
                transactionData = new ArrayList<>();
            }
            transaction.setTransactionRequest(this);
            transactionData.add(transaction);
        }
    }

    public void addShareHolderTempData(ShareHolderTemp shareHolderTemp){
        if(shareHolderTemp != null){
            if(shareHolderTempData == null ){
                shareHolderTempData = new ArrayList<>();
            }
            shareHolderTemp.setTransactionRequest(this);
            shareHolderTempData.add(shareHolderTemp);
        }
    }

    public void addTransactionMasterData(TransactionMaster transactionMaster){
        if(transactionMaster != null){
            if(transactionMasterData == null ){
                transactionMasterData = new ArrayList<>();
            }
            transactionMaster.setTransactionRequest(this);
            transactionMasterData.add(transactionMaster);
        }
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "id=" + id +
                ", transactionType='" + transactionType + '\'' +
                ", depository='" + depository + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                ", clientCompany='" + clientCompany + '\'' +
                ", transactionFileAddress='" + transactionFileAddress + '\'' +
                ", transactionMasterFileAddress='" + transactionMasterFileAddress + '\'' +
                '}';
    }
}
