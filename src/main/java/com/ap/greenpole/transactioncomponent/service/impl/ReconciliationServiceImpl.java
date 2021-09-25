package com.ap.greenpole.transactioncomponent.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ap.greenpole.transactioncomponent.dto.CancelledSuspendedTransactionsDto;
import com.ap.greenpole.transactioncomponent.dto.NotificationDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransAuthorizeDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransDTO;
import com.ap.greenpole.transactioncomponent.dto.ReconcileSuspendedTransManualDTO;
import com.ap.greenpole.transactioncomponent.dto.RequestAuthorizationDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.ProcessedTransaction;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.repository.TransactionRepository;
import com.ap.greenpole.transactioncomponent.service.ApprovalRequestService;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.service.ProcessTransactionService;
import com.ap.greenpole.transactioncomponent.service.ReconciliationService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderTempService;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
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
public class ReconciliationServiceImpl implements ReconciliationService {

    final static Logger logger = LoggerFactory.getLogger(ReconciliationServiceImpl.class);


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ShareHolderService shareHolderService;

    @Autowired
    private ShareHolderTempService shareHolderTempService;

    @Autowired
    private ModuleRequestService moduleRequestService;

    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private ApprovalRequestService approvalRequestService;
    
    @Autowired
    private TransactionService transactionService;


    @Override
    public List<ReconcileSuspendedTransDTO> getSuspendedTransactionForReconciliation(Map<String, Object> queryData) {
        List<ReconcileSuspendedTransDTO> suspendedTransactions = new ArrayList<>();
        String clientCompanyName = (String) queryData.get("client_company_name");
        String clientCompanyCode = (String) queryData.get("client_company_code");
        String fromShareHolderName = (String) queryData.get("from_shareholder_name");
        String toShareHolderName = (String) queryData.get("to_shareholder_name");
        String fromShareHolderChn = (String) queryData.get("from_shareholder_chn");
        String toShareHolderChn = (String) queryData.get("to_shareholder_chn");
        String buyerCSCSTransactionId = (String) queryData.get("buyer_cscs_transactionid");
        String sellerCSCSTransactionId = (String) queryData.get("seller_cscs_transactionid");
        Long fromShareHolderCompanyAccount = Long.valueOf((String) queryData.get("from_shareholder_company_account"));
        Long toShareHolderCompanyAccount = Long.valueOf((String) queryData.get("to_shareholder_company_account"));
        List<Object[]> supendedTransactionsObject = transactionRepository.findSupendedTransactions(
                "%" + clientCompanyName + "%", "%" + clientCompanyCode + "%",
                "%" + fromShareHolderName + "%", "%" + toShareHolderName + "%",
                "%" + fromShareHolderChn + "%", "%" + toShareHolderChn + "%",
                "%" + buyerCSCSTransactionId + "%", "%" + sellerCSCSTransactionId + "%",
                fromShareHolderCompanyAccount, toShareHolderCompanyAccount);
        if (supendedTransactionsObject.size() > 0) {
            for (Object[] eachObject : supendedTransactionsObject) {
                ReconcileSuspendedTransDTO reconcileSuspendedTransDTO = new ReconcileSuspendedTransDTO();
                reconcileSuspendedTransDTO.setCscsTransId(String.valueOf(eachObject[0]));
                reconcileSuspendedTransDTO.setDate((Date) eachObject[1]);
                reconcileSuspendedTransDTO.setTransDate((Date) eachObject[2]);
                reconcileSuspendedTransDTO.setSeller((String) eachObject[6]);
                reconcileSuspendedTransDTO.setBuyer((String) eachObject[7]);
                reconcileSuspendedTransDTO.setClientCompany((String) eachObject[3]);
                reconcileSuspendedTransDTO.setUnit(String.valueOf(eachObject[4]));
                reconcileSuspendedTransDTO.setReason(String.valueOf(eachObject[5]));
                reconcileSuspendedTransDTO.setCancel((Boolean) eachObject[8]);
                suspendedTransactions.add(reconcileSuspendedTransDTO);
            }
        }
        return suspendedTransactions;
    }
    
