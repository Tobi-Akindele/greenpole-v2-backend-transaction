package com.ap.greenpole.transactioncomponent.service.impl;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ap.greenpole.transactioncomponent.dto.*;
import com.ap.greenpole.transactioncomponent.entity.*;
import com.ap.greenpole.transactioncomponent.enums.ProcessStatus;
import com.ap.greenpole.transactioncomponent.service.*;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.repository.TransactionRequestRepository;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.model.User;
import com.google.gson.Gson;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:25 PM
 */
@Service
public class TransactionRequestServiceImpl implements TransactionRequestService {

    final static Logger logger = LoggerFactory.getLogger(TransactionRequestServiceImpl.class);

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
    private ModuleRequestService moduleRequestService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMasterService masterService;

    @Autowired
    private ShareHolderService shareHolderService;

    @Autowired
    private ApprovalRequestService approvalRequestService;

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private ReconciliationService reconciliationService;

    private Gson gson = new Gson();


    @Override
    public TransactionRequest save(TransactionRequest transaction) {
        return transactionRequestRepository.save(transaction);
    }


    @Override
    public TransactionRequest getTransactionRequestById(Long id) {
        Optional<TransactionRequest> byId = transactionRequestRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Override
    public Page<TransactionRequest> getAllTransactionRequestPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transactionRequestRepository.findAll(pageable);
    }

    @Override
    public Page<TransactionRequest> getTransactionForProcessing(Map<String, Object> queryData, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String clientCompany = (String) queryData.get("clientCompany");
        Date transactionDate = (Date) queryData.get("transactionDate");
        Date uploadDate = (Date) queryData.get("uploadDate");
        Page<TransactionRequest> byClientCompanyOrTransactionDateOrUploadDateLike = transactionRequestRepository
                .findAllTransactionRequestByTransactionDateOrClientCompanyLikeOrUploadDate(transactionDate,
                        "%" + clientCompany + "%", uploadDate, pageable);
        return byClientCompanyOrTransactionDateOrUploadDateLike;
    }

    @Override
    public ModuleRequest uploadTransactionRequest(ModuleRequestDTO moduleRequestDTO, User user) {
        NotificationDTO notification = new NotificationDTO();
        ModuleRequest pendingApprovalRequest = approvalRequestService.createPendingApprovalRequest(moduleRequestDTO, user, ApplicationConstant.REQUEST_TYPES[0], notification);
        return pendingApprovalRequest;
    }

    @Override
    public void authorizeRequest(RequestAuthorizationDTO requestAuthorization, User user) {
        approvalRequestService.authorizeRequest(requestAuthorization, user, ApplicationConstant.REQUEST_TYPES[0]);
    }


