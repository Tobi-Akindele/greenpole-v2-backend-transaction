package com.ap.greenpole.transactioncomponent.entity;

import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_temp_shareholder")
public class ShareHolderTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shareholder_id")
    private long shareHolderId;

    private String email, phone, gender, occupation, address, city, country, relationship, rin, nuban, bvn, status;

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

    @JsonProperty("kin_email")
    @Column(name = "kin_email")
    private String kinEmail;

    @JsonProperty("kin_name")
    @Column(name = "kin_name")
    private String kinName;

    @JsonProperty("kin_phone")
    @Column(name = "kin_phone")
    private String kinPhone;

    @JsonProperty("kin_address")
    @Column(name = "kin_address")
    private String kinAddress;

    @JsonProperty("kin_country")
    @Column(name = "kin_country")
    private String kinCountry;

    @JsonProperty("kin_state")
    @Column(name = "kin_state")
    private String kinState;

    @JsonProperty("kin_lga")
    @Column(name = "kin_lga")
    private String kinLga;

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

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cscs_transaction_request_id")
    @JsonIgnore
    private TransactionRequest transactionRequest;


    public long getShareHolderId() {
        return shareHolderId;
    }

    public void setShareHolderId(long shareHolderId) {
        this.shareHolderId = shareHolderId;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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

    public String getKinEmail() {
        return kinEmail;
    }

    public void setKinEmail(String kinEmail) {
        this.kinEmail = kinEmail;
    }

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getKinPhone() {
        return kinPhone;
    }

    public void setKinPhone(String kinPhone) {
        this.kinPhone = kinPhone;
    }

    public String getKinAddress() {
        return kinAddress;
    }

    public void setKinAddress(String kinAddress) {
        this.kinAddress = kinAddress;
    }

    public String getKinCountry() {
        return kinCountry;
    }

    public void setKinCountry(String kinCountry) {
        this.kinCountry = kinCountry;
    }

    public String getKinState() {
        return kinState;
    }

    public void setKinState(String kinState) {
        this.kinState = kinState;
    }

    public String getKinLga() {
        return kinLga;
    }

    public void setKinLga(String kinLga) {
        this.kinLga = kinLga;
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

    @Override
    public String toString() {
        return "ShareHolder{" +
                "shareHolderId=" + shareHolderId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", occupation='" + occupation + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", relationship='" + relationship + '\'' +
                ", rin='" + rin + '\'' +
                ", nuban='" + nuban + '\'' +
                ", bvn='" + bvn + '\'' +
                ", status='" + status + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", stateOfOrigin='" + stateOfOrigin + '\'' +
                ", marriageCertificateNumber='" + marriageCertificateNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", lgaOfOrigin='" + lgaOfOrigin + '\'' +
                ", kinEmail='" + kinEmail + '\'' +
                ", kinName='" + kinName + '\'' +
                ", kinPhone='" + kinPhone + '\'' +
                ", kinAddress='" + kinAddress + '\'' +
                ", kinCountry='" + kinCountry + '\'' +
                ", kinState='" + kinState + '\'' +
                ", kinLga='" + kinLga + '\'' +
                ", shareholderType='" + shareholderType + '\'' +
                ", clientCompany=" + clientCompany +
                ", stockBroker=" + stockBroker +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", clearingHousingNumber='" + clearingHousingNumber + '\'' +
                ", esopStatus='" + esopStatus + '\'' +
                ", shareUnit=" + shareUnit +
                ", createdOn=" + createdOn +
                ", dob=" + dob +
                ", taxExemption=" + taxExemption +
                ", registrarMandated=" + registrarMandated +
                '}';
    }
}
