package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.*;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.model.User;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 11/7/2020 11:56 PM
 */

@Service
public class ApprovalRequestServiceImpl implements ApprovalRequestService {

    final static Logger logger = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);

    @Autowired
    private ModuleRequestService moduleRequestService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionRequestService transactionRequestService;

    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Override
    public ModuleRequest createPendingApprovalRequest(Object object, User user, String requestType, NotificationDTO notification) {
        logger.info("[+] Inside ApprovalRequestServiceImpl.createPendingApprovalRequest() with {}", object.toString());

        try {
            ModuleRequest moduleRequest = new ModuleRequest(null, new Gson().toJson(object));
            moduleRequest.setCreatedOn(new Date());
            moduleRequest.setModules(ApplicationConstant.MODULE);
            moduleRequest.setStatus(ApprovalStatus.PENDING.getCode());
            moduleRequest.setRequestCode(Utils.getRequestCode(ApplicationConstant.MODULE));
            moduleRequest.setActionRequired(requestType);
            moduleRequest.setRequesterId(user.getId());

            ModuleRequest savedModuleRequest = moduleRequestService.save(moduleRequest);
            logger.info("[+] savedModuleRequest is {}", savedModuleRequest.toString());
            if (savedModuleRequest != null) {
                notification.setModuleName(ApplicationConstant.MODULE);
                notification.setModulePermission(String.format("%s_%s", ApplicationConstant.MODULE, "APPROVAL"));
                notification.setDataOwnerName("sir/ma");
                notification.setDataRequesterName(user.getFirstName());
                notification.setDataRequesterEmails(Collections.singletonList(user.getEmail()));
                notification.setDataRequesterPhoneNumbers(Collections.singletonList(user.getPhone()));
                notificationService.sendPendingApproval(notification);
            }
            return moduleRequest;
        } catch (Exception e) {
            logger.info("Error occured: {}", e);
            return null;
        }
    }


    @Override
//    @Async("transactionLogThreadPoolTaskExecutor")
    public void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user, String requestType) {
        logger.info("[+] Authorizing transaction with {} " , requestAuthorization.toString());
        List<DefaultResponse<?>> transactionRequestResponses = new ArrayList<>();
        DefaultResponse<?> response = new DefaultResponse<>();
        response.setStatus(ApiResponseCode.FAILED.getCode());
        response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        NotificationDTO notification =  new NotificationDTO();
        notification.setModuleName(ApplicationConstant.MODULE);
        notification.setModulePermission(String.format("%s_%s", ApplicationConstant.MODULE, "APPROVAL"));
        notification.setDataOwnerName("sir/ma");
        notification.setDataRequesterName(user.getFirstName());
        notification.setDataRequesterEmails(Collections.singletonList(user.getEmail()));
        notification.setDataRequesterPhoneNumbers(Collections.singletonList(user.getPhone()));
        requestAuthorization.getRequestIds()
                .stream()
                .forEach(requestId -> {
                    ModuleRequest request = moduleRequestService.getApprovalRequestById(requestId);
                    if (request == null) {
                        response.setStatusMessage(String.format("Request ID %s does not exist", requestId));
                        transactionRequestResponses.add(response);
                    } else {
                        if (ApprovalStatus.PENDING.getCode() != request.getStatus()) {
                            response.setStatusMessage(String.format("Request ID: %s has been %s ", requestId,
                                    ApplicationConstant.APPROVAL_STATUS[request.getStatus()]));
                            transactionRequestResponses.add(response);
                        } else {
                            if (ApplicationConstant.REQUEST_TYPES[0].equalsIgnoreCase(request.getActionRequired())) {
                                transactionRequestService.createTransactionComponent(request, requestId, requestAuthorization, user, notification);
                            }
                            if (ApplicationConstant.REQUEST_TYPES[2].equalsIgnoreCase(request.getActionRequired())) {
                                transactionRequestService.authorizeProcessTransactionRequest(request, requestId, requestAuthorization, user, notification);
                            }
                            if (ApplicationConstant.REQUEST_TYPES[1].equalsIgnoreCase(request.getActionRequired()) || ApplicationConstant.REQUEST_TYPES[3].equalsIgnoreCase(request.getActionRequired())) {
                                transactionRequestService.authorizeReconSuspensionRequest(request, requestAuthorization, user, notification, request.getActionRequired());
                            }
                        }
                    }
                });
        logger.info("TransactionRequestResponse >>> {}", new Gson().toJson(transactionRequestResponses));
    }
}
