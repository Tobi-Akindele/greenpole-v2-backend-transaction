package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/14/2020 11:41 PM
 */
public class Seller {

    private String CHN;

    private String Name;

    private long totalUnitPurchased;

    private long totalUnitSold;

    private long unitVariance;

    private List<Buyer> buyerList;

    public String getCHN() {
        return CHN;
    }

    public void setCHN(String CHN) {
        this.CHN = CHN;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getTotalUnitPurchased() {
        return totalUnitPurchased;
    }

    public void setTotalUnitPurchased(long totalUnitPurchased) {
        this.totalUnitPurchased = totalUnitPurchased;
    }

    public long getTotalUnitSold() {
        return totalUnitSold;
    }

    public void setTotalUnitSold(long totalUnitSold) {
        this.totalUnitSold = totalUnitSold;
    }

    public long getUnitVariance() {
        return unitVariance;
    }

    public void setUnitVariance(long unitVariance) {
        this.unitVariance = unitVariance;
    }

    public List<Buyer> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<Buyer> buyerList) {
        this.buyerList = buyerList;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "CHN='" + CHN + '\'' +
                ", Name='" + Name + '\'' +
                ", totalUnitPurchased=" + totalUnitPurchased +
                ", totalUnitSold=" + totalUnitSold +
                ", unitVariance=" + unitVariance +
                '}';
    }
}
