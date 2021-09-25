package com.ap.greenpole.transactioncomponent.interceptor;

import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:50 PM
 */
@Aspect
public class TransactionProcessAspectInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessAspectInterceptor.class);

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.TransactionProcessController.authorizeRequest(..))")
    public DefaultResponse<?> authorizeRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        RequestAuthorizationDTO requestAuthorizationDTO = (RequestAuthorizationDTO) joinPoint.getArgs()[0];
        return TransactionUtil.validateRequest(joinPoint, defaultResponse, requestAuthorizationDTO);
    }

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.TransactionProcessController.processTransactionRequest(..))")
    public DefaultResponse<?> processTransactionRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        ProcessTransactionRequestDTO processTransactionRequestDTO = (ProcessTransactionRequestDTO) joinPoint.getArgs()[0];
        Boolean doProceed = true;
        String validationMessage = null;
        if (processTransactionRequestDTO == null) {
            validationMessage = "Request body is required";
            doProceed = false;
        }else if (!(processTransactionRequestDTO.getRequestId() > 0)) {
            validationMessage = "RequestId is required";
            doProceed = false;
        }else if (processTransactionRequestDTO.getClientCompany() == null || processTransactionRequestDTO.getClientCompany().isEmpty()) {
            validationMessage = "Client Company is required";
            doProceed = false;
        }else if (processTransactionRequestDTO.getTransactionDate() == null || processTransactionRequestDTO.getTransactionDate().isEmpty()) {
            validationMessage = "Transaction Date is required";
            doProceed = false;
        }else if (processTransactionRequestDTO.getUploadDate() == null || processTransactionRequestDTO.getUploadDate().isEmpty()) {
            validationMessage = "Upload Date is required";
            doProceed = false;
        }else {
            doProceed = true;
        }
        return TransactionUtil.getValidationResponse(joinPoint, defaultResponse, doProceed, validationMessage);
    }



}
