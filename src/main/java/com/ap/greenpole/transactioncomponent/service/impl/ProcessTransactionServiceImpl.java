package com.ap.greenpole.transactioncomponent.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ap.greenpole.transactioncomponent.entity.*;
import com.ap.greenpole.transactioncomponent.repository.TransactionRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.ProcessTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.enums.ProcessStatus;
import com.ap.greenpole.transactioncomponent.repository.ProcessTransactionRepository;
import com.ap.greenpole.transactioncomponent.service.ApprovalRequestService;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.service.NotificationService;
import com.ap.greenpole.transactioncomponent.service.ProcessTransactionService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderTempService;
import com.ap.greenpole.transactioncomponent.service.TaxExemptionService;
import com.ap.greenpole.transactioncomponent.service.TransactionRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.model.User;
import com.google.gson.Gson;


/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:52 AM
 */
@Service
public class ProcessTransactionServiceImpl implements ProcessTransactionService {

	static final Logger logger = LoggerFactory.getLogger(ProcessTransactionServiceImpl.class);


    @Autowired
    private ProcessTransactionRepository processTransactionRepository;

    @Autowired
    private ModuleRequestService moduleRequestService;

    @Autowired
    private ApprovalRequestService approvalRequestService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ShareHolderTempService shareHolderTempService;

    @Autowired
    private ShareHolderService shareHolderService;

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;
    
    @Autowired
    private TaxExemptionService taxExemptionService;

    @Override
    public ProcessedTransaction save(ProcessedTransaction transaction) {
        return processTransactionRepository.save(transaction);
    }

    @Override
    public List<ProcessedTransaction> saveAll(List<ProcessedTransaction> transactions) {
        return null;
    }

    @Override
    public ProcessedTransaction findByCSCSTransactionId(Long id) {
        return processTransactionRepository.findByTransactionId(id);
    }

