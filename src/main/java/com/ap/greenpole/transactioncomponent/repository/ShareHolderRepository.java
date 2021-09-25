package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.dto.AccountDetailDTO;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:56 PM
 */
public interface ShareHolderRepository extends JpaRepository<ShareHolder, Long> {

    Optional<ShareHolder> findByClearingHousingNumberAndStatus(@Param("clearing_housing_number") String chn, @Param("status") String status);

    List<ShareHolder> findByClearingHousingNumberLikeOrFirstNameLikeOrMiddleNameLikeOrLastNameLikeOrShareHolderId(@Param("clearing_housing_number") String chn,
                                                                                                             @Param("first_name") String firstName,
                                                                                                             @Param("middle_name") String middlename,
                                                                                                             @Param("last_name") String lastname,
                                                                                                             @Param("shareholder_id") long shareholder_id);

    ShareHolder findByShareHolderIdAndClientCompanyAndStatus(long acctNumber, long clientCompany, String status);

    Optional<ShareHolder> findByClearingHousingNumberAndClientCompanyAndStatus(String chn, long clientComp, String status);

    Optional<ShareHolder> findByShareHolderIdAndStatus(Long acctNo, String status);
}
