package com.ap.greenpole.transactioncomponent.interceptor;

import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/15/2020 5:02 PM
 */

@Aspect
public class ReconciliationInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRequestAspectInterceptor.class);

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.ReconciliationController.authorizeRequest(..))")
    public DefaultResponse<?> authorizeRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        RequestAuthorizationDTO requestAuthorizationDTO = (RequestAuthorizationDTO) joinPoint.getArgs()[0];
        return TransactionUtil.validateRequest(joinPoint, defaultResponse, requestAuthorizationDTO);
    }

    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.ReconciliationController.authorizeManualRequest(..))")
    public DefaultResponse<?> authorizeManualRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        RequestAuthorizationDTO requestAuthorizationDTO = (RequestAuthorizationDTO) joinPoint.getArgs()[0];
        return TransactionUtil.validateRequest(joinPoint, defaultResponse, requestAuthorizationDTO);
    }
}
