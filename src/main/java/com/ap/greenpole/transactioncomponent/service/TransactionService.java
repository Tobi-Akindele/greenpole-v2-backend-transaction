package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.*;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.usermodule.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:56 AM
 */
public interface TransactionService {

    Transaction save(Transaction transaction);

    List<Transaction> saveAll(List<Transaction> transactions);

    Transaction getTransactionById(Long id);

    Page<Transaction> getAllTransactionPaginated(int page, int size);

    void balanceFile(ModuleRequestDTO moduleRequestDTO, ModuleRequest moduleRequest) throws Exception;

    void createTransaction(TransactionRequest transactionRequest, ModuleRequest moduleRequest) throws Exception;

    List<Transaction> findByTransactionRequestId(Long id);

    List<FilterTransactionDTO> getFilteredTransactions(Map<String, Object> queryData, int page, int size);

    AccountDetailDTO findDetailsByClientCompAndAcctNo(Map<String, Object> accountInfo);

	List<Transaction> findTransactionByCHN(String chn);
	
	List<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByCHNAndTransactionRequestAndSellOrBuy(String chn, Long transactionRequestId, String sellOrBuy);

	Transaction findByTransactionIdAndSellOrBuy(String transactionId, String sellOrBuy);
}
