package com.ap.greenpole.transactioncomponent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ap.greenpole.transactioncomponent.entity.ShareHolderTemp;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:56 PM
 */
public interface ShareHolderTempRepository extends JpaRepository<ShareHolderTemp, Long> {
    Optional<ShareHolderTemp> findByClearingHousingNumber(@Param("clearing_housing_number") String chn);

    List<ShareHolderTemp> findByTransactionRequest(TransactionRequest transactionRequest);

	ShareHolderTemp findByShareHolderId(Long shareHolderId);
}
