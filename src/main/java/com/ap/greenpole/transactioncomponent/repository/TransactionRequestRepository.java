package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:25 PM
 */
public interface TransactionRequestRepository extends PagingAndSortingRepository<TransactionRequest, Long> {

    Page<TransactionRequest> findAllTransactionRequestByTransactionDateOrClientCompanyLikeOrUploadDate(Date transactionDate,
                                                                                                       String clientCompany,
                                                                                                       Date uploadDate,
                                                                                                       Pageable pageable);

}
