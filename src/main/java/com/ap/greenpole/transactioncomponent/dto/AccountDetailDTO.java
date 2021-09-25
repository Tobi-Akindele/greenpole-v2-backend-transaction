package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

import com.ap.greenpole.transactioncomponent.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created By: Ilesanmi Omoniyi
 * Date: 10/24/2020 1:25 PM
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetailDTO {

    private long accountBalance;
    private long clientCompany;
    private String accountNumber;
    private String CHN;
    private List<Transaction> shareHolderTransanctions;

    public long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public long getClientCompany() {
        return clientCompany;
    }

    public void setClientCompany(long clientCompany) {
        this.clientCompany = clientCompany;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCHN() {
		return CHN;
	}

	public void setCHN(String cHN) {
		CHN = cHN;
	}

	public List<Transaction> getShareHolderTransanctions() {
		return shareHolderTransanctions;
	}

	public void setShareHolderTransanctions(List<Transaction> shareHolderTransanctions) {
		this.shareHolderTransanctions = shareHolderTransanctions;
	}

	@Override
    public String toString() {
        return "AccountDetailDTO{" +
                "accountBalance=" + accountBalance +
                ", clientCompany=" + clientCompany +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
