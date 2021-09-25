package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.entity.Holderkin;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.enums.GenericStatusEnum;
import com.ap.greenpole.transactioncomponent.repository.ShareHolderRepository;
import com.ap.greenpole.transactioncomponent.service.ShareHolderService;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:55 PM
 */

@Service
public class ShareHolderServiceImpl implements ShareHolderService {

    @Autowired
    private ShareHolderRepository shareHolderRepository;

    @Override
    public ShareHolder save(ShareHolder shareHolder) {
        return shareHolderRepository.save(shareHolder);
    }

    @Override
    public List<ShareHolder> saveAll(List<ShareHolder> shareHolders) {
        return shareHolderRepository.saveAll(shareHolders);
    }

    @Override
    public Optional<ShareHolder> findByClearingHousingNumber(String chn) {
        return shareHolderRepository.findByClearingHousingNumberAndStatus(chn, GenericStatusEnum.ACTIVE.name());
    }

    @Override
    public Optional<ShareHolder> findByAccountNumber(Long acctNo) {
        return shareHolderRepository.findByShareHolderIdAndStatus(acctNo, GenericStatusEnum.ACTIVE.name());
    }

    @Override
    public Optional<ShareHolder> findByClearingHousingNumberAndClientComp(String chn, long clientComp) {
        return shareHolderRepository.findByClearingHousingNumberAndClientCompanyAndStatus(chn, clientComp, GenericStatusEnum.ACTIVE.name());
    }

    @Override
    public Optional<ShareHolder> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public ShareHolder createNewShareHolder(TransactionMaster transactionMaster, TransactionRequest transactionRequest, Long shareUnit) {
        ShareHolder shareHolder = new ShareHolder();
        String shareHolderFullName = transactionMaster.getNames();
        String[] splitShareHolderFullName = shareHolderFullName.split("\\W");
        shareHolder.setFirstName(splitShareHolderFullName[1]);
        shareHolder.setLastName(splitShareHolderFullName[0]);
        if (splitShareHolderFullName.length > 2) {
            shareHolder.setMiddleName(splitShareHolderFullName[2]);
        }
        shareHolder.setClientCompany(Long.valueOf(transactionRequest.getClientCompany()));
        shareHolder.setEmail(transactionMaster.getEmailAddress());
        shareHolder.setCreatedOn(new Date());
        shareHolder.setClearingHousingNumber(transactionMaster.getCHN());
        shareHolder.setFirstName("");
        shareHolder.setLastName("");
        shareHolder.setMiddleName("");
        shareHolder.setEsopStatus(null);
        shareHolder.setAddress(String.format("%s, %s %s", transactionMaster.getAddress1(),
                transactionMaster.getAddress2(), transactionMaster.getAddress3()));
        shareHolder.setBankAccount(transactionMaster.getBankAccountNumber());
        shareHolder.setBankName(transactionMaster.getBankName());
        shareHolder.setBvn(transactionMaster.getBvn());
        shareHolder.setShareUnit(shareUnit == null ? 0 : shareUnit);
        shareHolder.setCountry(transactionMaster.getCountry());
        shareHolder.setPhone(transactionMaster.getPhoneNumber());
        Holderkin holderkin = new Holderkin();
        holderkin.setStatus(true);
        holderkin.setKinName(transactionMaster.getNextOfKinOrMaiden());
        shareHolder.setHolderkin(holderkin);
        return shareHolderRepository.save(shareHolder);
    }


    @Override
    public List<ShareHolder> queryAccount(String chn, String name, long accountNumber) {
        chn = "%" + chn + "%";
        String firstName = TransactionUtil.getName(name).get("firstName");
        String lastName = TransactionUtil.getName(name).get("lastName");
        String middleName = TransactionUtil.getName(name).get("middleName");
        List<ShareHolder> result = shareHolderRepository.findByClearingHousingNumberLikeOrFirstNameLikeOrMiddleNameLikeOrLastNameLikeOrShareHolderId(chn, "%" + firstName + "%", "%" + middleName + "%", "%" + lastName + "%", accountNumber);
        return result;
    }


    @Override
    public ShareHolder findDetailsByClientCompAndAcctNo(Map<String, Object> acountInfo) {
        long accountNumber = Long.parseLong(String.valueOf(acountInfo.get("account_number")));
        long clientCompany = Long.parseLong(String.valueOf(acountInfo.get("client_company")));
        return shareHolderRepository.findByShareHolderIdAndClientCompanyAndStatus(accountNumber, clientCompany, GenericStatusEnum.ACTIVE.name());
    }

    @Override
    public boolean existByAccountNumber(long accountNumber) {
        return shareHolderRepository.existsById(accountNumber);
    }

}
