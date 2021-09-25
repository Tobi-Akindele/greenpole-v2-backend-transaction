package com.ap.greenpole.transactioncomponent.interceptor;

import com.ap.greenpole.transactioncomponent.dto.ProcessMasterTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/9/2020 11:55 AM
 */

@Aspect
public class TransactionInterceptor {

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.TransactionController.uploadTransaction(..))")
    public DefaultResponse<?> uploadTransactionValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        MultipartFile masterFile = (MultipartFile) joinPoint.getArgs()[0];
        MultipartFile transactionFile = (MultipartFile) joinPoint.getArgs()[1];
        String transactionType = (String) joinPoint.getArgs()[2];
        String depository = (String) joinPoint.getArgs()[3];
        String transactionDate = (String) joinPoint.getArgs()[4];
        String clientCompany = (String) joinPoint.getArgs()[5];
        boolean doProceed = true;
        String validationMessage = null;
        if(masterFile == null || masterFile.isEmpty()){
            validationMessage = "MasterFile is Required";
            doProceed = false;
        }
        if(transactionFile == null || transactionFile.isEmpty()){
            validationMessage = "TransactionFile is Required";
            doProceed = false;
        }
        if(transactionType == null || transactionType.isEmpty()){
            validationMessage = "transactionType is Required";
            doProceed = false;
        }
        if(depository == null || depository.isEmpty()){
            validationMessage = "depository is Required";
            doProceed = false;
        }
        if(transactionDate == null || transactionDate.isEmpty()){
            validationMessage = "transactionDate is Required";
            doProceed = false;
        }
        if(clientCompany == null || clientCompany.isEmpty()){
            validationMessage = "transactionDate is Required";
            doProceed = false;
        }

        if(doProceed){
           return (DefaultResponse<?>) joinPoint.proceed();
        }else {
            defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
            defaultResponse.setStatusMessage(validationMessage);
            return defaultResponse;
        }
    }


    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.TransactionController.fixMasterRecord(..))")
    public DefaultResponse<?> fixMasterRecordValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        ProcessMasterTransactionResponseDTO processMasterTransactionResponseDTO = (ProcessMasterTransactionResponseDTO) joinPoint.getArgs()[1];
        boolean doProceed = true;
        String validationMessage = null;
        try {
            if(!(processMasterTransactionResponseDTO.getMasterRecordId() > 0)){
                validationMessage = "MasterRecordId is Required";
                doProceed = false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            validationMessage = "Expection Happened Validating MasterRecordId";
            doProceed = false;
        }

        if(doProceed){
            return (DefaultResponse<?>) joinPoint.proceed();
        }else {
            defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
            defaultResponse.setStatusMessage(validationMessage);
            return defaultResponse;
        }
    }
}