    @Override
	public List<CancelledSuspendedTransactionsDto> searchCancelledSuspendedTransactions(Map<String, Object> queryData) throws Exception {
    	List<CancelledSuspendedTransactionsDto> cancelledSuspendedTransactionsResult = new ArrayList<>();
    	Date transactionDate = (Date) queryData.get("transaction_date");
        long clientCompany = Long.parseLong(String.valueOf(queryData.get("client_company")));
		
//		List<Object[]> cancelledSuspendedTransactions = transactionRepository.findCancelledSuspendedTransactions(transactionDate, clientCompany);
		List<Object[]> cancelledSuspendedTransactions = transactionRepository.findCancelledSuspendedTransactions(clientCompany);

		if(cancelledSuspendedTransactions != null && !cancelledSuspendedTransactions.isEmpty()) {
			CancelledSuspendedTransactionsDto cancelledSuspendedTransactionsDto = new CancelledSuspendedTransactionsDto();
			for(Object[] obj: cancelledSuspendedTransactions) {
				cancelledSuspendedTransactionsDto.setCscsTransactionId((String) obj[0]);
                cancelledSuspendedTransactionsDto.setTransactionType((String) obj[1]);
                cancelledSuspendedTransactionsDto.setTransactionDate((Date) obj[2]);
				cancelledSuspendedTransactionsDto.setClientCompany((String) obj[3]);
                cancelledSuspendedTransactionsDto.setUnitTransacted(String.valueOf(obj[4]));
                cancelledSuspendedTransactionsDto.setSuspensionReason((String) obj[5]);
                cancelledSuspendedTransactionsDto.setSeller((String) obj[6]);
				cancelledSuspendedTransactionsDto.setBuyer((String) obj[7]);
				cancelledSuspendedTransactionsDto.setCancelled((Boolean) obj[8]);
                cancelledSuspendedTransactionsDto.setSuspendedDate((Date) obj[9]);
                cancelledSuspendedTransactionsResult.add(cancelledSuspendedTransactionsDto);
			}
		}

		return cancelledSuspendedTransactionsResult;
	}

    @Override
    public ModuleRequest reconcileSuspendedTransactionRequest(ReconcileSuspendedTransAuthorizeDTO reconcileSuspendedTransDTO, User user) {
        logger.info("[+] Inside reconcileSuspendedTransactionRequest() with {}", reconcileSuspendedTransDTO.toString());
        ModuleRequest pendingApprovalRequest = approvalRequestService.createPendingApprovalRequest(reconcileSuspendedTransDTO, user, ApplicationConstant.REQUEST_TYPES[1], new NotificationDTO());
        return pendingApprovalRequest;
    }

    @Override
    public void authorizeReconciliationSuspendedTransactionRequest(RequestAuthorizationDTO requestAuthorization, User user) {
        approvalRequestService.authorizeRequest(requestAuthorization, user, ApplicationConstant.REQUEST_TYPES[1]);
    }

    @Override
    public ModuleRequest reconcileSuspendedTransactionManualRequest(ReconcileSuspendedTransManualDTO reconcileSuspendedTransDTO, User user) {
        logger.info("[+] Inside reconcileSuspendedTransactionManualRequest() with {}", reconcileSuspendedTransDTO.toString());
        ModuleRequest pendingApprovalRequest = approvalRequestService.createPendingApprovalRequest(reconcileSuspendedTransDTO, user, ApplicationConstant.REQUEST_TYPES[3], new NotificationDTO());
        return pendingApprovalRequest;
    }

    @Override
    public void authorizeReconciliationSuspendedTransactionManualRequest(RequestAuthorizationDTO requestAuthorization, User user) {
        approvalRequestService.authorizeRequest(requestAuthorization, user, ApplicationConstant.REQUEST_TYPES[3]);
    }

