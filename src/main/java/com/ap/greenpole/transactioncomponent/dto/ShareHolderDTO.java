package com.ap.greenpole.transactioncomponent.dto;

import com.ap.greenpole.transactioncomponent.entity.Custodian;
import com.ap.greenpole.transactioncomponent.entity.Holderkin;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


public class ShareHolderDTO {

    private long shareHolderId;

    private String nin, email, phone, gender, occupation, address, city, country, rin, nuban, bvn, status, religious;

    private String firstName;


    private String middleName;

    private String lastName;

    private String maritalStatus;

    private String accountNumber;

    private String bankAccountName;

    private String stateOfOrigin;

    private String marriageCertificateNumber;

    private String postalCode;

    private String lgaOfOrigin;

    private String shareholderType;

    private long clientCompany;

    private long stockBroker;

    private String bankName;

    private String bankAccount;

    private String clearingHousingNumber;

    private String esopStatus;

    private long shareUnit;

    private Date createdOn;

    private Date dob;

    boolean taxExemption;

    boolean registrarMandated;

    public long getShareHolderId() {
        return shareHolderId;
    }

    public void setShareHolderId(long shareHolderId) {
        this.shareHolderId = shareHolderId;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRin() {
        return rin;
    }

    public void setRin(String rin) {
        this.rin = rin;
    }

    public String getNuban() {
        return nuban;
    }

    public void setNuban(String nuban) {
        this.nuban = nuban;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReligious() {
        return religious;
    }

    public void setReligious(String religious) {
        this.religious = religious;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getMarriageCertificateNumber() {
        return marriageCertificateNumber;
    }

    public void setMarriageCertificateNumber(String marriageCertificateNumber) {
        this.marriageCertificateNumber = marriageCertificateNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getShareholderType() {
        return shareholderType;
    }

    public void setShareholderType(String shareholderType) {
        this.shareholderType = shareholderType;
    }

    public long getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(long clientCompany) {
        this.clientCompany = clientCompany;
    }

    public long getStockBroker() {
        return stockBroker;
    }

    public void setStockBroker(long stockBroker) {
        this.stockBroker = stockBroker;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getClearingHousingNumber() {
        return clearingHousingNumber;
    }

    public void setClearingHousingNumber(String clearingHousingNumber) {
        this.clearingHousingNumber = clearingHousingNumber;
    }

    public String getEsopStatus() {
        return esopStatus;
    }

    public void setEsopStatus(String esopStatus) {
        this.esopStatus = esopStatus;
    }

    public long getShareUnit() {
        return shareUnit;
    }

    public void setShareUnit(long shareUnit) {
        this.shareUnit = shareUnit;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public boolean isTaxExemption() {
        return taxExemption;
    }

    public void setTaxExemption(boolean taxExemption) {
        this.taxExemption = taxExemption;
    }

    public boolean isRegistrarMandated() {
        return registrarMandated;
    }

    public void setRegistrarMandated(boolean registrarMandated) {
        this.registrarMandated = registrarMandated;
    }
}