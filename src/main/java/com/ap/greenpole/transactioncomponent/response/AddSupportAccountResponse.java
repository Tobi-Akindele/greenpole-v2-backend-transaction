package com.ap.greenpole.transactioncomponent.response;

public class AddSupportAccountResponse {

    private  String responseCode;
    private  String responseMessage;
    private  String responseData;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "AddSupportAccountResponse{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", responseData='" + responseData + '\'' +
                '}';
    }
}