    private void authorizeReconSuspensionManualRequest(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user) {
        ReconcileSuspendedTransDTO reconcileSuspendedTransDTO = new Gson().fromJson(request.getNewRecord(), ReconcileSuspendedTransDTO.class);
        if (reconcileSuspendedTransDTO != null) {
            if (ApplicationConstant.APPROVAL_ACTIONS[1].equalsIgnoreCase(requestAuthorization.getAction())) {
                request = Utils.setRequestStatus(ApprovalStatus.REJECTED.getCode(), user.getId(),
                        requestAuthorization.getRejectionReason(), request);
                request.setApprovedOn(new Date());
                ModuleRequest rejectedModuleRequest = moduleRequestService.save(request);
                if (rejectedModuleRequest != null) {
                    //Send Reject Notification
                }
            } else {
                boolean unitIsTransafered = false; //processRequest(reconcileSuspendedTransDTO, request);
                if (unitIsTransafered) {
                    request = Utils.setRequestStatus(ApprovalStatus.ACCEPTED.getCode(), user.getId(),
                            null, request);
                    request.setApprovedOn(new Date());
                    ModuleRequest approvedModuleRequest = moduleRequestService.save(request);
                    if (approvedModuleRequest != null) {
                        //Send Approved Notification
                    }
                }
            }
        }
    }

    @Override
    public void authorizeReconSuspensionRequest(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user, boolean isManual, NotificationDTO notification) {

        if(isManual){
            ReconcileSuspendedTransManualDTO reconcileSuspendedTransDTO = new Gson().fromJson(request.getNewRecord(), ReconcileSuspendedTransManualDTO.class);
            if (reconcileSuspendedTransDTO != null) {
                if (ApplicationConstant.APPROVAL_ACTIONS[3].equalsIgnoreCase(requestAuthorization.getAction())) {
                    transactionUtil.sendRejectionNotification(request, requestAuthorization, user, notification);
                } else {
                    processRequestManual(reconcileSuspendedTransDTO);
                    transactionUtil.sendApprovalNotification(request, user, notification);
                }
            }
        }else {
            ReconcileSuspendedTransAuthorizeDTO reconcileSuspendedTransDTO = new Gson().fromJson(request.getNewRecord(), ReconcileSuspendedTransAuthorizeDTO.class);
            if (reconcileSuspendedTransDTO != null) {
                if (ApplicationConstant.APPROVAL_ACTIONS[1].equalsIgnoreCase(requestAuthorization.getAction())) {
                    transactionUtil.sendRejectionNotification(request, requestAuthorization, user, notification);
                } else {
                    processRequest(reconcileSuspendedTransDTO);
                    transactionUtil.sendApprovalNotification(request, user, notification);
                }
            }
        }

    }

    private void processRequest(ReconcileSuspendedTransAuthorizeDTO reconcileSuspendedTransDTO) {
        List<ProcessedTransaction> processedTransactionList = new ArrayList<>();
        if (ApplicationConstant.RECONILIATION_ACTIONS[1].equalsIgnoreCase(reconcileSuspendedTransDTO.getAction())) {
            reconcileSuspendedTransDTO.getCscsTransId().forEach(rstd -> {
                List<Transaction> byTransactionId = transactionRepository.findByTransactionId(rstd);
                List<Transaction> selleTransactions = byTransactionId.stream().filter(TransactionUtil::isSellerTransaction).collect(Collectors.toList());
                selleTransactions.stream().forEach(st -> {
                    ProcessedTransaction processedTransaction = st.getProcessedTransaction();
                    processedTransaction.setCancelled(true);
                    processedTransactionList.add(processedTransaction);
                });
            });
            processTransactionService.saveAll(processedTransactionList);
        }
        if (ApplicationConstant.RECONILIATION_ACTIONS[0].equalsIgnoreCase(reconcileSuspendedTransDTO.getAction())) {
            reconcileSuspendedTransDTO.getCscsTransId().forEach(rstd -> {
                List<Transaction> byTransactionId = transactionRepository.findByTransactionId(rstd);
                byTransactionId.stream().forEach(st -> processTransactionService.transferUnit(st.getTransactionRequest()));
            });
        }
    }

