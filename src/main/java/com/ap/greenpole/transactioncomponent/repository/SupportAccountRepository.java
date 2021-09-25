package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.ClientCompany;
import com.ap.greenpole.transactioncomponent.entity.SupportAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 12:56 PM
 */
public interface SupportAccountRepository extends JpaRepository<SupportAccount, Long> {
}
