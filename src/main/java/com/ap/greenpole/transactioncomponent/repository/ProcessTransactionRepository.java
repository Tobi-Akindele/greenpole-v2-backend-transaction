package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.ProcessedTransaction;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:46 AM
 */
public interface ProcessTransactionRepository extends JpaRepository<ProcessedTransaction, Long> {
    ProcessedTransaction findByTransactionId(long cscsTransId);
}
