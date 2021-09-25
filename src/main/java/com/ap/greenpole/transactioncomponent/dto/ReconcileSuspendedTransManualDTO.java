package com.ap.greenpole.transactioncomponent.dto;

public class ReconcileSuspendedTransManualDTO {

	private String clientCompany;

	private String sourceAcctNumber;

	private String destinationAcctNumber;

	private String unitToTransfer;

	private String sellerCscsTransId;

	private String buyerCscsTransId;

	private String narration;

	private String transactionDate;

	public String getClientCompany() {
		return clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}

	public String getSourceAcctNumber() {
		return sourceAcctNumber;
	}

	public void setSourceAcctNumber(String sourceAcctNumber) {
		this.sourceAcctNumber = sourceAcctNumber;
	}

	public String getDestinationAcctNumber() {
		return destinationAcctNumber;
	}

	public void setDestinationAcctNumber(String destinationAcctNumber) {
		this.destinationAcctNumber = destinationAcctNumber;
	}

	public String getUnitToTransfer() {
		return unitToTransfer;
	}

	public void setUnitToTransfer(String unitToTransfer) {
		this.unitToTransfer = unitToTransfer;
	}

	public String getSellerCscsTransId() {
		return sellerCscsTransId;
	}

	public void setSellerCscsTransId(String sellerCscsTransId) {
		this.sellerCscsTransId = sellerCscsTransId;
	}

	public String getBuyerCscsTransId() {
		return buyerCscsTransId;
	}

	public void setBuyerCscsTransId(String buyerCscsTransId) {
		this.buyerCscsTransId = buyerCscsTransId;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "ReconcileSuspendedTransManuallyDTO{" +
				"clientCompany='" + clientCompany + '\'' +
				", sourceAcctNumber='" + sourceAcctNumber + '\'' +
				", destinationAcctNumber='" + destinationAcctNumber + '\'' +
				", unitToTransfer='" + unitToTransfer + '\'' +
				", sellerCscsTransId='" + sellerCscsTransId + '\'' +
				", buyerCscsTransId='" + buyerCscsTransId + '\'' +
				", narration='" + narration + '\'' +
				", transactionDate='" + transactionDate + '\'' +
				'}';
	}
}
