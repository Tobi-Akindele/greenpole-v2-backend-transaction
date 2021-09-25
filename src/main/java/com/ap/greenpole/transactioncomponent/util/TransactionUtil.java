package com.ap.greenpole.transactioncomponent.util;

import com.ap.greenpole.transactioncomponent.controller.TransactionController;
import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.ProcessMasterTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.repository.TransactionRequestRepository;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.service.NotificationService;
import com.ap.greenpole.transactioncomponent.service.impl.TransactionRequestServiceImpl;
import com.ap.greenpole.usermodule.model.User;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 10:59 PM
 */

@Component
public class TransactionUtil {

    final static Logger logger = LoggerFactory.getLogger(TransactionUtil.class);

    @Autowired
    private ModuleRequestService moduleRequestService;

    @Autowired
    private NotificationService notificationService;

    public static Map<String, String> getName(String names) {
        Map<String, String> nameMap = new HashMap<>();
        try {
            if (names != null && names.length() > 0) {
                String[] splitShareHolderFullName = names.split("\\W");
                if (splitShareHolderFullName.length > 0) {
                    nameMap.put("lastName", splitShareHolderFullName[0]);
                }
                if (splitShareHolderFullName.length > 1) {
                    nameMap.put("firstName", splitShareHolderFullName[1]);
                }
                if (splitShareHolderFullName.length > 2) {
                    nameMap.put("middleName", splitShareHolderFullName[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameMap;
    }

    public static String getNameFromShareHolder(ShareHolder shareHolder) {
        String firstName = null;
        String lastName = null;
        String middleName = null;
        try {
            firstName = shareHolder.getFirstName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lastName = shareHolder.getLastName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            middleName = shareHolder.getMiddleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastName + " " + firstName + " " + middleName;
    }

    public static String getNameFromFixedMasterRecord(ProcessMasterTransactionResponseDTO processMasterTransactionResponseDTO) {
        String firstName = null;
        String lastName = null;
        String middleName = null;
        try {
            firstName = processMasterTransactionResponseDTO.getSystemFirstName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lastName = processMasterTransactionResponseDTO.getSystemLastName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            middleName = processMasterTransactionResponseDTO.getSystemMiddleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastName + " " + firstName + " " + middleName;
    }

    public static boolean isSellerTransaction(Transaction transaction) {
        return transaction.getSellOrBuy().equals("-");
    }

    public static boolean isBuyerTransaction(Transaction transaction) {
        return transaction.getSellOrBuy().equals("+");
    }

    public static Map<String, Object> validateAuthorizeRequest(RequestAuthorizationDTO requestAuthorizationDTO) {
        Map<String, Object> responseMap = new HashMap<>();

        if (requestAuthorizationDTO == null) {
            responseMap.put("validationMessage", "Request body is required");
            responseMap.put("doProcess", Boolean.FALSE);
            return responseMap;
        }
        if (requestAuthorizationDTO.getRequestIds() == null || requestAuthorizationDTO.getRequestIds().isEmpty()) {
            responseMap.put("validationMessage", "At least one Request ID is required");
            responseMap.put("doProcess", Boolean.FALSE);
            return responseMap;
        }
        if (!Arrays.asList(ApplicationConstant.APPROVAL_ACTIONS).contains(requestAuthorizationDTO.getAction())) {
            responseMap.put("validationMessage", String.format("Invalid Action, options include %s", Arrays.toString(ApplicationConstant.APPROVAL_ACTIONS)));
            responseMap.put("doProcess", Boolean.FALSE);
            return responseMap;
        }
        if (ApplicationConstant.APPROVAL_ACTIONS[1].equalsIgnoreCase(requestAuthorizationDTO.getAction())
                && Utils.isEmptyString(requestAuthorizationDTO.getRejectionReason())) {
            responseMap.put("validationMessage", "Rejection reason is required");
            responseMap.put("doProcess", Boolean.FALSE);
            return responseMap;
        }
        responseMap.put("doProcess", Boolean.TRUE);
        return responseMap;
    }

    public static DefaultResponse<?> validateRequest(ProceedingJoinPoint joinPoint, DefaultResponse<?> defaultResponse, RequestAuthorizationDTO requestAuthorizationDTO) throws Throwable {
        Map<String, Object> stringObjectMap = validateAuthorizeRequest(requestAuthorizationDTO);
        Boolean doProceed = (Boolean) stringObjectMap.get("doProcess");
        String validationMessage = (String) stringObjectMap.get("validationMessage");
        return getValidationResponse(joinPoint, defaultResponse, doProceed, validationMessage);
    }

    public static DefaultResponse<?> getValidationResponse(ProceedingJoinPoint joinPoint, DefaultResponse<?> defaultResponse, Boolean doProceed, String validationMessage) throws Throwable {
        if (doProceed) {
            return (DefaultResponse<?>) joinPoint.proceed();
        } else {
            defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
            defaultResponse.setStatusMessage(validationMessage);
            return defaultResponse;
        }
    }

    public static Map<String, Object>  validateUploadRequest(MultipartFile masterFile, MultipartFile transactionFile,
                                                            String transactionType, String depository, String transactionDate,
                                                            String clientCompany) {
        logger.info("Inside TransactionController.uploadTransaction() with transactionType: {}, depository: {}, " +
                "transactionDate: {}, clientCompany: {}", transactionType, depository, transactionDate, clientCompany);
        Map<String, Object> responseMap = new HashMap<>();
        Boolean doProceed;
        String validationMessage = null;
        if(masterFile == null || masterFile.isEmpty()){
            validationMessage = "MasterFile is Required";
            doProceed = false;
        }else if(transactionFile == null || transactionFile.isEmpty()){
            validationMessage = "TransactionFile is Required";
            doProceed = Boolean.FALSE;
        }else if(transactionType == null || StringUtils.isEmpty(transactionType)){
            validationMessage = "transactionType is Required";
            doProceed = Boolean.FALSE;
        }else if(depository == null || StringUtils.isEmpty(depository)){
            validationMessage = "depository is Required";
            doProceed = Boolean.FALSE;
        }else if(transactionDate == null || StringUtils.isEmpty(transactionDate)){
            validationMessage = "transactionDate is Required";
            doProceed = Boolean.FALSE;
        }else if(clientCompany == null || StringUtils.isEmpty(clientCompany)){
            validationMessage = "transactionDate is Required";
            doProceed = Boolean.FALSE;
        }else {
            doProceed = Boolean.TRUE;
        }
        responseMap.put("validationMessage", validationMessage);
        responseMap.put("doProceed", doProceed);
        return responseMap;
    }

    public void sendRejectionNotification(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user, NotificationDTO notification) {
        request = Utils.setRequestStatus(ApprovalStatus.REJECTED.getCode(), user.getId(),
                requestAuthorization.getRejectionReason(), request);
        request.setApprovedOn(new Date());
        ModuleRequest rejectedModuleRequest = moduleRequestService.save(request);
        if (rejectedModuleRequest != null) {
            notificationService.sendRejectedApproval(notification);
        }
    }

    public void sendApprovalNotification(ModuleRequest request, User user, NotificationDTO notification) {
        request = Utils.setRequestStatus(ApprovalStatus.ACCEPTED.getCode(), user.getId(),
                null, request);
        request.setApprovedOn(new Date());
        ModuleRequest approvedModuleRequest = moduleRequestService.save(request);
        if (approvedModuleRequest != null) {
            notificationService.sendAcceptedApproval(notification);
        }
    }


    public static NotificationDTO getDataOwnerEmailsAndPhoneNumbers(List<Transaction> transactionDataList, List<TransactionMaster> transactionMasterDataList) {
        NotificationDTO notification = new NotificationDTO();
        List<String> dataOwnerEmails = new ArrayList<>();
        List<String> dataOwnerPhoneNumbers = new ArrayList<>();
        transactionDataList.stream().forEach(t -> {
            Optional<TransactionMaster> transactionMaster = transactionMasterDataList.stream().filter(tm -> tm.getCHN().equalsIgnoreCase(t.getCHN())).findFirst();
            if(transactionMaster.isPresent()){
                try {
                    TransactionMaster transactionMaster1 = transactionMaster.get();
                    dataOwnerEmails.add(transactionMaster1.getEmailAddress());
                    dataOwnerPhoneNumbers.add(transactionMaster1.getPhoneNumber());
                } catch (Exception e) {
                    logger.info("Exception happen while getting dataOwnerEmails or dataOwnerPhoneNumbers");
                    e.printStackTrace();
                }
            }
        });
        notification.setDataOwnerEmails(dataOwnerEmails);
        notification.setDataOwnerPhoneNumbers(dataOwnerPhoneNumbers);
        return notification;
    }
}
