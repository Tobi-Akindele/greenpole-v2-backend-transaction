package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.config.ApplicationPropertiesConfig;
import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransDTO;
import com.ap.greenpole.transactioncomponent.service.NotificationService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    final static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private ApplicationPropertiesConfig applicationProperties;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private static final String PENDING_URL = "/pending_approval";
    private static final String APPROVED_URL = "/successful_approval";
    private static final String REJECTED_URL = "/rejected_approval";

    @Override
    @Async
    public void sendPendingApproval(NotificationDTO notificationDTO) {
        logger.info("[+] Sending Pending Approval with  {}" , notificationDTO);
        sendNotification(notificationDTO, PENDING_URL);
    }

    @Override
    @Async
    public void sendRejectedApproval(NotificationDTO notificationDTO) {
        logger.info("[+] Sending Rejected Approval with  {}" , notificationDTO);
        sendNotification(notificationDTO, REJECTED_URL);
    }

    @Override
    @Async
    public void sendAcceptedApproval(NotificationDTO notificationDTO) {
        logger.info("[+] Sending Accepted Approval with  {}" , notificationDTO);
        sendNotification(notificationDTO, APPROVED_URL);
    }

    private void sendNotification(NotificationDTO notificationDTO, String noticationUrl) {
        notificationDTO.setModulePermission(ApplicationConstant.MODULE_APPROVAL_PERMISSION);
        notificationDTO.setModuleName(ApplicationConstant.MODULE);
        String url = String.format("%s%s", applicationProperties.getNotificationServiceBaseUrl(), noticationUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        restTemplateUtil.request(url, httpHeaders, HttpMethod.POST, notificationDTO, String.class);
    }
}
