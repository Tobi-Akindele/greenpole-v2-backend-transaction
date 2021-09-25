package com.ap.greenpole.transactioncomponent.service;

import com.ap.greenpole.transactioncomponent.dto.AddSupportAccountDTO;
import com.ap.greenpole.transactioncomponent.entity.SupportAccount;

import java.util.Map;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:44 PM
 */
public interface SupportAccountService {

    SupportAccount save(SupportAccount supportAccount);

    Map<String, String> addSupportAccount(AddSupportAccountDTO request);
}
