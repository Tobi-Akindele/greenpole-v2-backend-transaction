package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:44 PM
 */
public interface ClientCompanyService {

    Page<ClientCompany> getAllClientCompanies(Pageable page);

    ClientCompany getClientCompaniesById(Long id);
}
