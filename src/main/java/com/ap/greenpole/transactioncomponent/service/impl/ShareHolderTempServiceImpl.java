package com.ap.greenpole.transactioncomponent.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ap.greenpole.transactioncomponent.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ap.greenpole.transactioncomponent.enums.GenericStatusEnum;
import com.ap.greenpole.transactioncomponent.repository.ShareHolderRepository;
import com.ap.greenpole.transactioncomponent.repository.ShareHolderTempRepository;
import com.ap.greenpole.transactioncomponent.service.ShareHolderTempService;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:55 PM
 */

@Service
public class ShareHolderTempServiceImpl implements ShareHolderTempService {

    static final Logger logger = LoggerFactory.getLogger(ProcessTransactionServiceImpl.class);


    @Autowired
    private ShareHolderTempRepository shareHolderTempRepository;

    @Autowired
    private ShareHolderRepository shareHolderRepository;

    private final static String INDIVIDUAL = "INDIVIDUAL";


    @Override
    public ShareHolderTemp createTempShareHolder(TransactionMaster transactionMaster, TransactionRequest transactionRequest, Long shareUnit) {
        ShareHolderTemp shareHolder = new ShareHolderTemp();
        shareHolder.setClientCompany(Long.valueOf(transactionRequest.getClientCompany()));
        shareHolder.setEmail(transactionMaster.getEmailAddress());
        shareHolder.setCreatedOn(new Date());
        shareHolder.setClearingHousingNumber(transactionMaster.getCHN());
        shareHolder.setShareholderType(transactionMaster.getStructure());
        Optional<String> structure = Optional.ofNullable(transactionMaster.getStructure());
        if(structure.isPresent() && structure.get().equalsIgnoreCase(INDIVIDUAL)){
            shareHolder.setFirstName(TransactionUtil.getName(transactionMaster.getNames()).get("firstName"));
            shareHolder.setLastName(TransactionUtil.getName(transactionMaster.getNames()).get("lastName"));
            shareHolder.setMiddleName(TransactionUtil.getName(transactionMaster.getNames()).get("firstName"));
        }else {
            shareHolder.setFirstName(transactionMaster.getNames());
            shareHolder.setLastName(transactionMaster.getNames());
            shareHolder.setMiddleName(transactionMaster.getNames());
        }
        shareHolder.setEsopStatus(null);
        shareHolder.setAddress(String.format("%s, %s %s", transactionMaster.getAddress1(),
                transactionMaster.getAddress2(), transactionMaster.getAddress3()));
        shareHolder.setBankAccount(transactionMaster.getBankAccountNumber());
        shareHolder.setBankName(transactionMaster.getBankName());
        shareHolder.setBvn(transactionMaster.getBvn());
        shareHolder.setShareUnit(shareUnit == null ? 0 : shareUnit);
        shareHolder.setCountry(transactionMaster.getCountry());
        shareHolder.setPhone(transactionMaster.getPhoneNumber());
        shareHolder.setKinName(transactionMaster.getNextOfKinOrMaiden());
        shareHolder.setStatus(GenericStatusEnum.ACTIVE.toString());
        //Todo remove needed only for test
//        ShareHolder testShareHolder = new ShareHolder();
//        BeanUtils.copyProperties(shareHolder, testShareHolder);
//        Holderkin holderkin = new Holderkin();
//        holderkin.setKinName(shareHolder.getKinName());
//        holderkin.setShareHolder(testShareHolder);
//        testShareHolder.setHolderkin(holderkin);
//        shareHolderRepository.save(testShareHolder);
        return shareHolder;
    }

    @Override
    public Optional<ShareHolderTemp> findByClearingHousingNumber(String chn) {
        return shareHolderTempRepository.findByClearingHousingNumber(chn);
    }

    @Override
    public List<ShareHolderTemp> findAllShareHolderTempList(TransactionRequest transactionRequest) {
        return shareHolderTempRepository.findByTransactionRequest(transactionRequest);
    }

	@Override
	public ShareHolderTemp getShareHolderTempById(Long shareHolderId) {
		return shareHolderTempRepository.findByShareHolderId(shareHolderId);
	}

	@Override
	public ShareHolderTemp saveShareHolderTemp(ShareHolderTemp temp) {
		return shareHolderTempRepository.save(temp);
	}

}
