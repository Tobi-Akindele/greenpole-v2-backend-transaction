package com.ap.greenpole.transactioncomponent.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.dto.TransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import com.ap.greenpole.transactioncomponent.service.ClientCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.TransactionRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class TransactionRequestController {

    final static Logger logger = LoggerFactory.getLogger(TransactionRequestController.class);

    @Autowired
    private TransactionRequestService transactionRequestService;

    @Autowired
    private ClientCompanyService clientCompanyService;
    
    @Autowired
    private UserService userService;

    @PostMapping(value = "/request/approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> authorizeRequest(
            @RequestBody RequestAuthorizationDTO requestAuthorization, HttpServletRequest request) {
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        
        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
    	if(!user.isPresent()) {
    		defaultResponse.setStatusMessage("User details not found");
    		return defaultResponse;
    	}
        try {
            transactionRequestService.authorizeRequest(requestAuthorization, user.get());
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            return defaultResponse;
        } catch (Exception e) {
        	logger.info("[-] Exception happened while Authorizing requests {}", e.getMessage());
            return defaultResponse;
        }
    }


    @GetMapping(value = "/requests/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
    public DefaultResponse<List<TransactionRequest>> getAllTransactionRequestPaginated(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        DefaultResponse<List<TransactionRequest>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Page<TransactionRequest> allTransactionRequestPaginated = transactionRequestService.getAllTransactionRequestPaginated(page, size);
            List<TransactionRequest> allTransactionRequest = allTransactionRequestPaginated.getContent();
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (allTransactionRequest != null && !allTransactionRequest.isEmpty()) {
                defaultResponse.setCount(allTransactionRequestPaginated.getTotalElements());
                defaultResponse.setData(allTransactionRequest);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (NumberFormatException e) {
        	logger.info("[-] Exception happened while Processing Transaction requests {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }


    @GetMapping(value = "/request/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
    public DefaultResponse<TransactionRequest> getTransactionRequestById(@PathVariable long id) {
        DefaultResponse<TransactionRequest> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        
        try {
            if (id <= 0) {
            	defaultResponse.setStatusMessage("Request ID is required");
                return defaultResponse;
            }
            TransactionRequest transactionRequestById = transactionRequestService.getTransactionRequestById(id);
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (transactionRequestById != null) {
                defaultResponse.setData(transactionRequestById);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
            }
        } catch (Exception e) {
            logger.info("[-] Exception happened while getting getTransactionRequestById {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
        }
        return defaultResponse;
    }


    @GetMapping(value = "/request/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
    public DefaultResponse<List<TransactionRequestDTO>> getAllTransactionForProcessing(
            @RequestParam(value = "client_company", required = false, defaultValue = "0") String clientCompany,
            @RequestParam(value = "transaction_date", required = false, defaultValue = "2021-10-14 00:00:00.000000") String transactionDate,
            @RequestParam(value = "uploaded_date", required = false, defaultValue = "2021-10-14 00:00:00.000000") String uploadDate,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        DefaultResponse<List<TransactionRequestDTO>> defaultResponse = new DefaultResponse<>();
        List<TransactionRequestDTO> transactionRequestDTOS = new ArrayList<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
            Map<String, Object> queryData = new HashMap<>();
            queryData.put("clientCompany", clientCompany);
            queryData.put("transactionDate", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, transactionDate));
            queryData.put("uploadDate", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, uploadDate));
            Page<TransactionRequest> transactionForProcessing = transactionRequestService.getTransactionForProcessing(queryData, page, size);
            List<TransactionRequest> allTransaction = transactionForProcessing.getContent();
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            if (allTransaction != null && !allTransaction.isEmpty()) {
                defaultResponse.setCount(transactionForProcessing.getTotalElements());
                allTransaction.stream().forEach(t -> {
                    TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO();
                    BeanUtils.copyProperties(t, transactionRequestDTO);
                    try{
                        ClientCompany clientCompaniesById = clientCompanyService.getClientCompaniesById(Long.parseLong(t.getClientCompany()));
                        transactionRequestDTO.setClientCompanyName(clientCompaniesById.getRegisterName());
                    }catch (Exception e){
                        logger.info("[-] Exception happened while getting clientCompany {}", e.getMessage());
                    }
                    transactionRequestDTOS.add(transactionRequestDTO);
                });
                defaultResponse.setData(transactionRequestDTOS);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
            logger.info("[-] Exception happened while getting AllTransactionForProcessing {}", e.getMessage());
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }
}
