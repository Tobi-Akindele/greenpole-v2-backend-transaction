package com.ap.greenpole.transactioncomponent.util;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:47 PM
 */
public interface ApplicationConstant {

    String[] REQUEST_TYPES = { "CREATE_TRANSACTION_COMPONENT", "RECONCILE_SUSPENDED_TRANS", "CREATE_PROCESS_TRANSACTION", "RECONCILE_SUSPENDED_MANUAL"};
    String[] APPROVAL_ACTIONS = { "Accepted", "Rejected" };
    String[] RECONILIATION_ACTIONS = { "Reconcile", "Cancel" };
    String[] APPROVAL_STATUS = { "", "Pending", "Rejected", "Accepted" };
    String[] FILE_STORAGE_PATHS = { "TEMP_DIR", "PMT_DIR" };
    String DATE_FORMATE = "dd-MM-yyyy hh:mm:ss";
    String DOB_FORMATE = "yyyy-MM-dd";
    String TRANSACTION_FILE_FORMATE = "yyyyMMdd";
    String REQUEST_DATE_FORMATE = "yyyy-MM-dd HH:mm:ss.SSS";
    String BASE_CONTEXT_URL = "/api/v1/transaction";
    String MODULE = "TRANSACTION_COMPONENT";
    String MODULE_APPROVAL_PERMISSION = "TRANSACTION_COMPONENT_APPROVAL";
    String FILTER_DATE_FORMAT = "yyyy-MM-dd";
    String AUTHORIZATION = "Authorization";
    String[] TYPES = {"SENT", "RECEIVED"};
    String LESS_UNIT_TO_SETTLE_BUYER = "Seller Account Has Less Unit To Settle Buyer";
    String SELLER_HAS_ZERO_UNIT_IN_SYSTEM = "Seller Account Has Zero Unit In System";
    String SHAREHOLDER_HAS_ESOP_STATUS = "ShareHolder Has ESOP Status";
    String SELLER_NOT_IN_SYSTEM = "Seller Does Not Exist System";

}