    @Override
    public AnalyseTransactionDTO analyseTransaction(Long id) {
        logger.info("Analysing Transaction with id: {}", id);
        AnalyseTransactionDTO analyseTransactionDTO = new AnalyseTransactionDTO();
        TransactionRequest transactionById = getTransactionRequestById(id);
        if (transactionById == null) {
            return null;
        }
        List<Transaction> transactionList = transactionService.findByTransactionRequestId(transactionById.getId());
        long totalUnitBought = transactionList.stream().filter(t -> TransactionUtil.isBuyerTransaction(t)).mapToLong(s -> s.getUnit()).sum();
        long totalUnitSold = transactionList.stream().filter(t -> TransactionUtil.isSellerTransaction(t)).mapToLong(s -> s.getUnit()).sum();
        analyseTransactionDTO.setTotalUnitSold(totalUnitSold);
        analyseTransactionDTO.setTotalUnitPurchased(totalUnitBought);
        analyseTransactionDTO.setUnitVariance(analyseTransactionDTO.getTotalUnitSold() - analyseTransactionDTO.getTotalUnitPurchased());
        List<Seller> sellers = new ArrayList<>();
        Map<String, Seller> sellerMap = new HashMap();
        Map<String, List<String>> sellerTransactionIdMap = new HashMap();
        transactionList.stream().forEach(t -> {
            if (TransactionUtil.isSellerTransaction(t)) {
                if (sellerTransactionIdMap.containsKey(t.getCHN())) {
                    List<String> ids = sellerTransactionIdMap.get(t.getCHN());
                    ids.add(t.getTransactionId());
                } else {
                    List<String> transId = new ArrayList<>();
                    transId.add(t.getTransactionId());
                    sellerTransactionIdMap.put(t.getCHN(), transId);
                }
            }

        });
        System.out.println(">>>>>>>>>> " + sellerTransactionIdMap);
        sellerTransactionIdMap.forEach((sellerChn, sellerTransIds) -> {
            TransactionMaster transactionMaster = masterService.findByChnAndTransactionRequest(sellerChn, transactionById.getId());
            Seller seller = new Seller();
            seller.setCHN(transactionMaster.getCHN());
            seller.setName(transactionMaster.getNames());
            List<Buyer> buyers = new ArrayList<>();
            sellerTransIds.stream().forEach(t -> {
                Transaction byTransactionIdAndSellOrBuy = transactionService.findByTransactionIdAndSellOrBuy(t, "+");
                Buyer buyer = new Buyer();
                TransactionMaster transactionMaster2 = masterService.findByChnAndTransactionRequest(byTransactionIdAndSellOrBuy.getCHN(), transactionById.getId());
                buyer.setCHN(transactionMaster2.getCHN());
                buyer.setName(transactionMaster2.getNames());
                seller.setTotalUnitPurchased(seller.getTotalUnitPurchased() + Long.valueOf(byTransactionIdAndSellOrBuy.getUnit()));
                buyers.add(buyer);
            });
            List<Transaction> byCHNAndTransactionRequestAndSellOrBuy = transactionService.findByCHNAndTransactionRequestAndSellOrBuy(sellerChn, transactionById.getId(), "-");
            long totalUnitSellerSold = byCHNAndTransactionRequestAndSellOrBuy.stream().filter(t -> TransactionUtil.isBuyerTransaction(t)).mapToLong(s -> s.getUnit()).sum();
            seller.setTotalUnitSold(seller.getTotalUnitSold() + totalUnitSellerSold);
            seller.setBuyerList(buyers);
            seller.setUnitVariance(seller.getTotalUnitSold() - seller.getTotalUnitPurchased());
            sellers.add(seller);
        });
//        transactionList.stream().forEach(
//                transaction -> {
//                    TransactionMaster transactionMaster = masterService.findByChn(transaction.getCHN());
//                    Seller seller = new Seller();
//                    if(sellerMap.containsKey(transaction.getCHN())){
//                        seller = sellerMap.get(transaction.getCHN());
//                        if (TransactionUtil.isBuyerTransaction(transaction)) {
//                            Buyer buyer = new Buyer();
//                            buyer.setCHN(transactionMaster.getCHN());
//                            buyer.setName(transactionMaster.getNames());
//                            buyers.add(buyer);
//                            sellerMap.put(transaction.getCHN(), seller);
//                        }
//                    }else {
//                        if (TransactionUtil.isSellerTransaction(transaction)) {
//                            seller.setCHN(transactionMaster.getCHN());
//                            seller.setName(transactionMaster.getNames());
//                            seller.setTotalUnitSold(seller.getTotalUnitSold() + Long.valueOf(transaction.getUnit()));
//                        }else {
//                            Buyer buyer = new Buyer();
//                            buyer.setCHN(transactionMaster.getCHN());
//                            buyer.setName(transactionMaster.getNames());
//                            buyers.add(buyer);
//                            seller.setTotalUnitPurchased(seller.getTotalUnitPurchased() + Long.valueOf(transaction.getUnit()));
//                        }
//                        sellerMap.put(transaction.getCHN(), seller);
//                    }
//                    seller.setBuyerList(buyers);
//                    seller.setUnitVariance(seller.getTotalUnitSold() - seller.getTotalUnitPurchased());
//                    sellers.add(seller);
//                }
//        );
        analyseTransactionDTO.setSellers(sellers);
        logger.info("Sellers size is {}", sellers.size());
        return analyseTransactionDTO;
    }

