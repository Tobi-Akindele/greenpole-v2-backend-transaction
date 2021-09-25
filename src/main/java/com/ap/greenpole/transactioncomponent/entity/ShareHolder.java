package com.ap.greenpole.transactioncomponent.entity;

import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Shareholder")
public class ShareHolder {

    public long getShareHolderId() {
        return shareHolderId;
    }

    public void setShareHolderId(long shareHolderId) {
        this.shareHolderId = shareHolderId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("shareholder_id")
    @Column(name = "shareholder_id")
    private long shareHolderId;

    private String nin, email, phone, gender, occupation, address, city, country, rin, nuban, bvn, status, religious;

    boolean minor;
    @JsonProperty("first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonProperty("middle_name")
    @Column(name = "middle_name")
    private String middleName;

    @JsonProperty("last_name")
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty("marital_status")
    @Column(name = "marital_status")
    private String maritalStatus;

    @JsonProperty("account_number")
    @Column(name = "account_number")
    private String accountNumber;

    @JsonProperty("bank_account_name")
    @Column(name = "bank_account_name")
    private String bankAccountName;

    @JsonProperty("state_of_origin")
    @Column(name = "state_of_origin")
    private String stateOfOrigin;

    @JsonProperty("marriage_certificate_number")
    @Column(name = "marriage_certificate_number")
    private String marriageCertificateNumber;

    @JsonProperty("postal_code")
    @Column(name = "postal_code")
    private String postalCode;

    @JsonProperty("lga_of_origin")
    @Column(name = "lga_of_origin")
    private String lgaOfOrigin;

    @JsonProperty("shareholder_type")
    @Column(name = "shareholder_type")
    private String shareholderType;

    @JsonProperty("client_company")
    @Column(name = "client_company")
    private long clientCompany;

    @JsonProperty("stock_broker")
    @Column(name = "stock_broker")
    private long stockBroker;

    @JsonProperty("bank_name")
    @Column(name = "bank_name")
    private String bankName;

    @JsonProperty("bank_account")
    @Column(name = "bank_account")
    private String bankAccount;

    @JsonProperty("clearing_housing_number")
    @Column(name = "clearing_housing_number")
    private String clearingHousingNumber;

    @JsonProperty("esop_status")
    @Column(name = "esop_status")
    private String esopStatus;

    @JsonProperty("share_unit")
    @Column(name = "share_unit")
    private long shareUnit;

    @DateTimeFormat(pattern= ApplicationConstant.DATE_FORMATE)
    @JsonProperty("created_on")
    @Column(name = "created_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ApplicationConstant.DATE_FORMATE)
    private Date createdOn;

    @DateTimeFormat(pattern = ApplicationConstant.DOB_FORMATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ApplicationConstant.DOB_FORMATE)
    private Date dob;

    @JsonProperty("tax_exemption")
    @Column(name = "tax_exemption")
    boolean taxExemption;

    @JsonProperty("registrar_mandated")
    @Column(name = "registrar_mandated")
    boolean registrarMandated;

    @OneToOne(mappedBy="shareHolder", cascade=CascadeType.ALL)
    Holderkin holderkin;

    @OneToOne(mappedBy="shareHolder", cascade=CascadeType.ALL)
    Custodian custodian;

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public Custodian getCustodian() {
        return custodian;
    }

    public void setCustodian(Custodian custodian) {
        this.custodian = custodian;
    }

    public Holderkin getHolderkin() {
        return holderkin;
    }

    public void setHolderkin(Holderkin holderkin) {
        this.holderkin = holderkin;
    }

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public String getReligious() {
        return religious;
    }

    public void setReligious(String religious) {
        this.religious = religious;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getMarriageCertificateNumber() {
        return marriageCertificateNumber;
    }

    public void setMarriageCertificateNumber(String marriageCertificateNumber) {
        this.marriageCertificateNumber = marriageCertificateNumber;
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

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
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
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {

        try{
            this.dob = dob;

        }
        catch (Exception ex){
            this.dob = null;
        }

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getTaxExemption() {
        return taxExemption;
    }

    public void setTaxExemption(boolean taxExemption) {
        this.taxExemption = taxExemption;
    }

    public boolean getRegistrarMandated() {
        return registrarMandated;
    }

    public void setRegistrarMandated(boolean registrarMandated) {
        this.registrarMandated = registrarMandated;
    }
}