    private void processRequestManual(ReconcileSuspendedTransManualDTO reconcileSuspendedTransDTO) {
        List<Transaction> byTransactionId = transactionRepository.findByTransactionId(reconcileSuspendedTransDTO.getSellerCscsTransId());
        if (byTransactionId.size() > 0) {
            List<Transaction> selleTransactions = byTransactionId.stream().filter(TransactionUtil::isSellerTransaction).collect(Collectors.toList());
            Transaction transaction = selleTransactions.get(0);
            if(transaction.getTransactionRequest().getClientCompany() == reconcileSuspendedTransDTO.getClientCompany()){
                Map<String, Object> acountInfo = new HashMap<>();
                acountInfo.put("client_company", reconcileSuspendedTransDTO.getClientCompany());
                acountInfo.put("account_number", reconcileSuspendedTransDTO.getSourceAcctNumber());
                Optional<ShareHolder> sourceAccountHolder = Optional.ofNullable(shareHolderService.findDetailsByClientCompAndAcctNo(acountInfo));
                if(sourceAccountHolder.isPresent()){
                    Optional<ShareHolder> destinationAccountHolder = shareHolderService.findByAccountNumber(Long.valueOf(reconcileSuspendedTransDTO.getDestinationAcctNumber()));
                    if(destinationAccountHolder.isPresent()){
                        ProcessedTransaction processedTransaction = transaction.getProcessedTransaction();
                        if(processedTransaction.isProcessed() && processedTransaction.isSuspended()){
                            processedTransaction.setReadyForTransfer(true);
                            processedTransaction.setAddAccountRequired(false);
                            transaction.setProcessedTransaction(processedTransaction);
                            processTransactionService.transferUnit(transaction.getTransactionRequest());
                        }else{
                            logger.info("[-] Transaction is not processed or Suspended");
                        }
                    }else{
                        logger.info("[-] Destination Account %s does not exist on System", reconcileSuspendedTransDTO.getDestinationAcctNumber());
                    }
                }else{
                    logger.info("[-] Source Account %s with Client Company %s does not exist", reconcileSuspendedTransDTO.getSourceAcctNumber(), reconcileSuspendedTransDTO.getClientCompany());
                }
            } else {
                logger.info("[-] Client Company is not associated with CSCS transaction ID");
            }
        } else {
            logger.info("[-] Seller CSCS Transaction ID %s does not have transaction int the System ", reconcileSuspendedTransDTO.getSellerCscsTransId());
        }

    }

	@Override
	public Object cancelSuspendedTransactions(List<String> cscsTransactionIds, boolean action) {
		List<String> results = new ArrayList<>();
		for (String cscsTransId : cscsTransactionIds) {
			List<Transaction> transactionList = transactionService.findByTransactionId(cscsTransId);
			if (transactionList == null || transactionList.isEmpty()) {
				results.add("Transaction details for [ " + cscsTransId + " ] not found");
			} else {
				Transaction transaction = null;
				for (Transaction t : transactionList) {
					if ("-".equalsIgnoreCase(t.getSellOrBuy()))
						transaction = t;
				}
				if (transaction == null) {
					results.add("\"Transaction details for [ " + cscsTransId + " ] not found\"");
				} else {
					ProcessedTransaction pt = transaction.getProcessedTransaction();
					if (pt == null) {
						results.add("[ " + cscsTransId + " ] Transaction has not been processed");
					} else {
						if (!pt.isSuspended()) {
							results.add("[ " + cscsTransId
									+ " ] Transaction is not suspended");
						} else {
							pt.setCancelled(action);
							String state = action ? "CANCELLED" : "REVERSED";
							results.add("[ " + cscsTransId + " ] has been " + state);
							transaction.setProcessedTransaction(pt);
							transactionService.save(transaction);
						}
					}
				}
			}
		}
		return results;
	}
}