    @Override
    public void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user) {
        approvalRequestService.authorizeRequest(requestAuthorization, user, ApplicationConstant.REQUEST_TYPES[2]);
    }

    @Override
    public void authorizeProcessTransactionRequest(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization,
                                                   User user, NotificationDTO notification) {
        logger.debug("[+] Authorizing ProcessTransactionRequest with ID: {} " , requestId);
        ProcessTransactionRequestDTO processTransactionRequestDTO = new Gson().fromJson(request.getNewRecord(), ProcessTransactionRequestDTO.class);
        if (processTransactionRequestDTO != null) {
            Optional<TransactionRequest> transactionRequestById = Optional.ofNullable(transactionRequestRepository.findById(processTransactionRequestDTO.getRequestId())).get();
            if (transactionRequestById.isPresent()) {
                TransactionRequest transactionRequest = transactionRequestById.get();
                List<Transaction> transactionDataList = transactionRequest.getTransactionData();
                List<TransactionMaster> transactionMasterDataList = transactionRequest.getTransactionMasterData();
                try{
                    notification = TransactionUtil.getDataOwnerEmailsAndPhoneNumbers(transactionDataList, transactionMasterDataList);
                }catch( Exception e ){
                    logger.debug("Exception happened in {} ", e);
                }
                if (ApplicationConstant.APPROVAL_ACTIONS[1].equalsIgnoreCase(requestAuthorization.getAction())) {
                    request = Utils.setRequestStatus(ApprovalStatus.REJECTED.getCode(), user.getId(),
                            requestAuthorization.getRejectionReason(), request);
                    request.setApprovedOn(new Date());
                    ModuleRequest rejectedModuleRequest = moduleRequestService.save(request);
                    if (rejectedModuleRequest != null) {
                        logger.debug("[-] Rejected Process Transaction Request {} because  {} " , requestId);
                        notificationService.sendRejectedApproval(notification);
                    }
                } else {
                    List<ShareHolder> shareHolders = new ArrayList<>();
                    if (transactionRequestById.isPresent()) {
						transactionRequest = transactionRequestById.get();
						List<String> dataOwnerEmails = new ArrayList<>();
						List<String> taxExemptionKeywords = new ArrayList<>();
						logger.debug("Getting temp shareholder for system persistence");
						List<ShareHolderTemp> allShareHolderTempList = shareHolderTempService
								.findAllShareHolderTempList(transactionRequest);
						List<TaxExemptionValidator> taxExeptionValidator = taxExemptionService.getTaxExemptionValidator();
						if (taxExeptionValidator != null && !taxExeptionValidator.isEmpty()
								&& !Utils.isEmptyString(taxExeptionValidator.get(0).getKeyWords())) {
							taxExemptionKeywords = Arrays.asList(taxExeptionValidator.get(0).getKeyWords().toUpperCase().split("\\s*,\\s*"));

						}
                        if(allShareHolderTempList != null && !allShareHolderTempList.isEmpty()) {
                        	for(ShareHolderTemp n: allShareHolderTempList) {
                        		if(!taxExemptionKeywords.isEmpty()) {
                        			String firstName = Utils.isEmptyString(n.getFirstName()) ? null : n.getFirstName();
                        			String middleName = Utils.isEmptyString(n.getMiddleName()) ? null : n.getMiddleName();
                        			String lastName = Utils.isEmptyString(n.getLastName()) ? null : n.getLastName();
									if ( (!Utils.isEmptyString(firstName)  && taxExemptionKeywords.contains(firstName.toUpperCase())
										|| (!Utils.isEmptyString(middleName) && taxExemptionKeywords.contains(middleName.toUpperCase())
										|| (!Utils.isEmptyString(lastName) && taxExemptionKeywords.contains(lastName.toUpperCase())))) ) {
										n.setTaxExemption(true);
									}
                            	}
                                ShareHolder shareHolder = new ShareHolder();
                                BeanUtils.copyProperties(n, shareHolder);
                                Holderkin holderkin = new Holderkin();
                                holderkin.setKinName(n.getKinName());
                                holderkin.setStatus(true);
                                holderkin.setKinAddress(n.getKinAddress());
                                holderkin.setKinCountry(n.getKinCountry());
                                holderkin.setKinEmail(n.getKinEmail());
                                holderkin.setKinLga(n.getKinLga());
                                holderkin.setKinPhone(n.getKinPhone());
                                holderkin.setKinState(n.getStateOfOrigin());
                                holderkin.setShareHolder(shareHolder);
                                shareHolder.setHolderkin(holderkin);
                                dataOwnerEmails.add(shareHolder.getEmail());
                                shareHolders.add(shareHolder);
                        	}
                        }
                        logger.debug(">>> Persisting ShareHolders >>>");
                        //ToDo comment this part for test
                        shareHolderService.saveAll(shareHolders);
                        logger.debug("[+] Persisted Shareholders with no error");
                        transferUnit(transactionRequest);

                    }
                    request = Utils.setRequestStatus(ApprovalStatus.ACCEPTED.getCode(), user.getId(),
                            null, request);
                    request.setApprovedOn(new Date());
                    ModuleRequest approvedModuleRequest = moduleRequestService.save(request);
                    if (approvedModuleRequest != null) {
                        logger.debug("[+] Accepted Process Transaction Request {}" , requestId);
                        notificationService.sendAcceptedApproval(notification);
                    }

                }
            }

        }
    }

    @Override
    public void transferUnit(TransactionRequest transactionRequest) {
        transferUnitToBuyer(transactionRequest);
    }

    @Transactional
    void transferUnitToBuyer(TransactionRequest transactionRequest) {
        logger.debug(">>> Transferring Unit With {} >>>", transactionRequest.toString());
        List<Transaction> transactionData = transactionRequest.getTransactionData();
        List<Transaction> sellerTransactions = transactionData.stream().filter(t -> TransactionUtil.isSellerTransaction(t)).collect(Collectors.toList());
        sellerTransactions.stream().forEach(sellerTransaction -> {
                    ProcessedTransaction pt = sellerTransaction.getProcessedTransaction();
                    if (pt != null && (!pt.getStatus().equalsIgnoreCase(ProcessStatus.COMPLETED.toString()))) {
                        ShareHolder buyer = shareHolderService.findByClearingHousingNumber(pt.getBuyerChn()).get();
                        if (sellerTransaction.getUnit() > 0) {
                            if (pt.isProcessed() && pt.isReadyForTransfer()) {
                                List<SupportAccount> supportAccountData = pt.getSupportAccountData();
                                if (pt.isAddAccountRequired() && supportAccountData.size() > 0) {
                                    logger.debug(">>> Processing Transaction with {} added account >>>", supportAccountData.size());
                                    supportAccountData.stream().forEach(supportAccount -> {
                                        ShareHolder seller = shareHolderService.findByClearingHousingNumberAndClientComp(supportAccount.getChn(), Long.parseLong(supportAccount.getClientCompany())).get();
                                        logger.debug(">>> Unit to Receive for Transaction {} is  >>>", pt.getUnitToReceive());
                                        if (pt.getUnitToReceive() > 0) {
                                            logger.debug(">>> Transaferring {}Units Between Seller{} and Buyer: {} >>>", pt.getUnitToReceive(), sellerTransaction.getCHN(), pt.getBuyerChn());
                                            if (supportAccount.getSupportUnit() >= pt.getUnitToReceive()) {
                                                buyer.setShareUnit(buyer.getShareUnit() + pt.getUnitToReceive());
                                                seller.setShareUnit(seller.getShareUnit() - pt.getUnitToReceive());
                                                supportAccount.setSupportUnit(supportAccount.getSupportUnit() - pt.getUnitToReceive());
                                            } else {
                                                buyer.setShareUnit(buyer.getShareUnit() + pt.getUnitToReceive());
                                                seller.setShareUnit(seller.getShareUnit() - pt.getUnitToReceive());
                                                pt.setUnitToReceive(pt.getUnitToReceive() - supportAccount.getSupportUnit());
                                            }
                                            shareHolderService.save(seller);
                                        }
                                    });
                                } else {
                                    logger.debug(">>> Processing Transaction with no added account >>>");
                                    ShareHolder seller = shareHolderService.findByClearingHousingNumberAndClientComp(sellerTransaction.getCHN(),
                                            Long.parseLong(sellerTransaction.getTransactionRequest().getClientCompany())).get();
                                    logger.debug(">>> Seller Unit >>> {}", seller.getShareUnit());
                                    if(seller.getShareUnit() > 0){
                                        buyer.setShareUnit(buyer.getShareUnit() + pt.getUnitToReceive());
                                        seller.setShareUnit(seller.getShareUnit() - pt.getUnitToReceive());
                                    }
                                    logger.debug("[+] Transfer {} Share Unit with No exception between {} and {}",
                                            pt.getUnitToReceive(), seller.getClearingHousingNumber(), buyer.getClearingHousingNumber());
                                    shareHolderService.save(seller);
                                }
                                shareHolderService.save(buyer);
                                pt.setStatus(ProcessStatus.COMPLETED.toString());
                                processTransactionRepository.save(pt);
                            }else{
                                logger.debug("[-] Seller transaction is not processed or not ready for transfer");
                            }
                        }else {
                            logger.debug("[-] Seller has zero or less than unit to complete transaction {}", sellerTransaction.getTransactionId());
                        }
                    }else{
                        logger.debug("[-] Seller does not have processed details or transaction is COMPLETED");
                    }
                }
        );
    }

	@Override
	public List<ShareHolderTemp> getTransactionRequestShareHoldersTemp(ModuleRequest request) {
		
		List<ShareHolderTemp> result = new ArrayList<>();
		ProcessTransactionRequestDTO processTransactionRequestDTO = new Gson().fromJson(request.getNewRecord(), ProcessTransactionRequestDTO.class);
        if (processTransactionRequestDTO != null) {
        	Optional<TransactionRequest> transactionRequestById = Optional.ofNullable(transactionRequestRepository.findById(processTransactionRequestDTO.getRequestId())).get();
            if (transactionRequestById.isPresent()) {
            	result = shareHolderTempService.findAllShareHolderTempList(transactionRequestById.get());
            }
        }
		return result;
	}

	@Override
	public List<ShareHolderTemp> addTaxExemptionToShareHoldersTemp(List<Long> shareHolderTempIds,
			boolean taxExemptionOption) {
		List<ShareHolderTemp> result = new ArrayList<>();
		shareHolderTempIds.forEach(itemId -> {
			ShareHolderTemp temp = shareHolderTempService.getShareHolderTempById(itemId);
			if(temp != null) {
				temp.setTaxExemption(taxExemptionOption);
				temp = shareHolderTempService.saveShareHolderTemp(temp);
				result.add(temp);
			}
		});
		
		return result;
	}
}
