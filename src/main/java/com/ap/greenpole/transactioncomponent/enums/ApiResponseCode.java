package com.ap.greenpole.transactioncomponent.enums;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 8/11/2020 11:13 PM
 */
public enum ApiResponseCode {


    SUCCESS("00", "SUCCESSFUL"), FAILED("01", "FAILED"), ERROR_PROCESSING_REQUEST("01", "ERROR PROCESSING REQUEST"),
    TRANSACTION_NOT_BALANCE("01", "TRANSACTION FILE NOT BALANCE"), TRANSACTION_BALANCE("01", "TRANSACTION FILE IS BALANCE"),
    SUCCESS_UPLOAD("00", "UPLOADED SUCCESSFULLY"), PROCESSING_REQUEST("00", "PROCESSING REQUEST"), RECORD_FOUND("11", "RECORD FOUND"),
    RECORD_NOT_FOUND("10", "RECORD NOT FOUND"), ANALYSIS_OK("22", "ANALYSIS OK"), ANALYSIS_NOT_OK("20", "ANALYSIS NOT OK"),
    FILE_VALIDATION("01", "VALIDATING FILE"), ERROR_WRITING_FILE_TO_SYSTEM("01", "Could not write File to System");

    private String code;

    private String description;


    ApiResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

}
