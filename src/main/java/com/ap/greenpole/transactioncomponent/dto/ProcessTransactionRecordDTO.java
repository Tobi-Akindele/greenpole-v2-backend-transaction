package com.ap.greenpole.transactioncomponent.dto;

import com.ap.greenpole.transactioncomponent.entity.ProcessedTransaction;

import java.util.List;

public class ProcessTransactionRecordDTO {

    private  long transactionRecordId;
    private String sellerChn;
    private String sellerAccountNumber;
    private String sellerCscsName;
    private String sellerSystemName;
    private long sellerHoldingUnit;
    private List<Buyer> buyers;

    public String getSellerChn() {
        return sellerChn;
    }

    public void setSellerChn(String sellerChn) {
        this.sellerChn = sellerChn;
    }

    public String getSellerAccountNumber() {
        return sellerAccountNumber;
    }

    public void setSellerAccountNumber(String sellerAccountNumber) {
        this.sellerAccountNumber = sellerAccountNumber;
    }

    public String getSellerCscsName() {
        return sellerCscsName;
    }

    public void setSellerCscsName(String sellerCscsName) {
        this.sellerCscsName = sellerCscsName;
    }

    public long getSellerHoldingUnit() {
        return sellerHoldingUnit;
    }

    public void setSellerHoldingUnit(long sellerHoldingUnit) {
        this.sellerHoldingUnit = sellerHoldingUnit;
    }

    public String getSellerSystemName() {
        return sellerSystemName;
    }

    public void setSellerSystemName(String sellerSystemName) {
        this.sellerSystemName = sellerSystemName;
    }

    public long getTransactionRecordId() {
        return transactionRecordId;
    }

    public void setTransactionRecordId(long transactionRecordId) {
        this.transactionRecordId = transactionRecordId;
    }

    public List<Buyer> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<Buyer> buyers) {
        this.buyers = buyers;
    }

    @Override
    public String toString() {
        return "ProcessTransactionRecordDTO{" +
                "transactionRecordId=" + transactionRecordId +
                ", sellerChn='" + sellerChn + '\'' +
                ", sellerAccountNumber='" + sellerAccountNumber + '\'' +
                ", sellerCscsName='" + sellerCscsName + '\'' +
                ", sellerSystemName='" + sellerSystemName + '\'' +
                ", sellerHoldingUnit='" + sellerHoldingUnit + '\'' +
                ", buyers=" + buyers +
                '}';
    }


    public class Buyer {
        private String buyerChn;
        private String buyerCscsName;
        private String buyerSystemName;
        private String sellerTransactionid;
        private String buyerTransactionId;
        private Long buyerHoldingUnit;
        private ProcessedTransaction processedTransaction;

        public String getBuyerChn() {
            return buyerChn;
        }

        public void setBuyerChn(String buyerChn) {
            this.buyerChn = buyerChn;
        }

        public String getBuyerCscsName() {
            return buyerCscsName;
        }

        public void setBuyerCscsName(String buyerCscsName) {
            this.buyerCscsName = buyerCscsName;
        }

        public String getBuyerSystemName() {
            return buyerSystemName;
        }

        public void setBuyerSystemName(String buyerSystemName) {
            this.buyerSystemName = buyerSystemName;
        }

        public String getSellerTransactionid() {
            return sellerTransactionid;
        }

        public void setSellerTransactionid(String sellerTransactionid) {
            this.sellerTransactionid = sellerTransactionid;
        }

        public String getBuyerTransactionId() {
            return buyerTransactionId;
        }

        public void setBuyerTransactionId(String buyertransactionId) {
            this.buyerTransactionId = buyertransactionId;
        }

        public ProcessedTransaction getProcessedTransaction() {
            return processedTransaction;
        }

        public void setProcessedTransaction(ProcessedTransaction processedTransaction) {
            this.processedTransaction = processedTransaction;
        }

        public Long getBuyerHoldingUnit() {
            return buyerHoldingUnit;
        }

        public void setBuyerHoldingUnit(Long buyerHoldingUnit) {
            this.buyerHoldingUnit = buyerHoldingUnit;
        }

        @Override
        public String toString() {
            return "Buyer{" +
                    "buyerChn='" + buyerChn + '\'' +
                    ", buyerCscsName='" + buyerCscsName + '\'' +
                    ", buyerSystemName='" + buyerSystemName + '\'' +
                    ", sellerTransactionid='" + sellerTransactionid + '\'' +
                    ", buyerTransactionId='" + buyerTransactionId + '\'' +
                    ", buyerHoldingUnit='" + buyerHoldingUnit + '\'' +
                    ", processedTransaction=" + processedTransaction +
                    '}';
        }
    }
}
