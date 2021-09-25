package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.dto.ProcessMasterTransactionResponseDTO;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.TransactionMaster;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.repository.TransactionMasterRepository;
import com.ap.greenpole.transactioncomponent.service.FileStorageService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderService;
import com.ap.greenpole.transactioncomponent.service.TransactionMasterService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
public class TransactionMasterServiceImpl extends AbstractTransactionService implements TransactionMasterService {

    @Autowired
    private TransactionMasterRepository masterRepository;

    @Autowired
    private ShareHolderService shareHolderService;

    @Autowired
    private FileStorageService fileStorageService;

    private Gson gson = new Gson();

    @Override
    public TransactionMaster save(TransactionMaster transactionMaster) {
        return masterRepository.save(transactionMaster);
    }

    @Override
    public List<TransactionMaster> saveAll(List<TransactionMaster> transactionMasters) {
        return masterRepository.saveAll(transactionMasters);
    }

    @Override
    public TransactionMaster getTransactionMasterById(Long id) {
        return masterRepository.findById(id).get();
    }

    @Override
    public List<TransactionMaster> getAllTransactionMaster() {
        return masterRepository.findAll();
    }


    @Override
    public List<TransactionMaster> getMasterFileDataList(TransactionRequest transactionRequest, ModuleRequest moduleRequest) throws Exception {
        String masterFileString = entryToList(transactionRequest, moduleRequest, false, false);
        List<TransactionMaster> transactionMasterList = gson.fromJson(masterFileString, new TypeToken<List<TransactionMaster>>() {
        }.getType());
        return transactionMasterList;

    }

    @Override
    public List<TransactionMaster> findByTransactionRequestId(Long id) {
        return masterRepository.findAllByTransactionRequestId(id);
    }

    @Override
    public TransactionMaster findByChn(String Chn) {
        return masterRepository.findByCHN(Chn);
    }

    @Override
    public TransactionMaster findByChnAndTransactionRequest(String Chn, Long transactionRequest) {
        return masterRepository.findByCHNAndTransactionRequest(Chn, transactionRequest);
    }

    @Override
    public ProcessMasterTransactionResponseDTO fixMasterRecord(ProcessMasterTransactionResponseDTO request) {
        logger.info("Fixing Master Record with {}", request.toString());
        ShareHolder shareHolder = new ShareHolder();
        Optional<TransactionMaster> byId = masterRepository.findById(request.getMasterRecordId());
        if (byId.isPresent()) {
            TransactionMaster transactionMaster = byId.get();
            Optional<ShareHolder> byClearingHousingNumberAndClientComp = shareHolderService.findByClearingHousingNumberAndClientComp(transactionMaster.getCHN(), Long.parseLong(transactionMaster.getTransactionRequest().getClientCompany()));
            if (byClearingHousingNumberAndClientComp.isPresent()) {
                shareHolder = byClearingHousingNumberAndClientComp.get();
                shareHolder.setFirstName(request.getSystemFirstName());
                shareHolder.setLastName(request.getSystemLastName());
                shareHolder.setMiddleName(request.getSystemMiddleName());
                shareHolder.setShareholderType(request.getType());
                shareHolder.setAddress(request.getSystemAddress1());
                shareHolder.setStateOfOrigin(request.getSystemState());
                ShareHolder save = shareHolderService.save(shareHolder);
                if (save != null) {
                    return request;
                }
            }

        }
        logger.info("Fixed Master Record is {}", shareHolder.toString());
        return null ;
    }

    @Override
    protected String entryToList(TransactionRequest transactionRequest, ModuleRequest moduleRequest, boolean doValidate, boolean saveData) throws Exception {
        logger.info("Inside TransactionServiceImpl.entryToList() with transactionRequest: {}, moduleRequest: {}, doValidate: {}, saveData: {}",
                transactionRequest.toString(), moduleRequest.toString(), doValidate, saveData);
        List<TransactionMaster> transactionMasterList = new ArrayList<>();
        Path masterFileePath = Paths.get(fileStorageService.downloadFile(transactionRequest.getTransactionMasterFileAddress()));
        Resource masterFile = new FileSystemResource(FilenameUtils.normalize(masterFileePath.toString()));
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(masterFile.getFile()))) {
            DataFormatter formatter = new DataFormatter();
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                TransactionMaster transactionMaster = new TransactionMaster();
                row = sheet.getRow(i);
                if (row != null) {
                    try {
                        for (int j = 0; j < 15; j++) {
                            String strCurrentCell = null;
                            Cell cellValue = row.getCell(j);
                            if (cellValue != null) {
                                strCurrentCell = formatter.formatCellValue(cellValue).trim();
                            }
                            switch (j) {
                                case 0:
                                    transactionMaster.setCHN(String.valueOf(strCurrentCell));
                                    break;
                                case 1:
                                    transactionMaster.setNames(String.valueOf(strCurrentCell));
                                    break;
                                case 2:
                                    transactionMaster.setAddress1(String.valueOf(strCurrentCell));
                                    break;
                                case 3:
                                    transactionMaster.setAddress2(String.valueOf(strCurrentCell));
                                    break;
                                case 4:
                                    transactionMaster.setAddress3(String.valueOf(strCurrentCell));
                                    break;
                                case 5:
                                    transactionMaster.setCountry(String.valueOf(strCurrentCell));
                                    break;
                                case 6:
                                    transactionMaster.setStructure(String.valueOf(strCurrentCell));
                                    break;
                                case 7:
                                    transactionMaster.setBankName(String.valueOf(strCurrentCell));
                                    break;
                                case 8:
                                    transactionMaster.setBankAccountNumber(String.valueOf(strCurrentCell));
                                    break;
                                case 9:
                                    transactionMaster.setBvn(String.valueOf(strCurrentCell));
                                    break;
                                case 10:
                                    transactionMaster.setEmailAddress(String.valueOf(strCurrentCell));
                                    break;
                                case 11:
                                    transactionMaster.setPhoneNumber(String.valueOf(strCurrentCell));
                                    break;
                                case 12:
                                    transactionMaster.setNextOfKinOrMaiden(String.valueOf(strCurrentCell));
                                    break;
                                default:
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        logger.info("Exception in Row {}", row);
                        logger.info("Exception Happened while writing transaction master file row {}", e);
                    }
                    transactionMasterList.add(transactionMaster);
                } else {
                    break;
                }
            }
            System.out.println(">>>>transactionMasterList Size<<<< " + transactionMasterList.size());
        }
        return new Gson().toJson(transactionMasterList);
    }

