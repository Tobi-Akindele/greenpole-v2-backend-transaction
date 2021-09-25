package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransManualDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.usermodule.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
public interface NotificationService {

    void sendPendingApproval(NotificationDTO notificationDTO);

    void sendRejectedApproval(NotificationDTO notificationDTO);

    void sendAcceptedApproval(NotificationDTO notificationDTO);
}
