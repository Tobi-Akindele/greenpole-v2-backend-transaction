package com.ap.greenpole.transactioncomponent.enums;

public enum ApprovalStatus {
    PENDING(1), REJECTED(2), ACCEPTED(3);

    int code;

    ApprovalStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
