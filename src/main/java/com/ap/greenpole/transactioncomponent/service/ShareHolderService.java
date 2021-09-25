package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:54 PM
 */
public interface ShareHolderService {

    ShareHolder save(ShareHolder shareHolder);

    List<ShareHolder> saveAll(List<ShareHolder> shareHolders);

    Optional<ShareHolder> findByClearingHousingNumber(String chn);

    Optional<ShareHolder> findByAccountNumber(Long acctNo);

    Optional<ShareHolder> findByClearingHousingNumberAndClientComp(String chn, long ClientComp);

    Optional<ShareHolder> findByName(String name);

    ShareHolder createNewShareHolder(TransactionMaster transactionMaster, TransactionRequest transactionRequest, Long shareUnit);

    List<ShareHolder> queryAccount(String chn, String name, long accountNumber);

    ShareHolder findDetailsByClientCompAndAcctNo(Map<String, Object> acountInfo);

    boolean existByAccountNumber(long accountNumber);
}
