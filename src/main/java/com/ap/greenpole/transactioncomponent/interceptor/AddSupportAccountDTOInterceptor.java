package com.ap.greenpole.transactioncomponent.interceptor;

import com.ap.greenpole.transactioncomponent.dto.AddSupportAccountDTO;
import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/15/2020 4:54 PM
 */

@Aspect
public class AddSupportAccountDTOInterceptor {

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.SupportAccountController.addSupportAccount(..))")
    public DefaultResponse<?> processTransactionRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        AddSupportAccountDTO addSupportAccountDTO = (AddSupportAccountDTO) joinPoint.getArgs()[1];
        Boolean doProceed;
        String validationMessage = null;
        if (addSupportAccountDTO == null) {
            validationMessage = "Request body is required";
            doProceed = false;
        }else if (!(addSupportAccountDTO.getTransactionRecordId() > 0)) {
            validationMessage = "TransactionRecordId is required";
            doProceed = false;
        }else if (addSupportAccountDTO.getAccountNumbers().size() > 0) {
            validationMessage = "One or More SupportAccount is required";
            doProceed = false;
        }else {
            doProceed = true;
        }
        return TransactionUtil.getValidationResponse(joinPoint, defaultResponse, doProceed, validationMessage);
    }
}
