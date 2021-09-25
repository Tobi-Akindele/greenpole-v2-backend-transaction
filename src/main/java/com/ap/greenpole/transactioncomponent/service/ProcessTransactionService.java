package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.ProcessedTransaction;
import com.ap.greenpole.transactioncomponent.entity.ShareHolderTemp;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.usermodule.model.User;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
public interface ProcessTransactionService {

    ProcessedTransaction save(ProcessedTransaction transaction);

    List<ProcessedTransaction> saveAll(List<ProcessedTransaction> transactions);

    ProcessedTransaction findByCSCSTransactionId(Long id);


    void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user);

    void authorizeProcessTransactionRequest(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization,
                                            User user, NotificationDTO notification );

    void transferUnit(TransactionRequest transactionRequest);

	List<ShareHolderTemp> getTransactionRequestShareHoldersTemp(ModuleRequest request);

	List<ShareHolderTemp> addTaxExemptionToShareHoldersTemp(List<Long> shareHolderTempIds, boolean taxExemptionOption);
}