    @Override
    public ProcessTransactionResponseDTO processTransaction(ProcessTransactionRequestDTO processTransactionRequestDTO, User user) {
        logger.info("[+] User: {}  is processing transaction ", user.getFirstName());
        ProcessTransactionResponseDTO processTransactionResponseDTO = new ProcessTransactionResponseDTO();
        Optional<TransactionRequest> transactionRequestById = Optional.ofNullable(getTransactionRequestById(processTransactionRequestDTO.getRequestId()));
        if (transactionRequestById.isPresent()) {
            TransactionRequest transactionRequest = transactionRequestById.get();
            List<Transaction> transactionDataList = transactionRequest.getTransactionData();
            List<TransactionMaster> transactionMasterDataList = transactionRequest.getTransactionMasterData();
            Map<String, Object> masterRecordProcessSummary = getMasterRecordProcessSummary(transactionMasterDataList);
            processTransactionResponseDTO.setMasterTransactionDTO((ProcessMasterTransactionDTO) masterRecordProcessSummary.get("processMasterTransactionDTO"));
            processTransactionResponseDTO.setProcessMasterTransactionResponseDTOList((List<ProcessMasterTransactionResponseDTO>) masterRecordProcessSummary.get("processMasterTransactionResponseDTOList"));
            Map<String, Object> transactionRecordProcessSummary = getTransactionRecordProcessSummary(transactionDataList, transactionMasterDataList, user);
            processTransactionResponseDTO.setProcessTransactionRecordDTOS((List<ProcessTransactionRecordDTO>) transactionRecordProcessSummary.get("processTransactionRecordDTOList"));
            Boolean isProcessedTransactionSave = (Boolean) transactionRecordProcessSummary.get("isProcessedTransactionSave");
            if (isProcessedTransactionSave) {
                NotificationDTO notification = TransactionUtil.getDataOwnerEmailsAndPhoneNumbers(transactionDataList, transactionMasterDataList);
                approvalRequestService.createPendingApprovalRequest(processTransactionRequestDTO, user, ApplicationConstant.REQUEST_TYPES[2], notification);
            }
            return processTransactionResponseDTO;
        } else {
            logger.info("[-] No Record found for Transaction ID; {}", processTransactionRequestDTO.getRequestId());
        }
        return processTransactionResponseDTO;
    }

    @Override
    public List<QueryAccountDTO> queryAccount(Map<String, Object> queryData) {
        List<QueryAccountDTO> queryAccountDTOList = new ArrayList<>();
        QueryAccountDTO queryAccountDTO = new QueryAccountDTO();
        String chn = (String) queryData.get("chn");
        String name = (String) queryData.get("name");
        long accountNumber = Long.valueOf((String) queryData.get("accountNumber"));
        String oldAccountNumber = (String) queryData.get("oldAccountNumber");
        List<ShareHolder> shareHolders = shareHolderService.queryAccount(chn, name, accountNumber);
        shareHolders.stream().forEach(
                s -> {
                    queryAccountDTO.setClientCompany(s.getClientCompany());
                    queryAccountDTO.setChn(s.getClearingHousingNumber());
                    queryAccountDTO.setFirstName(s.getFirstName());
                    queryAccountDTO.setLastName(s.getLastName());
                    queryAccountDTO.setMiddleName(s.getMiddleName());
                    queryAccountDTO.setAcountNumber(s.getShareHolderId());
                    queryAccountDTO.setUnit(Long.valueOf(s.getShareUnit()));
                    queryAccountDTO.setStatus(s.getStatus());
                    queryAccountDTOList.add(queryAccountDTO);
                }
        );
        return queryAccountDTOList;
    }

