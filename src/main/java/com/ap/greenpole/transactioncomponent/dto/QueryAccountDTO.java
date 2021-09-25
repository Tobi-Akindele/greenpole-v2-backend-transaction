package com.ap.greenpole.transactioncomponent.dto;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 9:37 PM
 */
public class QueryAccountDTO {

    private long clientCompany;

    private String chn;

    private long acountNumber;

    private String firstName;

    private String lastName;

    private String middleName;

    private Long unit;

    private String status;

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public long getAcountNumber() {
        return acountNumber;
    }

    public void setAcountNumber(long acountNumber) {
        this.acountNumber = acountNumber;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }


    public long getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(long clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QueryAccountDTO{" +
                "clientCompany=" + clientCompany +
                ", chn='" + chn + '\'' +
                ", acountNumber=" + acountNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", unit=" + unit +
                ", status='" + status + '\'' +
                '}';
    }
}
