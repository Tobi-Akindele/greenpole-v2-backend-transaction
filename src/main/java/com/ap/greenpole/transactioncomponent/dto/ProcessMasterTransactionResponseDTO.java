package com.ap.greenpole.transactioncomponent.dto;

public class ProcessMasterTransactionResponseDTO {

    private  long masterRecordId;
    private  String cscsChn;
    private  String cscsLastName;
    private  String cscsFirstName;
    private  String cscsMiddleName;
    private  String cscsAddress1;
    private  String cscsAdress2;
    private  String cscsAddress3;
    private  String systemLastName;
    private  String systemFirstName;
    private  String systemMiddleName;
    private  String systemAddress1;
    private  String systemState;
    private  String type;
    private  String errorCode;
    private  String state;

    public String getCscsChn() {
        return cscsChn;
    }

    public void setCscsChn(String cscsChn) {
        this.cscsChn = cscsChn;
    }

    public String getCscsLastName() {
        return cscsLastName;
    }

    public void setCscsLastName(String cscsLastName) {
        this.cscsLastName = cscsLastName;
    }

    public String getCscsFirstName() {
        return cscsFirstName;
    }

    public void setCscsFirstName(String cscsFirstName) {
        this.cscsFirstName = cscsFirstName;
    }

    public String getCscsMiddleName() {
        return cscsMiddleName;
    }

    public void setCscsMiddleName(String cscsMiddleName) {
        this.cscsMiddleName = cscsMiddleName;
    }

    public String getCscsAddress1() {
        return cscsAddress1;
    }

    public void setCscsAddress1(String cscsAddress1) {
        this.cscsAddress1 = cscsAddress1;
    }

    public String getCscsAdress2() {
        return cscsAdress2;
    }

    public void setCscsAdress2(String cscsAdress2) {
        this.cscsAdress2 = cscsAdress2;
    }

    public String getCscsAddress3() {
        return cscsAddress3;
    }

    public void setCscsAddress3(String cscsAddress3) {
        this.cscsAddress3 = cscsAddress3;
    }

    public String getSystemLastName() {
        return systemLastName;
    }

    public void setSystemLastName(String systemLastName) {
        this.systemLastName = systemLastName;
    }

    public String getSystemFirstName() {
        return systemFirstName;
    }

    public void setSystemFirstName(String systemFirstName) {
        this.systemFirstName = systemFirstName;
    }

    public String getSystemMiddleName() {
        return systemMiddleName;
    }

    public void setSystemMiddleName(String systemMiddleName) {
        this.systemMiddleName = systemMiddleName;
    }

    public String getSystemAddress1() {
        return systemAddress1;
    }

    public void setSystemAddress1(String systemAddress1) {
        this.systemAddress1 = systemAddress1;
    }

    public String getSystemState() {
        return systemState;
    }

    public void setSystemState(String systemState) {
        this.systemState = systemState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMasterRecordId() {
        return masterRecordId;
    }

    public void setMasterRecordId(long masterRecordId) {
        this.masterRecordId = masterRecordId;
    }

    @Override
    public String toString() {
        return "ProcessMasterTransactionResponseDTO{" +
                "masterRecordId=" + masterRecordId +
                ", cscsChn='" + cscsChn + '\'' +
                ", cscsLastName='" + cscsLastName + '\'' +
                ", cscsFirstName='" + cscsFirstName + '\'' +
                ", cscsMiddleName='" + cscsMiddleName + '\'' +
                ", cscsAddress1='" + cscsAddress1 + '\'' +
                ", cscsAdress2='" + cscsAdress2 + '\'' +
                ", cscsAddress3='" + cscsAddress3 + '\'' +
                ", systemLastName='" + systemLastName + '\'' +
                ", systemFirstName='" + systemFirstName + '\'' +
                ", systemMiddleName='" + systemMiddleName + '\'' +
                ", systemAddress1='" + systemAddress1 + '\'' +
                ", systemState='" + systemState + '\'' +
                ", type='" + type + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