//    @Override
//    protected String entryToList(TransactionRequest transactionRequest, ModuleRequest moduleRequest, boolean doValidate, boolean saveData) throws IOException {
//        logger.info("Inside TransactionServiceImpl.entryToList() with transactionRequest: {}, moduleRequest: {}, doValidate: {}, saveData: {}",
//                transactionRequest.toString(), moduleRequest.toString(), doValidate, saveData);
//        List<TransactionMaster> transactionMasterList = new ArrayList<>();
//        Path masterFileePath = Paths.get(transactionRequest.getTransactionMasterFileAddress());
//        Resource masterFile = new FileSystemResource(FilenameUtils.normalize(masterFileePath.toString()));
//        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(masterFile.getFile()))) {
//            DataFormatter formatter = new DataFormatter();
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rows = sheet.iterator();
//            rows.next();
//            boolean rowHasError = false;
//            while (rows.hasNext()) {
//                TransactionMaster transactionMaster = new TransactionMaster();
//                boolean endOfRow = false;
//                Row currentRow = rows.next();
//                int cellIndex = 0;
//                Iterator<Cell> cellInRow = currentRow.iterator();
//                try {
//                    while (cellInRow.hasNext()) {
//                        Cell currentCell = cellInRow.next();
//                        endOfRow = true;
//                        Object strCurrentCell = formatter.formatCellValue(currentCell);
////                        if()
////                        Object strCurrentCell = formatter.formatCellValue(StringUtils.isEmpty(String.valueOf(currentCell)) ? currentCell : null);
//                        switch (cellIndex) {
//                            case 0:
//                                transactionMaster.setCHN(String.valueOf(strCurrentCell));
//                                break;
//                            case 1:
//                                transactionMaster.setNames(String.valueOf(strCurrentCell));
//                                break;
//                            case 2:
//                                transactionMaster.setAddress1(String.valueOf(strCurrentCell));
//                                break;
//                            case 3:
//                                transactionMaster.setAddress2(String.valueOf(strCurrentCell));
//                                break;
//                            case 4:
//                                transactionMaster.setAddress3(String.valueOf(strCurrentCell));
//                                break;
//                            case 5:
//                                transactionMaster.setCountry(String.valueOf(strCurrentCell));
//                                break;
//                            case 6:
//                                transactionMaster.setStructure(String.valueOf(strCurrentCell));
//                                break;
//                            case 7:
//                                transactionMaster.setBankName(String.valueOf(strCurrentCell));
//                                break;
//                            case 8:
//                                transactionMaster.setBankAccountNumber(String.valueOf(strCurrentCell));
//                                break;
//                            case 9:
//                                transactionMaster.setBvn(String.valueOf(strCurrentCell));
//                                break;
//                            case 10:
//                                transactionMaster.setEmailAddress(String.valueOf(strCurrentCell));
//                                break;
//                            case 11:
//                                transactionMaster.setPhoneNumber(String.valueOf(strCurrentCell));
//                                break;
//                            case 12:
//                                transactionMaster.setNextOfKinOrMaiden(String.valueOf(strCurrentCell));
//                                break;
//                            default:
//                                break;
//                        }
//                        rowHasError = false;
//                        cellIndex++;
//                    }
//                } catch (Exception e) {
//                    rowHasError = true;
//                    logger.info("Exception Happened while writing transaction master file row", e);
//                }
//                if (endOfRow && !rowHasError) {
//                    transactionMasterList.add(transactionMaster);
//                }
//            }
//        }
//        return new Gson().toJson(transactionMasterList);
//    }
}
