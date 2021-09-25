package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.usermodule.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/13/2020 11:48 AM
 */
public interface ApprovalRequestService {

    ModuleRequest createPendingApprovalRequest(Object object, User user, String requestType, NotificationDTO notification);

    void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user, String requestType);
}
