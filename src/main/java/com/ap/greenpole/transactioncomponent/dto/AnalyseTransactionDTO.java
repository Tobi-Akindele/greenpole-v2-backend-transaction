package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/14/2020 10:38 PM
 */
public class AnalyseTransactionDTO {

    private long totalUnitPurchased;

    private long totalUnitSold;

    private long unitVariance;

    private List<Seller> sellers;

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

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    @Override
    public String toString() {
        return "AnalyseTransactionDTO{" +
                "totalUnitPurchased=" + totalUnitPurchased +
                ", totalUnitSold=" + totalUnitSold +
                ", unitVariance=" + unitVariance +
                ", sellers=" + sellers +
                '}';
    }
}
