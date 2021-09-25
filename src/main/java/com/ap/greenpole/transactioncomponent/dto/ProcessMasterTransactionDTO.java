package com.ap.greenpole.transactioncomponent.dto;

public class ProcessMasterTransactionDTO {

    private int mixMatchLastNameCount;
    private int mixMatchFirstNameCount;
    private int mixMatchMiddleNameCount;
    private int mixMatchAddress1Count;
    private int mixMatchAddress2Count;
    private int mixMatchAddress3Count;
    private int invalidStateSetCount;
    private int chnNotInSystemCount;


    public int getMixMatchLastNameCount() {
        return mixMatchLastNameCount;
    }

    public void setMixMatchLastNameCount(int mixMatchLastNameCount) {
        this.mixMatchLastNameCount = mixMatchLastNameCount;
    }

    public int getMixMatchFirstNameCount() {
        return mixMatchFirstNameCount;
    }

    public void setMixMatchFirstNameCount(int mixMatchFirstNameCount) {
        this.mixMatchFirstNameCount = mixMatchFirstNameCount;
    }

    public int getMixMatchMiddleNameCount() {
        return mixMatchMiddleNameCount;
    }

    public void setMixMatchMiddleNameCount(int mixMatchMiddleNameCount) {
        this.mixMatchMiddleNameCount = mixMatchMiddleNameCount;
    }

    public int getMixMatchAddress1Count() {
        return mixMatchAddress1Count;
    }

    public void setMixMatchAddress1Count(int mixMatchAddress1Count) {
        this.mixMatchAddress1Count = mixMatchAddress1Count;
    }

    public int getMixMatchAddress2Count() {
        return mixMatchAddress2Count;
    }

    public void setMixMatchAddress2Count(int mixMatchAddress2Count) {
        this.mixMatchAddress2Count = mixMatchAddress2Count;
    }

    public int getMixMatchAddress3Count() {
        return mixMatchAddress3Count;
    }

    public void setMixMatchAddress3Count(int mixMatchAddress3Count) {
        this.mixMatchAddress3Count = mixMatchAddress3Count;
    }

    public int getInvalidStateSetCount() {
        return invalidStateSetCount;
    }

    public void setInvalidStateSetCount(int invalidStateSetCount) {
        this.invalidStateSetCount = invalidStateSetCount;
    }

    public int getChnNotInSystemCount() {
        return chnNotInSystemCount;
    }

    public void setChnNotInSystemCount(int chnNotInSystemCount) {
        this.chnNotInSystemCount = chnNotInSystemCount;
    }

    @Override
    public String toString() {
        return "ProcessMasterTransactionDTO{" +
                "mixMatchLastNameCount=" + mixMatchLastNameCount +
                ", mixMatchFirstNameCount=" + mixMatchFirstNameCount +
                ", mixMatchMiddleNameCount=" + mixMatchMiddleNameCount +
                ", mixMatchAddress1Count=" + mixMatchAddress1Count +
                ", mixMatchAddress2Count=" + mixMatchAddress2Count +
                ", mixMatchAddress3Count=" + mixMatchAddress3Count +
                ", invalidStateSetCount=" + invalidStateSetCount +
                ", chnNotInSystemCount=" + chnNotInSystemCount +
                '}';
    }
}
