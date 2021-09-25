package com.ap.greenpole.transactioncomponent.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.ShareHolderTemp;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.service.ProcessTransactionService;
import com.ap.greenpole.transactioncomponent.service.TransactionRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class TransactionProcessController {

    final static Logger logger = LoggerFactory.getLogger(TransactionProcessController.class);

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private TransactionRequestService transactionRequestService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ModuleRequestService moduleRequestService;

    @PostMapping(value = "/process/approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> authorizeRequest(
            @RequestBody RequestAuthorizationDTO requestAuthorization, HttpServletRequest request) {
        logger.info("[+] Inside TransactionProcessController.authorizeRequest() with {} " , requestAuthorization.toString());
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {

        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
            if(!user.isPresent()) {
                defaultResponse.setStatusMessage("User details not found");
                return defaultResponse;
            }

            processTransactionService.authorizeRequest(requestAuthorization, user.get());
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());

            return defaultResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @PostMapping(value = "/request/process", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> processTransactionRequest(@RequestBody ProcessTransactionRequestDTO request, HttpServletRequest servletRequest) {
        logger.info("[+] Inside TransactionProcessController.processTransactionRequest() with {} " , request.toString());
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        Optional<User> user = userService.memberFromAuthorization(servletRequest.getHeader(ApplicationConstant.AUTHORIZATION));
        if(!user.isPresent()) {
            defaultResponse.setStatusMessage("User details not found");
            return defaultResponse;
        }
        try {
            ProcessTransactionResponseDTO processTransactionResponseDTO = transactionRequestService.processTransaction(request, user.get());
            if(processTransactionResponseDTO != null){
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(processTransactionResponseDTO);
            }
            return defaultResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while Processing Transaction requests {}", e.getMessage());
            return defaultResponse;
        }
    }

    @GetMapping(value = "/query/request/shareholders/temp/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<List<ShareHolderTemp>> queryTransactionsShareHoldersForTaxExemption(@PathVariable Long requestId){
    	logger.info("[+] Inside TransactionProcessController.queryTransactionsShareHoldersForTaxExemption() with requestId: {} " , requestId);
        DefaultResponse<List<ShareHolderTemp>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        
        try {
			ModuleRequest request = moduleRequestService.getApprovalRequestById(requestId);
			if(request == null) {
				defaultResponse.setStatusMessage("Request does not exist");
				return defaultResponse;
			}
			if(ApprovalStatus.PENDING.getCode() != request.getStatus()) {
				defaultResponse.setStatusMessage("Only transactions pending can be marked tax exempted");
				return defaultResponse;
			}
			if (!ApplicationConstant.REQUEST_TYPES[2].equalsIgnoreCase(request.getActionRequired())) {
				defaultResponse.setStatusMessage("Only Approval request of type [ "
						+ ApplicationConstant.REQUEST_TYPES[2] + " ] has Temporary Shareholders for creation");
				return defaultResponse;
			}
			List<ShareHolderTemp> result = processTransactionService.getTransactionRequestShareHoldersTemp(request);
			
			defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
			defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
			defaultResponse.setData(result);
			
			return defaultResponse;
		} catch (Exception e) {
			e.printStackTrace();
            logger.info("[-] Exception happened while Retreiving Transaction requests Shaeholders {}", e.getMessage());
            return defaultResponse;
		}
    }
    
    @PostMapping(value = "/shareholder/temp/tax/exemption", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_ADD_TAX_EXEMPTION_TRANSACTION_ACCOUNTS"})
    public DefaultResponse<List<ShareHolderTemp>> addTaExemptionToTransactionShareHolderAccounts(@RequestBody List<Long> shareHolderTempIds,
    		@RequestHeader(value = "taxExemptionOption", required = false) boolean taxExemptionOption){
    	logger.info("[+] Inside TransactionProcessController.addTaExemptionToTransactionShareHolderAccounts() with shareHolderTempIds: {}, taxExemption: {} " , shareHolderTempIds, taxExemptionOption);
        DefaultResponse<List<ShareHolderTemp>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        
        try {
			if(shareHolderTempIds == null || shareHolderTempIds.isEmpty()) {
				defaultResponse.setStatusMessage("RequestBody is required");
				return defaultResponse;
			}
			
			List<ShareHolderTemp> result = processTransactionService.addTaxExemptionToShareHoldersTemp(shareHolderTempIds, taxExemptionOption);
			
			defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
			defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
			defaultResponse.setData(result);
			
			return defaultResponse;
		} catch (Exception e) {
			e.printStackTrace();
            logger.info("[-] Exception happened while Adding Tax exemption to Transaction requests Temp Shareholders {}", e.getMessage());
            return defaultResponse;
		}
    }
}
