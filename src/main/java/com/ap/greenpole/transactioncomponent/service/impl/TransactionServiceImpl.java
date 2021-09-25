package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.dto.AccountDetailDTO;
import com.ap.greenpole.transactioncomponent.dto.FilterTransactionDTO;
import com.ap.greenpole.transactioncomponent.dto.ModuleRequestDTO;
import com.ap.greenpole.transactioncomponent.entity.*;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.repository.TransactionRepository;
import com.ap.greenpole.transactioncomponent.repository.TransactionRequestRepository;
import com.ap.greenpole.transactioncomponent.service.*;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:52 AM
 */
@Service
public class TransactionServiceImpl extends AbstractTransactionService implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final ShareHolderService shareHolderService;

    private final ShareHolderTempService shareHolderTempService;

    private final ModuleRequestService moduleRequestService;

    private final TransactionMasterService transactionMasterService;

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
    private FileStorageService fileStorageService;


    public TransactionServiceImpl(TransactionRepository transactionRepository, ShareHolderService shareHolderService, ShareHolderTempService shareHolderTempService, ModuleRequestService moduleRequestService, TransactionMasterService transactionMasterService) {
        this.transactionRepository = transactionRepository;
        this.shareHolderService = shareHolderService;
        this.shareHolderTempService = shareHolderTempService;
        this.moduleRequestService = moduleRequestService;
        this.transactionMasterService = transactionMasterService;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> transactions) {
        return transactionRepository.saveAll(transactions);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        logger.info("Getting Transaction with ID: {} ", id);
        Optional<Transaction> byId = transactionRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Override
    public Page<Transaction> getAllTransactionPaginated(int page, int size) {
        logger.info("Getting Paginated Transactions for page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Async
    public void balanceFile(ModuleRequestDTO moduleRequestDTO, ModuleRequest moduleRequest) throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        BeanUtils.copyProperties(moduleRequestDTO, transactionRequest);
        try {
            entryToList(transactionRequest, moduleRequest, true, false);
        } catch (IOException e) {
            logger.info("Exception happened while balancing File");
        }
    }

    @Override
    public void createTransaction(TransactionRequest transactionRequest, ModuleRequest moduleRequest) throws Exception {
        try {
            entryToList(transactionRequest, moduleRequest, false, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findByTransactionRequestId(Long id) {
        return transactionRepository.findAllByTransactionRequestId(id);
    }

    @Override
    public List<FilterTransactionDTO> getFilteredTransactions(Map<String, Object> queryData, int page, int size) {
        logger.info("Filtering Transaction For Processing with {}", queryData);
        List<FilterTransactionDTO> filteredTransaction = new ArrayList<>();
        String clientCompanyName = (String) queryData.get("client_company_name");
        String clientCompanyCode = (String) queryData.get("client_company_code");
        String fromShareHolderName = (String) queryData.get("from_shareholder_name");
        String toShareHolderName = (String) queryData.get("to_shareholder_name");
        String fromShareHolderChn = (String) queryData.get("from_shareholder_chn");
        String toShareHolderChn = (String) queryData.get("to_shareholder_chn");
        String buyerCSCSTransactionId = (String) queryData.get("buyer_cscs_transactionid");
        String sellerCSCSTransactionId = (String) queryData.get("seller_cscs_transactionid");
        Long fromShareHolderCompanyAccount = Long.valueOf(String.valueOf(queryData.get("from_shareholder_company_account")));
        Long toShareHolderCompanyAccount = Long.valueOf(String.valueOf(queryData.get("to_shareholder_company_account")));
        Date dateOn = (Date) queryData.get("date_on");
        Date dateBefore = (Date) queryData.get("date_before");
        Date uploadDate = (Date) queryData.get("uploaded_date");
        Date uploadBetween = (Date) queryData.get("uploaded_between");
        Date uploadAfter = (Date) queryData.get("uploaded_after");
        Pageable pageable = PageRequest.of(page, size);
        List<Object[]> filteredTransactionResponseObj = transactionRepository.findFilteredTransaction(
                "%" + clientCompanyName + "%", "%" + clientCompanyCode + "%",
                "%" + fromShareHolderName + "%", "%" + toShareHolderName + "%",
                "%" + fromShareHolderChn + "%", "%" + toShareHolderChn + "%",
                "%" + buyerCSCSTransactionId + "%", "%" + sellerCSCSTransactionId + "%", fromShareHolderCompanyAccount, toShareHolderCompanyAccount,
                dateOn, dateBefore, uploadDate, uploadAfter, pageable);
        if (filteredTransactionResponseObj.size() > 0) {
            for (Object[] eachObject : filteredTransactionResponseObj) {
                FilterTransactionDTO transactionDTO = new FilterTransactionDTO();
                transactionDTO.setTransactionRequestId(String.valueOf(eachObject[0]));
                transactionDTO.setTransactionType((String) eachObject[1]);
                transactionDTO.setClientCompany((String) eachObject[2]);
                transactionDTO.setTransactionDate((Date) eachObject[3]);
                transactionDTO.setCscsTransactionId((String) eachObject[4]);
                transactionDTO.setSellerChn((String) eachObject[5]);
                transactionDTO.setBuyerChn((String) eachObject[6]);
                transactionDTO.setSellerUnitBefore(String.valueOf(eachObject[7]));
                transactionDTO.setSellerUnitAfter(String.valueOf(eachObject[8]));
                transactionDTO.setBuyerUnitBefore(String.valueOf(eachObject[9]));
                transactionDTO.setBuyerUnitAfter(String.valueOf(eachObject[10]));
                transactionDTO.setActionDate((Date) eachObject[11]);
                transactionDTO.setUnitTransacted(String.valueOf(eachObject[12]));
                transactionDTO.setIsCancelled(String.valueOf(eachObject[13]));
                transactionDTO.setIsProcessed(String.valueOf(eachObject[14]));
                filteredTransaction.add(transactionDTO);
            }
        }
        return filteredTransaction;

    }

    @Override
    public AccountDetailDTO findDetailsByClientCompAndAcctNo(Map<String, Object> accountInfo) {
        logger.info("Finding Details for {}", accountInfo);
        AccountDetailDTO accountDetailDTO = null;
        ShareHolder detailsByClientCompAndAcctNo = shareHolderService.findDetailsByClientCompAndAcctNo(accountInfo);
        if (detailsByClientCompAndAcctNo != null) {
            accountDetailDTO = new AccountDetailDTO();
            accountDetailDTO.setClientCompany(detailsByClientCompAndAcctNo.getClientCompany());
            accountDetailDTO.setAccountNumber(String.valueOf(detailsByClientCompAndAcctNo.getShareHolderId()));
            accountDetailDTO.setAccountBalance(detailsByClientCompAndAcctNo.getShareUnit());
            accountDetailDTO.setCHN(detailsByClientCompAndAcctNo.getClearingHousingNumber());
            logger.info("findDetailsByClientCompAndAcctNo returns {} ", accountDetailDTO.toString());
            return accountDetailDTO;
        }
        return accountDetailDTO;
    }

    @Override
    protected String entryToList(TransactionRequest transactionRequest, ModuleRequest moduleRequest, boolean doValidate, boolean saveData) throws Exception {
        logger.info("Inside TransactionServiceImpl.entryToList() with transactionRequest: {}, moduleRequest: {}, doValidate: {}, saveData: {}",
                transactionRequest.toString(), moduleRequest.toString(), doValidate, saveData);
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> transactionData = new ArrayList<>();
        List<ShareHolderTemp> shareHolderTempList = new ArrayList<>();

        Path transactionFilePath = Paths.get(fileStorageService.downloadFile(transactionRequest.getTransactionFileAddress()));
        Resource transactionFile = new FileSystemResource(FilenameUtils.normalize(transactionFilePath.toString()));
        List<TransactionMaster> transactionMasterList = new ArrayList<>();
        if (saveData) {
            transactionMasterList = transactionMasterService.getMasterFileDataList(transactionRequest, moduleRequest);
        }
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(transactionFile.getFile()))) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            int totalBuy = 0;
            int totalSell = 0;
            boolean rowHasError = false;
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Transaction transaction = new Transaction();
                ProcessedTransaction processedTransaction = new ProcessedTransaction();
                Optional<ShareHolder> byClearingHousingNumber = Optional.empty();
                Long shareUnit = null;
                row = sheet.getRow(i);
                boolean isDuplicateTransaction = false;
                if (row != null) {
                    try {
                        for (int j = 0; j < 15; j++) {
                            String strCurrentCell = null;
                            Cell cellValue = row.getCell(j);
                            if (cellValue != null) {
                                strCurrentCell = formatter.formatCellValue(cellValue).trim();
                            }
                            String transactionId = String.valueOf(strCurrentCell);
                            if (j == 0) {
                                isDuplicateTransaction = isDuplicateTransaction(transactionId);
                            }
                            if (!isDuplicateTransaction) {
                                switch (j) {
                                    case 0:
                                        transaction.setTransactionId(transactionId);
                                        processedTransaction.setTransactionId(transactionId);
                                        break;
                                    case 1:
                                        transaction.setCntrlId(String.valueOf(strCurrentCell));
                                        break;
                                    case 2:
                                        transaction.setTransactionDate(Utils.getDate(ApplicationConstant.TRANSACTION_FILE_FORMATE, String.valueOf(strCurrentCell)));
                                        break;
                                    case 3:
                                        transaction.setCompany(String.valueOf(strCurrentCell));
                                        break;
                                    case 4:
                                        shareUnit = Long.valueOf(String.valueOf(strCurrentCell));
                                        transaction.setUnit(shareUnit);
                                        break;
                                    case 5:
                                        transaction.setSell(String.valueOf(strCurrentCell));
                                        break;
                                    case 6:
                                        transaction.setSellOrBuy(String.valueOf(strCurrentCell));
                                        if (doValidate) {
                                            if (TransactionUtil.isBuyerTransaction(transaction)) {
                                                totalBuy++;
                                            }
                                            if (TransactionUtil.isSellerTransaction(transaction)) {
                                                totalSell++;
                                            }
                                        }
                                        break;
                                    case 7:
                                        String chn = String.valueOf(strCurrentCell);
                                        Long shareUnitt = shareUnit;
                                        if (saveData) {
                                            byClearingHousingNumber = shareHolderService.findByClearingHousingNumber(chn);
                                            if (byClearingHousingNumber.isPresent()) {
                                                ShareHolder shareHolder = byClearingHousingNumber.get();
                                                processedTransaction.setHolderOnSystem(true);
                                                Long newShareUnit = (shareUnitt == null ? 0 : Long.valueOf(shareUnit));
                                                if (TransactionUtil.isSellerTransaction(transaction)) {
                                                    if ((shareHolder.getShareUnit() >= newShareUnit)) {
                                                        processedTransaction.setReadyForTransfer(true);
                                                    }
                                                    if (shareHolder.getShareUnit() == 0) {
                                                        processedTransaction.setSuspended(true);
                                                        processedTransaction.setSuspendedOn(new Date());
                                                        processedTransaction.setSuspensionReason(ApplicationConstant.SELLER_HAS_ZERO_UNIT_IN_SYSTEM);
                                                    }
                                                }
                                                if (shareHolder.getEsopStatus() != null) {
                                                    processedTransaction.setSuspended(true);
                                                    processedTransaction.setSuspendedOn(new Date());
                                                    processedTransaction.setSuspensionReason(ApplicationConstant.SHAREHOLDER_HAS_ESOP_STATUS);
                                                }
                                            } else {
//                                        if(TransactionUtil.isSellerTransaction(transaction)){
//                                            processedTransaction.setSuspended(true);
//                                            processedTransaction.setSuspensionReason(ApplicationConstant.SELLER_NOT_IN_SYSTEM);
//                                        }else{
                                                Optional<TransactionMaster> anyMaster = transactionMasterList.stream().filter(transactionMaster -> chn.equalsIgnoreCase(transactionMaster.getCHN())).findAny();
                                                if (anyMaster.isPresent()) {
                                                    TransactionMaster transactionMaster = anyMaster.get();
                                                    ShareHolderTemp tempShareHolder = shareHolderTempService.createTempShareHolder(transactionMaster, transactionRequest, shareUnitt);
                                                    shareHolderTempList.add(tempShareHolder);
                                                    transactionRequest.addShareHolderTempData(tempShareHolder);
                                                }
//                                        }
                                            }
                                        }
                                        transaction.setCHN(chn);
                                        break;
                                    default:
                                        break;
                                }
                                rowHasError = false;
                            }

                        }
                    } catch (Exception e) {
                        rowHasError = true;
                        logger.info("Exception in Row {}", row);
                        logger.info("Exception Happened while writing transaction master file row {}", e);
                    }
                    if (saveData && !isDuplicateTransaction && !rowHasError) {
                        if (processedTransaction.isHolderOnSystem() && TransactionUtil.isSellerTransaction(transaction)) {
//                        if (TransactionUtil.isSellerTransaction(transaction)) {
                            processedTransaction.setTransaction(transaction);
                            transaction.setProcessedTransaction(processedTransaction);
                        }
                        transactionList.add(transaction);
                        transactionRequest.addTransactionData(transaction);
                        transactionData.add(transaction);

                    }
                }
            }
            if (doValidate) {
                ModuleRequest approvalRequestById = moduleRequestService.getApprovalRequestById(moduleRequest.getRequestId());
                ModuleRequestDTO moduleRequestDTO = new Gson().fromJson(approvalRequestById.getNewRecord(), ModuleRequestDTO.class);
                if (totalBuy == totalSell) {
                    moduleRequestDTO.setTransactionBalance(true);
                    moduleRequestDTO.setBalanceTransactionMessage(ApiResponseCode.TRANSACTION_BALANCE.getDescription());
                } else {
                    moduleRequestDTO.setTransactionBalance(false);
                    moduleRequestDTO.setBalanceTransactionMessage(ApiResponseCode.TRANSACTION_NOT_BALANCE.getDescription());
                }
                approvalRequestById.setNewRecord(new Gson().toJson(moduleRequestDTO));
                moduleRequestService.save(approvalRequestById);
            }
            if (saveData && transactionList.size() > 0) {
                transactionMasterList.forEach(tml -> transactionRequest.addTransactionMasterData(tml));
                transactionRequest.setTransactionData(transactionData);
                transactionRequest.setTransactionMasterData(transactionMasterList);
                transactionRequest.setShareHolderTempData(shareHolderTempList);
                logger.info("========BEFORE Saving transactionRequest=============");
                TransactionRequest save = transactionRequestRepository.save(transactionRequest);
                logger.info("========== transactionRequest saved Successfully", save.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception Happened while converting transaction file", e);
        }
        return transactionList.toString();
    }

    private boolean isDuplicateTransaction(String transactionId) {
        List<Transaction> byTransactionId = transactionRepository.findByTransactionId(transactionId);
        return byTransactionId.size() > 0;
    }

    @Override
    public List<Transaction> findTransactionByCHN(String chn) {
        return transactionRepository.findAllTransactionByCHN(chn);
    }

    @Override
    public List<Transaction> findByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    @Override
    public List<Transaction> findByCHNAndTransactionRequestAndSellOrBuy(String chn, Long transactionRequestId, String sellOrBuy) {
        return transactionRepository.findByCHNAndTransactionRequestAndSellOrBuy(chn, transactionRequestId, sellOrBuy);
    }

    @Override
    public Transaction findByTransactionIdAndSellOrBuy(String transactionId, String sellOrBuy) {
        return transactionRepository.findByTransactionIdAndSellOrBuy(transactionId, sellOrBuy);
    }
}
