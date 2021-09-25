package com.ap.greenpole.transactioncomponent.service;

import java.util.List;
import java.util.Map;

import com.ap.greenpole.transactioncomponent.dto.*;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.usermodule.model.User;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
public interface ReconciliationService {

    List<ReconcileSuspendedTransDTO> getSuspendedTransactionForReconciliation(Map<String, Object> queryData);

    ModuleRequest reconcileSuspendedTransactionRequest(ReconcileSuspendedTransAuthorizeDTO reconcileSuspendedTransDTO, User user);

    void authorizeReconciliationSuspendedTransactionRequest(RequestAuthorizationDTO requestAuthorization, User user);

    ModuleRequest reconcileSuspendedTransactionManualRequest(ReconcileSuspendedTransManualDTO reconcileSuspendedTransDTO, User user);

    void authorizeReconciliationSuspendedTransactionManualRequest(RequestAuthorizationDTO requestAuthorization, User user);

    void authorizeReconSuspensionRequest(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user, boolean isManual, NotificationDTO notification);

	Object cancelSuspendedTransactions(List<String> cscsTransactionIds, boolean action);

	List<CancelledSuspendedTransactionsDto> searchCancelledSuspendedTransactions(Map<String, Object> queryData) throws Exception ;
}
