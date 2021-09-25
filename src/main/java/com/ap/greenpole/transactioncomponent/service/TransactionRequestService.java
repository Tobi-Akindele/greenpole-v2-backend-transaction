package com.ap.greenpole.transactioncomponent.service;

import java.util.List;
import java.util.Map;

import com.ap.greenpole.transactioncomponent.dto.*;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.usermodule.model.User;
import org.springframework.data.domain.Page;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:24 PM
 */
public interface TransactionRequestService {

    TransactionRequest save(TransactionRequest transactionRequest);

    TransactionRequest getTransactionRequestById(Long id);

    Page<TransactionRequest> getAllTransactionRequestPaginated(int page, int size);

    Page<TransactionRequest> getTransactionForProcessing(Map<String, Object> queryData, int page, int size);

    ModuleRequest uploadTransactionRequest(ModuleRequestDTO moduleRequestDTO, User user);

    void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user);

    AnalyseTransactionDTO analyseTransaction(Long id);

    ProcessTransactionResponseDTO processTransaction(ProcessTransactionRequestDTO processTransactionRequestDTO, User user);

    List<QueryAccountDTO> queryAccount(Map<String, Object> queryData);

    void createTransactionComponent(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization,
                                    User user, NotificationDTO notification);

    void authorizeProcessTransactionRequest(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization,
                                            User user, NotificationDTO notification);


    void authorizeReconSuspensionRequest(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user, NotificationDTO notification, String requestType);

}
