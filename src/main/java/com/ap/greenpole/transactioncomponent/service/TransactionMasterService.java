package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.ProcessMasterTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
public interface TransactionMasterService {

    TransactionMaster save(TransactionMaster transactionMaster);

    List<TransactionMaster> saveAll(List<TransactionMaster> transactionMasters);

    TransactionMaster getTransactionMasterById(Long id);

    List<TransactionMaster> getAllTransactionMaster();

    List<TransactionMaster> getMasterFileDataList(TransactionRequest transactionRequest, ModuleRequest moduleRequest) throws Exception;

    List<TransactionMaster> findByTransactionRequestId(Long id);

    TransactionMaster findByChn(String Chn);

    TransactionMaster findByChnAndTransactionRequest(String Chn, Long transRequest);

    ProcessMasterTransactionResponseDTO fixMasterRecord(ProcessMasterTransactionResponseDTO request);
}
