package com.ap.greenpole.transactioncomponent.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ap.greenpole.transactioncomponent.dto.AccountDetailDTO;
import com.ap.greenpole.transactioncomponent.dto.AnalyseTransactionDTO;
import com.ap.greenpole.transactioncomponent.dto.FilterTransactionDTO;
import com.ap.greenpole.transactioncomponent.dto.ModuleRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.ProcessMasterTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.FileStorageService;
import com.ap.greenpole.transactioncomponent.service.TransactionMasterService;
import com.ap.greenpole.transactioncomponent.service.TransactionRequestService;
import com.ap.greenpole.transactioncomponent.service.TransactionService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class TransactionController {

    final static Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRequestService transactionRequestService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionMasterService transactionMasterService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_UPLOAD_CSCS_TRANSACTIONS"})
    public DefaultResponse<?> uploadTransaction(@RequestParam("masterFile") MultipartFile masterFile,
                                                @RequestParam("transactionFile") MultipartFile transactionFile,
                                                @RequestParam("transactionType") String transactionType,
                                                @RequestParam("depository") String depository,
                                                @RequestParam("transactionDate") String transactionDate,
                                                @RequestParam("clientCompany") String clientCompany,
                                                HttpServletRequest request) {
        logger.info("[+] Inside TransactionController.uploadTransaction() with transactionType: {}, depository: {}, " +
                "transactionDate: {}, clientCompany: {}", transactionType, depository, transactionDate, clientCompany);
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
        Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
            if (!user.isPresent()) {
                defaultResponse.setStatusMessage("User details not found");
                return defaultResponse;
            }
            Map<String, Object> validationResponse = TransactionUtil.validateUploadRequest(masterFile, transactionFile, transactionType, depository, transactionDate, clientCompany);
            Boolean doProceed = (Boolean) validationResponse.get("doProceed");
            String validationMessage = (String) validationResponse.get("validationMessage");

            if (!doProceed) {
                defaultResponse.setStatusMessage(validationMessage);
                return defaultResponse;
            }

            ModuleRequestDTO moduleRequestDTO = new ModuleRequestDTO();
            moduleRequestDTO.setClientCompany(clientCompany);
            moduleRequestDTO.setDepository(depository);
            moduleRequestDTO.setTransactionDate(Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, transactionDate));
            moduleRequestDTO.setTransactionType(transactionType);
            String masterFileAddress = fileStorageService.storeFile(masterFile);
            String transactionFileAddress = fileStorageService.storeFile(transactionFile);
            moduleRequestDTO.setTransactionMasterFileAddress(masterFileAddress);
            moduleRequestDTO.setTransactionFileAddress(transactionFileAddress);
            moduleRequestDTO.setTransactionBalance(false);
            if (masterFileAddress == null || transactionFileAddress == null) {
                defaultResponse.setStatusMessage(ApiResponseCode.ERROR_WRITING_FILE_TO_SYSTEM.getDescription());
                return defaultResponse;
            }
            moduleRequestDTO.setBalanceTransactionMessage(ApiResponseCode.FILE_VALIDATION.getDescription());
            ModuleRequest moduleRequest = transactionRequestService.uploadTransactionRequest(moduleRequestDTO, user.get());
            if (moduleRequest != null) {
                transactionService.balanceFile(moduleRequestDTO, moduleRequest);
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(moduleRequest);
            }
            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while processing File Upload {}", e);
            return defaultResponse;
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<List<TransactionRequest>> getAllTransactionPaginated(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        logger.info("[+] Inside TransactionController.getAllTransactionPaginated()");
        DefaultResponse<List<TransactionRequest>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Page<Transaction> allTransactionPaginated = transactionService.getAllTransactionPaginated(page, size);
            if (allTransactionPaginated != null && !allTransactionPaginated.isEmpty()) {
                List<Transaction> allTransaction = allTransactionPaginated.getContent();
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setCount(allTransactionPaginated.getTotalElements());
                defaultResponse.setData(allTransaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }

        } catch (Exception e) {
            logger.info("[-] Exception happened while getting AllTransactionPaginated {}", e);
            defaultResponse.setStatusMessage("Error Processing Request: " + e.getMessage());
            return defaultResponse;
        }
    }

    @GetMapping(value = "/details/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<Transaction> getTransactionById(@PathVariable long transactionId) {
        logger.info("[+] Inside TransactionController.getTransactionById() with id: {}", transactionId);
        DefaultResponse<Transaction> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            if (transactionId <= 0) {
                defaultResponse.setStatusMessage("Request ID is required");
                return defaultResponse;
            }
            Transaction transaction = transactionService.getTransactionById(transactionId);
            if (transaction != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(transaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
            }
        } catch (Exception e) {
            logger.info("[-] Exception happened while getting TransactionById {}", e);
            defaultResponse.setStatusMessage("Error Processing Request: " + e.getMessage());
        }
        return defaultResponse;
    }

    @GetMapping(value = "/analyse/{transactionRequestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<AnalyseTransactionDTO> analyseTransactionById(@PathVariable long transactionRequestId) {
        logger.info("[+] Inside TransactionController.analyseTransactionById() with id: {}", transactionRequestId);
        DefaultResponse<AnalyseTransactionDTO> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            if (transactionRequestId <= 0) {
                defaultResponse.setStatusMessage("TransactionRequestId is required");
                return defaultResponse;
            }
            AnalyseTransactionDTO transaction = transactionRequestService.analyseTransaction(transactionRequestId);
            if (transaction != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(transaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.ANALYSIS_NOT_OK.getDescription());
            }
        } catch (Exception e) {
            logger.info("[-] Exception happened while getting analyzing Transaction {}", e);
            defaultResponse.setStatusMessage("Error Processing Request: " + e.getMessage());
        }
        return defaultResponse;
    }

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorizePermission({"PERMISSION_QUERY_TRANSACTION"})
    public DefaultResponse<List<FilterTransactionDTO>> getAllTransactionForProcessing(
            @RequestParam(value = "client_company_name", required = false, defaultValue = "-0") String clientCompanyName,
            @RequestParam(value = "client_company_code", required = false, defaultValue = "-0") String clientCompanyCode,
            @RequestParam(value = "from_shareholder_name", required = false, defaultValue = "-0") String fromShareHolderName,
            @RequestParam(value = "to_shareholder_name", required = false, defaultValue = "-0") String toShareHolderName,
            @RequestParam(value = "from_shareholder_chn", required = false, defaultValue = "-0") String fromShareHolderChn,
            @RequestParam(value = "to_shareholder_chn", required = false, defaultValue = "-0") String toShareHolderChn,
            @RequestParam(value = "buyer_cscs_transactionid", required = false, defaultValue = "-0") String buyerCSCSTransactionId,
            @RequestParam(value = "seller_cscs_transactionid", required = false, defaultValue = "-0") String sellerCSCSTransactionId,
            @RequestParam(value = "from_shareholder_company_account", required = false, defaultValue = "-0") String fromShareHolderCompanyAccount,
            @RequestParam(value = "to_shareholder_company_account", required = false, defaultValue = "-0") String toShareHolderCompanyAccount,
            @RequestParam(value = "date_on", required = false, defaultValue = "2000-10-14 00:00:00.000000") String dateOn,
            @RequestParam(value = "date_before", required = false, defaultValue = "1990-10-14 00:00:00.000000") String dateBefore,
            @RequestParam(value = "uploaded_date", required = false, defaultValue = "2021-10-14 00:00:00.000000") String uploadDate,
            @RequestParam(value = "uploaded_after", required = false, defaultValue = "5050-10-14 00:00:00.000000") String uploadAfter,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        logger.info("[+] Inside TransactionController.getAllTransactionForProcessing()");
        DefaultResponse<List<FilterTransactionDTO>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());
        try {
            Map<String, Object> queryData = new HashMap<>();
            queryData.put("client_company_name", clientCompanyName);
            queryData.put("client_company_code", clientCompanyCode);
            queryData.put("from_shareholder_name", fromShareHolderName);
            queryData.put("to_shareholder_name", toShareHolderName);
            queryData.put("from_shareholder_chn", fromShareHolderChn);
            queryData.put("to_shareholder_chn", toShareHolderChn);
            queryData.put("buyer_cscs_transactionid", buyerCSCSTransactionId);
            queryData.put("seller_cscs_transactionid", sellerCSCSTransactionId);
            queryData.put("from_shareholder_company_account", fromShareHolderCompanyAccount);
            queryData.put("to_shareholder_company_account", toShareHolderCompanyAccount);
            queryData.put("date_on", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, dateOn));
            queryData.put("date_before", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, dateBefore));
            queryData.put("uploaded_date", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, uploadDate));
            queryData.put("uploaded_after", Utils.getDate(ApplicationConstant.REQUEST_DATE_FORMATE, uploadAfter));
            List<FilterTransactionDTO> allTransaction = transactionService.getFilteredTransactions(queryData, page, size);
            if (!allTransaction.isEmpty()) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(allTransaction);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while getAllTransactionForProcessing {}", e);
            defaultResponse.setStatusMessage("Error Processing Request: " + e.getMessage());
            return defaultResponse;
        }
    }

    @GetMapping(value = "/sourceaccount/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<AccountDetailDTO> getAccountDetail(
            @RequestParam(value = "client_company", required = false, defaultValue = "-0") String clientCompany,
            @RequestParam(value = "account_number", required = false, defaultValue = "-0") String accountNumber) {
        logger.info("[+] Inside TransactionController.getAccountDetail() with client_company; {} and account_number: {}", clientCompany, accountNumber);
        DefaultResponse<AccountDetailDTO> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Map<String, Object> acctInfo = new HashMap<>();
            acctInfo.put("client_company", clientCompany);
            acctInfo.put("account_number", accountNumber);
            AccountDetailDTO detail = transactionService.findDetailsByClientCompAndAcctNo(acctInfo);
            if (detail == null) {
                defaultResponse.setStatusMessage("Source Account not found");
                return defaultResponse;
            }
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            defaultResponse.setData(detail);
            return defaultResponse;
        } catch (Exception e) {
            logger.info("[-] Exception happened while getAccountDetail {}", e);
            defaultResponse.setStatusMessage("Error Processing Request: " + e.getMessage());
            return defaultResponse;
        }
    }

    @GetMapping(value = "/shareholder/account/transaction/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<AccountDetailDTO> queryShareholderAccountTransactions(@RequestParam(value = "client_company", required = false, defaultValue = "-0") String clientCompany,
                                                                                 @RequestParam(value = "account_number", required = false, defaultValue = "-0") String accountNumber) {

        logger.info("[+] Inside TransactionController.queryShareholderAccountTransactions() with clientCompany: {}, accountNumber: {}", clientCompany, accountNumber);
        DefaultResponse<AccountDetailDTO> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            Map<String, Object> acctInfo = new HashMap<>();
            acctInfo.put("client_company", clientCompany);
            acctInfo.put("account_number", accountNumber);
            AccountDetailDTO detail = transactionService.findDetailsByClientCompAndAcctNo(acctInfo);

            if (detail == null) {
                defaultResponse.setStatusMessage("Shareholder details not found");
                return defaultResponse;
            }

            List<Transaction> findTransactionByChn = transactionService.findTransactionByCHN(detail.getCHN());
            detail.setShareHolderTransanctions(findTransactionByChn);
            defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
            defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
            defaultResponse.setData(detail);

            return defaultResponse;
        } catch (Exception ex) {
            logger.info("[-] Exception occurred when queryShareholderAccountTransactions: {}", ex);
            defaultResponse.setStatusMessage("Error Processing Request: " + ex.getMessage());
            return defaultResponse;
        }
    }


    @PostMapping(value = "/masterrecord/fix", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> fixMasterRecord(HttpServletRequest servletRequest,
                                              @RequestBody ProcessMasterTransactionResponseDTO request) {
        logger.info("[+] Inside TransactionController.fixMasterRecord() with {}", request.toString());
        DefaultResponse<?> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        try {
            ProcessMasterTransactionResponseDTO processMasterTransactionResponseDTO = transactionMasterService.fixMasterRecord(request);
            if (processMasterTransactionResponseDTO != null) {
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setData(null);
            }
            return defaultResponse;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[-] Exception happened while Processing Transaction requests {}", e.getMessage());
            return defaultResponse;
        }
    }
}
