package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.controller.TransactionController;
import com.ap.greenpole.transactioncomponent.dto.ModuleRequestDTO;
import com.ap.greenpole.transactioncomponent.dto.QueryTransactionRequestDTO;
import com.ap.greenpole.transactioncomponent.entity.*;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.enums.FileType;
import com.ap.greenpole.transactioncomponent.repository.ShareHolderRepository;
import com.ap.greenpole.transactioncomponent.repository.TransactionMasterRepository;
import com.ap.greenpole.transactioncomponent.repository.TransactionRepository;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:18 PM
 */
public abstract class AbstractTransactionService {

    final static Logger logger = LoggerFactory.getLogger(AbstractTransactionService.class);

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected TransactionMasterRepository masterRepository;

    protected abstract String entryToList(TransactionRequest transactionRequest, ModuleRequest moduleRequest, boolean doValidate, boolean saveData) throws Exception;


}
