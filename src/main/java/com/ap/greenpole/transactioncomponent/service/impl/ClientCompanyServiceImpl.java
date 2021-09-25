package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import com.ap.greenpole.transactioncomponent.enums.GenericStatusEnum;
import com.ap.greenpole.transactioncomponent.repository.ClientCompanyRepository;
import com.ap.greenpole.transactioncomponent.service.ClientCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:44 PM
 */
@Service
public class ClientCompanyServiceImpl implements ClientCompanyService{

    @Autowired
    private ClientCompanyRepository clientCompanyRepository;


    @Override
    public Page<ClientCompany> getAllClientCompanies(Pageable page) {
        return clientCompanyRepository.findByStatusOrderByApprovedAtDesc(GenericStatusEnum.ACTIVE.name(), page);
    }

    @Override
    public ClientCompany getClientCompaniesById(Long id) {
        return clientCompanyRepository.findByIdAndStatus(id, GenericStatusEnum.ACTIVE.name());
    }
}
