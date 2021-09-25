package com.ap.greenpole.transactioncomponent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:37 AM
 */

@Entity
@Table(name = "cscs_master")
public class TransactionMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String CHN;
    private String names;
    private String address1;
    private String address2;
    private String address3;
    private String country;
    private String bankName;
    private String structure;
    private String bankAccountNumber;
    private String bvn;
    private String emailAddress;
    private String phoneNumber;
    private String nextOfKinOrMaiden;

    @ManyToOne
    @JoinColumn(name = "cscs_transaction_request_id")
    @JsonIgnore
    private TransactionRequest transactionRequest;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCHN() {
        return CHN;
    }

    public void setCHN(String CHN) {
        this.CHN = CHN;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNextOfKinOrMaiden() {
        return nextOfKinOrMaiden;
    }

    public void setNextOfKinOrMaiden(String nextOfKinOrMaiden) {
        this.nextOfKinOrMaiden = nextOfKinOrMaiden;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    @Override
    public String toString() {
        return "TransactionMaster{" +
                "id=" + id +
                ", CHN='" + CHN + '\'' +
                ", names='" + names + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", country='" + country + '\'' +
                ", bankName='" + bankName + '\'' +
                ", structure='" + structure + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bvn='" + bvn + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nextOfKinOrMaiden='" + nextOfKinOrMaiden + '\'' +
                '}';
    }
}
