package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:37 AM
 */

@Entity
@Table(name = "processed_transaction")
public class ProcessedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private boolean isSuspended;

    private boolean isReadyForTransfer;

    private boolean isHolderOnSystem;

    private boolean isProcessed;

    private long unitTransacted;

    private long unitToReceive;

    private boolean isFullTransaction;

    private boolean isPartialTransaction;

    private boolean isReadyForApproval;

    private boolean isCancelled;

    private long remainingUnit;

    private Long sellerUnitBefore;

    private Long sellerUnitAfter;

    private Long buyerUnitBefore;

    private Long buyerUnitAfter;

    private String suspensionReason;

    private String buyerChn;

    private String buyerName;

    private Long buyerAccountNumber;

    private boolean isAddAccountRequired;

    private String status;

    private Date processedOn;

    private Date suspendedOn;

    private String transactionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cscs_transaction_table_id")
    @JsonIgnore
    private Transaction transaction;

    @OneToMany(mappedBy="processedTransaction", cascade=CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SupportAccount> supportAccountData;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public boolean isReadyForTransfer() {
        return isReadyForTransfer;
    }

    public void setReadyForTransfer(boolean readyForTransfer) {
        isReadyForTransfer = readyForTransfer;
    }

    public boolean isHolderOnSystem() {
        return isHolderOnSystem;
    }

    public void setHolderOnSystem(boolean holderOnSystem) {
        isHolderOnSystem = holderOnSystem;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public long getUnitTransacted() {
        return unitTransacted;
    }

    public void setUnitTransacted(long unitTransacted) {
        this.unitTransacted = unitTransacted;
    }

    public long getUnitToReceive() {
        return unitToReceive;
    }

    public void setUnitToReceive(long unitToReceive) {
        this.unitToReceive = unitToReceive;
    }

    public boolean isFullTransaction() {
        return isFullTransaction;
    }

    public void setFullTransaction(boolean fullTransaction) {
        isFullTransaction = fullTransaction;
    }

    public boolean isPartialTransaction() {
        return isPartialTransaction;
    }

    public void setPartialTransaction(boolean partialTransaction) {
        isPartialTransaction = partialTransaction;
    }

    public boolean isReadyForApproval() {
        return isReadyForApproval;
    }

    public void setReadyForApproval(boolean readyForApproval) {
        isReadyForApproval = readyForApproval;
    }

    public long getRemainingUnit() {
        return remainingUnit;
    }

    public void setRemainingUnit(long remainingUnit) {
        this.remainingUnit = remainingUnit;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }

    public boolean isAddAccountRequired() {
        return isAddAccountRequired;
    }

    public void setAddAccountRequired(boolean addAccountRequired) {
        isAddAccountRequired = addAccountRequired;
    }

    public void addSupportAccountData(SupportAccount supportAccount){
        if(supportAccount != null){
            if(supportAccountData == null ){
                supportAccountData = new ArrayList<>();
            }
            supportAccount.setProcessedTransaction(this);
            supportAccountData.add(supportAccount);
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerChn() {
        return buyerChn;
    }

    public void setBuyerChn(String buyerChn) {
        this.buyerChn = buyerChn;
    }

    public List<SupportAccount> getSupportAccountData() {
        return supportAccountData;
    }

    public void setSupportAccountData(List<SupportAccount> supportAccountData) {
        this.supportAccountData = supportAccountData;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getBuyerAccountNumber() {
        return buyerAccountNumber;
    }

    public void setBuyerAccountNumber(Long buyerAccountNumber) {
        this.buyerAccountNumber = buyerAccountNumber;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }

    public Date getSuspendedOn() {
        return suspendedOn;
    }

    public void setSuspendedOn(Date suspendedOn) {
        this.suspendedOn = suspendedOn;
    }


    public long getSellerUnitBefore() {
        return sellerUnitBefore;
    }

    public void setSellerUnitBefore(Long sellerUnitBefore) {
        this.sellerUnitBefore = sellerUnitBefore;
    }

    public Long getSellerUnitAfter() {
        return sellerUnitAfter;
    }

    public void setSellerUnitAfter(Long sellerUnitAfter) {
        this.sellerUnitAfter = sellerUnitAfter;
    }

    public Long getBuyerUnitBefore() {
        return buyerUnitBefore;
    }

    public void setBuyerUnitBefore(Long buyerUnitBefore) {
        this.buyerUnitBefore = buyerUnitBefore;
    }

    public Long getBuyerUnitAfter() {
        return buyerUnitAfter;
    }

    public void setBuyerUnitAfter(Long buyerUnitAfter) {
        this.buyerUnitAfter = buyerUnitAfter;
    }

    @Override
    public String toString() {
        return "ProcessedTransaction{" +
                "id=" + id +
                ", isSuspended=" + isSuspended +
                ", isReadyForTransfer=" + isReadyForTransfer +
                ", isHolderOnSystem=" + isHolderOnSystem +
                ", isProcessed=" + isProcessed +
                ", unitTransacted=" + unitTransacted +
                ", unitToReceive=" + unitToReceive +
                ", isFullTransaction=" + isFullTransaction +
                ", isPartialTransaction=" + isPartialTransaction +
                ", isReadyForApproval=" + isReadyForApproval +
                ", isCancelled=" + isCancelled +
                ", remainingUnit=" + remainingUnit +
                ", sellerUnitBefore=" + sellerUnitBefore +
                ", sellerUnitAfter=" + sellerUnitAfter +
                ", buyerUnitBefore=" + buyerUnitBefore +
                ", buyerUnitAfter=" + buyerUnitAfter +
                ", suspensionReason='" + suspensionReason + '\'' +
                ", buyerChn='" + buyerChn + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerAccountNumber=" + buyerAccountNumber +
                ", isAddAccountRequired=" + isAddAccountRequired +
                ", status='" + status + '\'' +
                ", processedOn=" + processedOn +
                ", suspendedOn=" + suspendedOn +
                ", transaction=" + transaction +
                ", supportAccountData=" + supportAccountData +
                '}';
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
