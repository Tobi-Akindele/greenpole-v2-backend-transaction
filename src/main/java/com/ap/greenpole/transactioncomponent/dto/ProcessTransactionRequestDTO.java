package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

public class ProcessTransactionRequestDTO {

	private long requestId;

	private String clientCompany;

	private String transactionDate;

	private String uploadDate;

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getClientCompany() {
		return clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	@Override
	public String toString() {
		return "ProcessTransactionRequestDTO{" +
				"clientCompany='" + clientCompany + '\'' +
				", transactionDate='" + transactionDate + '\'' +
				", uploadDate='" + uploadDate + '\'' +
				'}';
	}
}
