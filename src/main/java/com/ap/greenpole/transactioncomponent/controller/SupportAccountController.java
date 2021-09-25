package com.ap.greenpole.transactioncomponent.controller;

import com.ap.greenpole.transactioncomponent.dto.*;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.AddSupportAccountResponse;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ReconciliationService;
import com.ap.greenpole.transactioncomponent.service.SupportAccountService;
import com.ap.greenpole.transactioncomponent.service.TransactionRequestService;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class SupportAccountController {

    final static Logger logger = LoggerFactory.getLogger(SupportAccountController.class);

    @Autowired
    private TransactionRequestService transactionRequestService;

    @Autowired
    private SupportAccountService supportAccountService;

    @Autowired
    private ReconciliationService reconciliationService;
    
    @Autowired
    private UserService userService;

    @GetMapping(value = "/account/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<List<QueryAccountDTO>> queryAccount(
            @RequestParam(value = "chn") String chn,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "account_number") String accountNumber,
            @RequestParam(value = "old_account_number", required = false) String oldAccountNumber) {
        DefaultResponse<List<QueryAccountDTO>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Map<String, Object> queryData = new HashMap<>();
            queryData.put("chn", chn);
            queryData.put("name", name);
            queryData.put("accountNumber", accountNumber);
            queryData.put("oldAccountNumber", oldAccountNumber);
            List<QueryAccountDTO> filtedAccounts = transactionRequestService.queryAccount(queryData);
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (filtedAccounts != null && !filtedAccounts.isEmpty()) {
                defaultResponse.setData(filtedAccounts);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            logger.info("[-] Exception happened while querying Account {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }


    @PostMapping(value = "/account/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> addSupportAccount(HttpServletRequest servletRequest,
                                              @RequestBody AddSupportAccountDTO request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Map<String, String> addSupportAccountResponse = supportAccountService.addSupportAccount(request);
            if (addSupportAccountResponse != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(addSupportAccountResponse);
                return defaultResponse;
            }
            defaultResponse.setData(addSupportAccountResponse);
            return defaultResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while Processing Transaction requests {}", e.getMessage());
            return defaultResponse;
        }
    }

}
