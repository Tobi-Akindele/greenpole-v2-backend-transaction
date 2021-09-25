package com.ap.greenpole.transactioncomponent.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ap.greenpole.transactioncomponent.dto.CancelledSuspendedTransactionsDto;
import com.ap.greenpole.transactioncomponent.dto.FilterTransactionDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransAuthorizeDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransManualDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ReconciliationService;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class ReconciliationController {

    final static Logger logger = LoggerFactory.getLogger(ReconciliationController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private UserService userService;


    @GetMapping(value = "/reconcile/suspendedtransaction/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_QUERY_RECONCILE_TRANSACTION"})
    public DefaultResponse<List<FilterTransactionDTO>> getSuspendedTransactionForReconciliation(
            @RequestParam(value = "client_company_name", required = false, defaultValue = "-0") String clientCompanyName,
            @RequestParam(value = "client_company_code", required = false, defaultValue = "-0") String clientCompanyCode,
            @RequestParam(value = "from_shareholder_name", required = false, defaultValue = "-0") String fromShareHolderName,
            @RequestParam(value = "to_shareholder_name", required = false, defaultValue = "-0") String toShareHolderName,
            @RequestParam(value = "from_shareholder_chn", required = false, defaultValue = "-0") String fromShareHolderChn,
            @RequestParam(value = "to_shareholder_chn", required = false, defaultValue = "-0") String toShareHolderChn,
            @RequestParam(value = "buyer_cscs_transactionid", required = false, defaultValue = "-0") String buyerCSCSTransactionId,
            @RequestParam(value = "seller_cscs_transactionid", required = false, defaultValue = "-0") String sellerCSCSTransactionId,
            @RequestParam(value = "from_shareholder_company_account", required = false, defaultValue = "-0") String fromShareHolderCompanyAccount,
            @RequestParam(value = "to_shareholder_company_account", required = false, defaultValue = "-0") String toShareHolderCompanyAccount) {
        DefaultResponse<List<FilterTransactionDTO>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
            Map<String, Object> queryData = new HashMap<>();
            queryData.put("client_company_name", clientCompanyName);
            queryData.put("client_company_code", clientCompanyCode);
            queryData.put("from_shareholder_name", fromShareHolderName);
            queryData.put("to_shareholder_name", toShareHolderName);
            queryData.put("from_shareholder_chn", fromShareHolderChn);
            queryData.put("to_shareholder_chn", toShareHolderChn);
            queryData.put("buyer_cscs_transactionid", buyerCSCSTransactionId);
            queryData.put("seller_cscs_transactionid", sellerCSCSTransactionId);
            queryData.put("from_shareholder_company_account", fromShareHolderCompanyAccount);
            queryData.put("to_shareholder_company_account", toShareHolderCompanyAccount);
            List<ReconcileSuspendedTransDTO> allTransaction = reconciliationService.getSuspendedTransactionForReconciliation(queryData);
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (allTransaction != null && !allTransaction.isEmpty()) {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_FOUND.getDescription());
                defaultResponse.setData(allTransaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while getAllTransactionForProcessing {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }

    @PostMapping(value = "/reconcile/suspendedtransaction/request", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_QUERY_RECONCILE_TRANSACTION"})
    public DefaultResponse<?> reconcileSuspendedTransactionRequest(
            @RequestBody ReconcileSuspendedTransAuthorizeDTO reconcileSuspendedTransDTO, HttpServletRequest request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
        if (!user.isPresent()) {
            defaultResponse.setStatusMessage("User details not found");
            return defaultResponse;
        }

        try {
            ModuleRequest moduleRequest = reconciliationService.reconcileSuspendedTransactionRequest(reconcileSuspendedTransDTO, user.get());
            if (moduleRequest != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            }
            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @PostMapping(value = "/reconcile/suspendedtransaction/approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> authorizeRequest(
            @RequestBody RequestAuthorizationDTO requestAuthorization, HttpServletRequest request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
            if (!user.isPresent()) {
                defaultResponse.setStatusMessage("User details not found");
                return defaultResponse;
            }

            reconciliationService.authorizeReconciliationSuspendedTransactionRequest(requestAuthorization, user.get());
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());

            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @PostMapping(value = "/reconcile/suspendedtransaction/manual/request", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_QUERY_RECONCILE_TRANSACTION"})
    public DefaultResponse<?> reconcileSuspendedTransactionManualRequest(
            @RequestBody ReconcileSuspendedTransManualDTO reconcileSuspendedTransDTO, HttpServletRequest request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
            if (!user.isPresent()) {
                defaultResponse.setStatusMessage("User details not found");
                return defaultResponse;
            }
            ModuleRequest moduleRequest = reconciliationService.reconcileSuspendedTransactionManualRequest(reconcileSuspendedTransDTO, user.get());
            if (moduleRequest != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            }
            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @PostMapping(value = "/reconcile/suspendedtransaction/manual/approval", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorizePermission({"PERMISSION_TRANSACTION_COMPONENT_APPROVAL"})
    public DefaultResponse<?> authorizeManualRequest(
            @RequestBody RequestAuthorizationDTO requestAuthorization, HttpServletRequest request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
            if (!user.isPresent()) {
                defaultResponse.setStatusMessage("User details not found");
                return defaultResponse;
            }
            reconciliationService.authorizeReconciliationSuspendedTransactionManualRequest(requestAuthorization, user.get());
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());

            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @PostMapping(value = "/cancel-reverse/suspendedtransactions", produces = MediaType.APPLICATION_JSON_VALUE)
//  @PreAuthorizePermission({"PERMISSION_TRANSACTION_COMPONENT_APPROVAL"})
    public DefaultResponse<?> cancelSuspendedTransactions(@RequestHeader(value = "cancelOrReverseOption", required = false) boolean action,
            @RequestBody List<String> cscsTransactionIds, HttpServletRequest request) {

        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {

            if (cscsTransactionIds == null || cscsTransactionIds.isEmpty()) {
                defaultResponse.setStatusMessage("CSCS Transaction IDs required");
                return defaultResponse;
            }
            Object cancelledSuspendedTransactions = reconciliationService.cancelSuspendedTransactions(cscsTransactionIds, action);
            if (cancelledSuspendedTransactions != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setData(cancelledSuspendedTransactions);
            }
            return defaultResponse;
        } catch (Exception ex) {
            logger.info("[-] Exception occurred while Cancelling suspended transactions {}", ex.getMessage());
            return defaultResponse;
        }
    }

    @GetMapping(value = "/cancelled/suspendedtransactions/query", produces = MediaType.APPLICATION_JSON_VALUE)
//  @PreAuthorizePermission({"PERMISSION_ANALYSE_TRANSACTION"})
    public DefaultResponse<List<CancelledSuspendedTransactionsDto>> queryCancelledSuspendedTransactions(
            @RequestParam(value = "client_company", required = false) String clientCompany,
            @RequestParam(value = "transaction_date", required = false) String transactionDate ) throws Exception {
        DefaultResponse<List<CancelledSuspendedTransactionsDto>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
            Map<String, Object> queryData = new HashMap<>();
            queryData.put("client_company", clientCompany);
            queryData.put("transaction_date", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, transactionDate));

            List<CancelledSuspendedTransactionsDto> allTransaction = reconciliationService.searchCancelledSuspendedTransactions(queryData);
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (allTransaction != null && !allTransaction.isEmpty()) {
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(allTransaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while queryCancelledSuspendedTransactions {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }
}