    @Override
    @Async
    public void createTransactionComponent(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization,
                                           User user, NotificationDTO notification) {
        TransactionRequest transactionRequest = new TransactionRequest();
        ModuleRequestDTO pendingModuleRequestDTO = gson.fromJson(request.getNewRecord(), ModuleRequestDTO.class);
        if (pendingModuleRequestDTO != null) {
            if (!pendingModuleRequestDTO.isTransactionBalance()) {
                logger.info(">>> {}", pendingModuleRequestDTO.getBalanceTransactionMessage());
            } else {
                logger.info(">>> {}", pendingModuleRequestDTO.getBalanceTransactionMessage());
                BeanUtils.copyProperties(pendingModuleRequestDTO, transactionRequest);
                if (ApplicationConstant.APPROVAL_ACTIONS[1].equalsIgnoreCase(requestAuthorization.getAction())) {
                    request = Utils.setRequestStatus(ApprovalStatus.REJECTED.getCode(), user.getId(),
                            requestAuthorization.getRejectionReason(), request);
                    request.setApprovedOn(new Date());
                    ModuleRequest rejectedModuleRequest = moduleRequestService.save(request);
                    if (rejectedModuleRequest != null) {
                        logger.info("[-] Rejected Transaction {} because  {} ", requestId, requestAuthorization.getRejectionReason());
                        transactionUtil.sendRejectionNotification(request, requestAuthorization, user, notification);
                    }
                } else {
                    createTransaction(transactionRequest, request, pendingModuleRequestDTO);
                    if (transactionRequest != null) {
                        logger.info("[+] Accepted Transaction {}", requestId);
                        request = Utils.setRequestStatus(ApprovalStatus.ACCEPTED.getCode(), user.getId(),
                                null, request);
                        request.setApprovedOn(new Date());
                        ModuleRequest approvedModuleRequest = moduleRequestService.save(request);
                        if (approvedModuleRequest != null) {
                            transactionUtil.sendApprovalNotification(request, user, notification);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Async
    public void authorizeProcessTransactionRequest(ModuleRequest request, Long requestId, RequestAuthorizationDTO requestAuthorization, User user, NotificationDTO notification) {
        processTransactionService.authorizeProcessTransactionRequest(request, requestId, requestAuthorization, user, notification);
    }

    @Override
    @Async
    public void authorizeReconSuspensionRequest(ModuleRequest request, RequestAuthorizationDTO requestAuthorization, User user, NotificationDTO notification, String requestType) {
        if (ApplicationConstant.REQUEST_TYPES[1].equalsIgnoreCase(requestType)) {
            reconciliationService.authorizeReconSuspensionRequest(request, requestAuthorization, user, false, notification);
        }
        if (ApplicationConstant.REQUEST_TYPES[3].equalsIgnoreCase(requestType)) {
            reconciliationService.authorizeReconSuspensionRequest(request, requestAuthorization, user, true, notification);
        }
    }


    private Map<String, Object> getTransactionRecordProcessSummary(List<Transaction> transactionDataList, List<TransactionMaster> transactionMasterDataList, User user) {
        Map<String, Object> responsemap = new HashMap<>();
        List<Transaction> sellerList = transactionDataList.stream().filter(t -> TransactionUtil.isSellerTransaction(t)).collect(Collectors.toList());
        List<Transaction> buyerList = transactionDataList.stream().filter(t -> TransactionUtil.isBuyerTransaction(t)).collect(Collectors.toList());
        BiFunction<List<TransactionMaster>, String, String> nameFromMasterRecord = (masterDataList, chnToFilter) -> {
            Optional<TransactionMaster> transactionMaster = masterDataList.stream().filter(tm -> tm.getCHN().equalsIgnoreCase(chnToFilter)).findFirst();
            if (transactionMaster.isPresent()) {
                TransactionMaster transactionMaster1 = transactionMaster.get();
                return transactionMaster1.getNames();
            }
            return null;
        };
        Function<String, ShareHolder> getShareHolder = (chn) -> {
            Optional<ShareHolder> byClearingHousingNumber = shareHolderService.findByClearingHousingNumber(chn);
            if (byClearingHousingNumber.isPresent()) {
                return byClearingHousingNumber.get();
            }
            return null;
        };
        final Boolean[] isProcessedTransactionSave = {Boolean.FALSE};
        List<ProcessTransactionRecordDTO> processTransactionRecordDTOList = new ArrayList<>();
        sellerList.forEach(
                sellerTransaction -> {
                    ProcessTransactionRecordDTO processTransactionRecordDTO = new ProcessTransactionRecordDTO();
                    Optional<ProcessedTransaction> processedTransactionOptional = Optional.ofNullable(sellerTransaction.getProcessedTransaction());
                    if (processedTransactionOptional.isPresent() && !processedTransactionOptional.get().isSuspended() && !processedTransactionOptional.get().isProcessed()) {
                        ProcessedTransaction processedTransaction = processedTransactionOptional.get();
                        ShareHolder sellerShareHolda = getShareHolder.apply(sellerTransaction.getCHN());
                        ShareHolderDTO sellerShareHolder = new ShareHolderDTO();
                        BeanUtils.copyProperties(sellerShareHolda, sellerShareHolder);
                        if (sellerShareHolder != null) {
                            processTransactionRecordDTO.setSellerChn(sellerTransaction.getCHN());
                            processTransactionRecordDTO.setTransactionRecordId(sellerTransaction.getId());
                            processTransactionRecordDTO.setSellerAccountNumber(String.valueOf(sellerShareHolder.getShareHolderId()));
                            processTransactionRecordDTO.setSellerHoldingUnit(sellerShareHolder.getShareUnit());
                            processTransactionRecordDTO.setSellerSystemName(TransactionUtil.getNameFromShareHolder(sellerShareHolda));
                            processTransactionRecordDTO.setSellerCscsName(nameFromMasterRecord.apply(transactionMasterDataList, (sellerTransaction.getCHN())));
                            ProcessTransactionRecordDTO.Buyer buyer = processTransactionRecordDTO.new Buyer();
                            List<ProcessTransactionRecordDTO.Buyer> listOfSellerBuyers = new ArrayList<>();

                            List<Transaction> buyers = buyerList.stream().filter(b -> b.getTransactionId().equals(sellerTransaction.getTransactionId())).collect(Collectors.toList());
                            if (buyers.size() > 0) {
                                buyers.stream().forEach(buyerTransaction -> {
                                    ShareHolder buyerShareholder = getShareHolder.apply(buyerTransaction.getCHN());
                                    buyer.setBuyerChn(buyerTransaction.getCHN());
                                    buyer.setBuyerHoldingUnit(buyerShareholder.getShareUnit());
                                    if (buyerShareholder != null) {
                                        buyer.setBuyerSystemName(TransactionUtil.getNameFromShareHolder(buyerShareholder));
                                        processedTransaction.setUnitTransacted(buyerTransaction.getUnit());
                                        processedTransaction.setBuyerUnitBefore(buyerTransaction.getUnit());
                                        processedTransaction.setSellerUnitBefore(sellerShareHolder.getShareUnit());
                                        processedTransaction.setBuyerChn(buyerTransaction.getCHN());
                                    }
                                    buyer.setBuyerCscsName(nameFromMasterRecord.apply(transactionMasterDataList, (buyerTransaction.getCHN())));
                                    buyer.setBuyerChn(buyerTransaction.getCHN());
                                    buyer.setSellerTransactionid(sellerTransaction.getTransactionId());
                                    buyer.setBuyerTransactionId(buyerTransaction.getTransactionId());
                                    processedTransaction.setBuyerName(TransactionUtil.getNameFromShareHolder(buyerShareholder));
                                    processedTransaction.setBuyerAccountNumber(buyerShareholder.getShareHolderId());
                                    if (sellerShareHolder.getShareUnit() >= buyerTransaction.getUnit()) {
                                        processedTransaction.setFullTransaction(true);
                                        processedTransaction.setReadyForApproval(true);
                                        processedTransaction.setReadyForTransfer(true);
                                        processedTransaction.setUnitToReceive(buyerTransaction.getUnit());
                                        sellerShareHolder.setShareUnit(sellerShareHolder.getShareUnit() - buyerShareholder.getShareUnit());
                                    } else {
                                        processedTransaction.setSuspended(true);
                                        processedTransaction.setSuspendedOn(new Date());
                                        processedTransaction.setSuspensionReason(ApplicationConstant.LESS_UNIT_TO_SETTLE_BUYER);
                                        processedTransaction.setAddAccountRequired(true);
                                        if (sellerShareHolder.getShareUnit() > 0) {
                                            processedTransaction.setReadyForTransfer(true);
                                            processedTransaction.setPartialTransaction(true);
                                            processedTransaction.setReadyForApproval(true);
                                            processedTransaction.setUnitToReceive(sellerShareHolder.getShareUnit());
                                            processedTransaction.setRemainingUnit(buyerTransaction.getUnit() - sellerShareHolder.getShareUnit());
                                            sellerShareHolder.setShareUnit(buyerShareholder.getShareUnit() - sellerShareHolder.getShareUnit());
                                        } else {
                                            processedTransaction.setReadyForTransfer(false);
                                            processedTransaction.setUnitToReceive(buyerTransaction.getUnit());
                                        }
                                    }
                                    processedTransaction.setBuyerUnitAfter(buyerShareholder.getShareUnit());
                                    processedTransaction.setSellerUnitAfter(sellerShareHolder.getShareUnit());
                                    buyer.setProcessedTransaction(processedTransaction);
                                    listOfSellerBuyers.add(buyer);
                                });
                                processTransactionRecordDTO.setBuyers(listOfSellerBuyers);
                            }
                        } else {
                            logger.info("CHN: {} not in System ", sellerTransaction.getCHN());
                        }
                        processTransactionRecordDTOList.add(processTransactionRecordDTO);
                        processedTransaction.setProcessed(true);
                        processedTransaction.setProcessedOn(new Date());
                        processedTransaction.setStatus(ProcessStatus.PROCESSED.toString());
                        ProcessedTransaction save = processTransactionService.save(processedTransaction);
                        if (save != null) {
                            isProcessedTransactionSave[0] = Boolean.TRUE;
                        }
                    } else {
                        logger.info("Could not process transaction because Transaction is Suspended or record on processedTransaction is {}", processedTransactionOptional.orElse(null));
                    }
                }
        );
        responsemap.put("isProcessedTransactionSave", isProcessedTransactionSave[0]);
        responsemap.put("processTransactionRecordDTOList", processTransactionRecordDTOList);
        return responsemap;
    }

    private Map<String, Object> getMasterRecordProcessSummary(List<TransactionMaster> transactionMasterDataList) {
        Map<String, Object> responsemap = new HashMap<>();
        List<ProcessMasterTransactionResponseDTO> processMasterTransactionResponseDTOList = new ArrayList<>();
        ProcessMasterTransactionDTO masterTransactionDTO = new ProcessMasterTransactionDTO();
        transactionMasterDataList.stream().forEach(
                transactionMaster -> {
                    try {
                        ProcessMasterTransactionResponseDTO processMasterTransactionResponseDTO = new ProcessMasterTransactionResponseDTO();
                        Optional<ShareHolder> systemRecord = shareHolderService.findByClearingHousingNumber(transactionMaster.getCHN());
                        boolean recordIsAffected = false;
                        processMasterTransactionResponseDTO.setCscsChn(transactionMaster.getCHN());
                        if (systemRecord.isPresent()) {
                            ShareHolder shareHolder = systemRecord.get();
                            String firstName = TransactionUtil.getName(transactionMaster.getNames()).get("firstName");
                            String lastName = TransactionUtil.getName(transactionMaster.getNames()).get("lastName");
                            String middleName = TransactionUtil.getName(transactionMaster.getNames()).get("middleName");
                            processMasterTransactionResponseDTO.setType(transactionMaster.getStructure());
                            processMasterTransactionResponseDTO.setMasterRecordId(transactionMaster.getId());
                            if (firstName != null && !firstName.equalsIgnoreCase(shareHolder.getFirstName())) {
                                recordIsAffected = true;
                                processMasterTransactionResponseDTO.setCscsFirstName(firstName);
                                processMasterTransactionResponseDTO.setSystemFirstName(shareHolder.getFirstName());
                                processMasterTransactionResponseDTO.setErrorCode("200");
                                masterTransactionDTO.setMixMatchFirstNameCount(masterTransactionDTO.getMixMatchFirstNameCount() + 1);
                            }
                            if (lastName != null && !lastName.equalsIgnoreCase(shareHolder.getLastName())) {
                                recordIsAffected = true;
                                processMasterTransactionResponseDTO.setCscsLastName(lastName);
                                processMasterTransactionResponseDTO.setSystemLastName(shareHolder.getLastName());
                                processMasterTransactionResponseDTO.setErrorCode("200");
                                masterTransactionDTO.setMixMatchLastNameCount(masterTransactionDTO.getMixMatchLastNameCount() + 1);
                            }
                            if (middleName != null && !middleName.equalsIgnoreCase(shareHolder.getMiddleName())) {
                                recordIsAffected = true;
                                processMasterTransactionResponseDTO.setCscsMiddleName(middleName);
                                processMasterTransactionResponseDTO.setSystemMiddleName(shareHolder.getMiddleName());
                                processMasterTransactionResponseDTO.setErrorCode("200");
                                masterTransactionDTO.setMixMatchMiddleNameCount(masterTransactionDTO.getMixMatchMiddleNameCount() + 1);
                            }
                            if (!transactionMaster.getAddress1().contains(shareHolder.getAddress())) {
                                recordIsAffected = true;
                                processMasterTransactionResponseDTO.setCscsAddress1(transactionMaster.getAddress1());
                                processMasterTransactionResponseDTO.setSystemAddress1(shareHolder.getAddress());
                                processMasterTransactionResponseDTO.setErrorCode("400");
                                masterTransactionDTO.setMixMatchAddress1Count(masterTransactionDTO.getMixMatchAddress1Count() + 1);
                            }
                            if (!isStateSet(transactionMaster, shareHolder)) {
                                recordIsAffected = true;
                                processMasterTransactionResponseDTO.setCscsAdress2(transactionMaster.getAddress2());
                                processMasterTransactionResponseDTO.setCscsAddress3(transactionMaster.getAddress3());
                                processMasterTransactionResponseDTO.setErrorCode("300");
                                masterTransactionDTO.setInvalidStateSetCount(masterTransactionDTO.getInvalidStateSetCount() + 1);
                            }
                        } else {
                            masterTransactionDTO.setChnNotInSystemCount(masterTransactionDTO.getChnNotInSystemCount() + 1);
                        }
                        if (recordIsAffected) {
                            processMasterTransactionResponseDTOList.add(processMasterTransactionResponseDTO);
                        } else {
                            processMasterTransactionResponseDTO.setErrorCode("200");
                        }
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                    logger.info(">>>>> {}", transactionMaster.toString());
                }
        );
        responsemap.put("processMasterTransactionDTO", masterTransactionDTO);
        responsemap.put("processMasterTransactionResponseDTOList", processMasterTransactionResponseDTOList);
        return responsemap;
    }

    private boolean isStateSet(TransactionMaster transactionMaster, ShareHolder shareHolder) {
        boolean isStateValid = false;
        try {
            Optional<String> address2 = Optional.ofNullable(transactionMaster.getAddress2());
            Optional<String> address3 = Optional.ofNullable(transactionMaster.getAddress3());
            Optional<String> stateOfOrigin = Optional.ofNullable(shareHolder.getStateOfOrigin());
            if (address2.isPresent() && stateOfOrigin.isPresent()) {
                if (address2.get().contains(stateOfOrigin.get()))
                    return true;
            }
            if (address3.isPresent() && stateOfOrigin.isPresent()) {
                if (address3.get().contains(stateOfOrigin.get()))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isStateValid;
    }

    private void createTransaction(TransactionRequest transactionRequest, ModuleRequest moduleRequest, ModuleRequestDTO pendingModuleRequestDTO) {
        try {
            transactionService.createTransaction(transactionRequest, moduleRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
