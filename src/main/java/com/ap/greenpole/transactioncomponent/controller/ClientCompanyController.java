package com.ap.greenpole.transactioncomponent.controller;

import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.ap.greenpole.transactioncomponent.entity.TransactionRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ClientCompanyService;
import com.ap.greenpole.transactioncomponent.service.FileStorageService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 11:03 PM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class ClientCompanyController {

    final static Logger logger = LoggerFactory.getLogger(ClientCompanyController.class);

    @Autowired
    private ClientCompanyService clientCompanyService;

    @GetMapping(value = "/clientcompany/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse<?> getAllClientCompanies(@RequestParam(defaultValue = "0") Integer page,
                                                                           @RequestParam(defaultValue = "10") Integer size,
                                                                           HttpServletRequest request){
        DefaultResponse<List<ClientCompany>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus(ApiResponseCode.FAILED.getCode());
        defaultResponse.setStatusMessage(ApiResponseCode.FAILED.getDescription());

        Pageable pageable = PageRequest.of(page, size);
        List<ClientCompany> allClientCompany = new ArrayList<>();
        try {
            Page<ClientCompany> clientCompanies = clientCompanyService.getAllClientCompanies(pageable); //active client companies
            if (clientCompanies != null && !clientCompanies.isEmpty()) {
                allClientCompany = clientCompanies.getContent();
                defaultResponse.setStatus(ApiResponseCode.SUCCESS.getCode());
                defaultResponse.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
                defaultResponse.setCount(clientCompanies.getTotalElements());
                defaultResponse.setData(allClientCompany);
                return defaultResponse;
            } else {
                defaultResponse.setStatusMessage(ApiResponseCode.RECORD_NOT_FOUND.getDescription());
                return defaultResponse;
            }
        } catch (Exception e) {
            logger.info("[-] Exception happened while getting AllClientCompanies {}", e);
            defaultResponse.setStatusMessage("Error Processing Request");
            return defaultResponse;
        }
    }


}
