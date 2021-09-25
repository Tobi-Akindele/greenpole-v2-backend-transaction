package com.ap.greenpole.transactioncomponent.service.impl;

import com.ap.greenpole.transactioncomponent.dto.AddSupportAccountDTO;
import com.ap.greenpole.transactioncomponent.entity.ProcessedTransaction;
import com.ap.greenpole.transactioncomponent.entity.ShareHolder;
import com.ap.greenpole.transactioncomponent.entity.SupportAccount;
import com.ap.greenpole.transactioncomponent.repository.ClientCompanyRepository;
import com.ap.greenpole.transactioncomponent.repository.SupportAccountRepository;
import com.ap.greenpole.transactioncomponent.service.ProcessTransactionService;
import com.ap.greenpole.transactioncomponent.service.ShareHolderService;
import com.ap.greenpole.transactioncomponent.service.SupportAccountService;
import com.ap.greenpole.transactioncomponent.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/9/2020 1:44 PM
 */
@Service
public class AccountSupportServiceImpl implements SupportAccountService {

    @Autowired
    private SupportAccountRepository supportAccountRepository;

    @Autowired
    private ProcessTransactionService processTransactionService;

    @Autowired
    private ShareHolderService shareHolderService;

    @Override
    public SupportAccount save(SupportAccount supportAccount) {
        return supportAccountRepository.save(supportAccount);
    }

    @Override
    public Map<String, String> addSupportAccount(AddSupportAccountDTO request) {
        Map<String, String> responseMap = new HashMap<>();
        Map<String, Object> acctAndClientCompanyInfo = new HashMap<>();
        BiPredicate<Long, Long> testShareUnit = (t, u) -> t >= u;
        long totalUnitForSupport = request.getAccountNumbers().stream().mapToLong(s -> s.getSupportUnit()).sum();
        Optional<ProcessedTransaction> geyTransByCSCSTransactionId = Optional.ofNullable(processTransactionService.findByCSCSTransactionId(request.getTransactionRecordId()));
        if (geyTransByCSCSTransactionId.isPresent()){
            ProcessedTransaction byCSCSTransactionId = geyTransByCSCSTransactionId.get();
            final long[] unitToBeRecievedByBuyer = {byCSCSTransactionId.getUnitToReceive()};
            if(byCSCSTransactionId.isProcessed() && byCSCSTransactionId.isAddAccountRequired()){
                request.getAccountNumbers().stream().forEach(supportAccount -> {
                    if (unitToBeRecievedByBuyer[0] > 0){
                        acctAndClientCompanyInfo.put("client_company", supportAccount.getClientCompany());
                        acctAndClientCompanyInfo.put("account_number", supportAccount.getAccountNumber());
                        Optional<ShareHolder> detailsByClientCompAndAcctNo = Optional.ofNullable(shareHolderService.findDetailsByClientCompAndAcctNo(acctAndClientCompanyInfo));
                        if(detailsByClientCompAndAcctNo.isPresent()){
                            if(testShareUnit.test(totalUnitForSupport, unitToBeRecievedByBuyer[0])){
//                            if(testShareUnit.test( supportAccount.getSupportUnit(), unitToBeRecievedByBuyer)){
                                long supportUnit = supportAccount.getSupportUnit();
                                unitToBeRecievedByBuyer[0] -= supportUnit;
                                byCSCSTransactionId.addSupportAccountData(supportAccount);
                                processTransactionService.save(byCSCSTransactionId);
                                responseMap.put(supportAccount.getAccountNumber(), "Added Successfully");
//                            }
                            }else {
                                responseMap.put(supportAccount.getAccountNumber(), "Sum of Supported Unit is Less Than Buyer's Unit To Receive" );
                            }

                        }else {
                            responseMap.put(supportAccount.getAccountNumber(), "Does Not Exist" );
                        }
                    }
                });
            }else {
                responseMap.put("message", "Add Support Is Not Required For this Transaction or Transaction Is Not Processed" );
            }
        }else {
            responseMap.put("message", "TransactionRecordId Not Found");
        }

        return responseMap;
    }
}
