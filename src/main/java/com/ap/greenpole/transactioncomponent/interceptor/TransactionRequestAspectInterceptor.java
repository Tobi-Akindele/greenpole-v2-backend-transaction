package com.ap.greenpole.transactioncomponent.interceptor;

import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import com.ap.greenpole.transactioncomponent.util.Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:50 PM
 */
@Aspect
public class TransactionRequestAspectInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRequestAspectInterceptor.class);


    @Around("execution(* com.ap.greenpole.transactioncomponent.controller.TransactionRequestController.authorizeRequest(..))")
    public DefaultResponse<?> authorizeRequestValidation(ProceedingJoinPoint joinPoint) throws Throwable {
        DefaultResponse<ModuleRequest> defaultResponse = new DefaultResponse<>();
        RequestAuthorizationDTO requestAuthorizationDTO = (RequestAuthorizationDTO) joinPoint.getArgs()[0];
        return TransactionUtil.validateRequest(joinPoint, defaultResponse, requestAuthorizationDTO);
    }



}
