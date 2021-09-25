package com.ap.greenpole.transactioncomponent.service;

import java.util.List;
import java.util.Optional;

import com.ap.greenpole.transactioncomponent.entity.ShareHolderTemp;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:54 PM
 */
public interface ShareHolderTempService {

    ShareHolderTemp createTempShareHolder(TransactionMaster transactionMaster, TransactionRequest transactionRequest, Long shareUnit);

    Optional<ShareHolderTemp> findByClearingHousingNumber(String chn);

    List<ShareHolderTemp> findAllShareHolderTempList(TransactionRequest transactionRequest);

	ShareHolderTemp getShareHolderTempById(Long shareHolderId);

	ShareHolderTemp saveShareHolderTemp(ShareHolderTemp temp);

}
