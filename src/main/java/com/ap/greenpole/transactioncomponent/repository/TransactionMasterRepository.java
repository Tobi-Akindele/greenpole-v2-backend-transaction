package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:46 AM
 */
public interface TransactionMasterRepository extends JpaRepository<TransactionMaster, Long> {
    List<TransactionMaster> findAllByTransactionRequestId(Long id);

    TransactionMaster findByCHN(String chn);

    @Query(value = "SELECT * FROM cscs_master WHERE chn = ?1 AND cscs_transaction_request_id = ?2", nativeQuery = true)
    TransactionMaster findByCHNAndTransactionRequest(String chn, Long transactionRequest);
}
