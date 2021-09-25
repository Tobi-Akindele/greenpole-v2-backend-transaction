package com.ap.greenpole.transactioncomponent.enums;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 8/11/2020 11:13 PM
 */
public enum ProcessedTransactResponse {


    SELLER_NOT_IN_SYSTEM(100, "Number of sellers that don't exit on system"), BUYER_NOT_IN_SYSTEM(200, "Number of buys that don't exit on system"),
    SELLER_WITH_INSUFFICIENT_BAL(300, "Number of sellers with insuficient balance to carry out transaction"),
    MASTERFILE_RECORD_DOES_NOT_MATCH_SYSTEM(400, "Number of master file records with names that don't match system records"),
    HOLDER_WITHOUT_SET_STATE(500, "Number of non-existent holder records without a set state"),
    INVALID_STATE_AGAINST_MASTERFILE(600, "Number of invalid states set against master file records"),
    MULTIPLE_CHN_IN_SAME_COMPANY(700, "Number of holders with multiple chn accounts under the same company");

    private int code;

    private String description;


    ProcessedTransactResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

}
