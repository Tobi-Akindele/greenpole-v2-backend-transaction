package com.ap.greenpole.transactioncomponent.dto;

import java.util.Date;

public class CancelledSuspendedTransactionsDto {

	private String cscsTransactionId;
	private String clientCompany;
	private String transactionType;
	private String seller;
	private String buyer;
	private String unitTransacted;
	private Date transactionDate;
	private String suspensionReason;
	private Date suspendedDate;
	private Boolean cancelled;
	
	public String getCscsTransactionId() {
		return cscsTransactionId;
	}
	public void setCscsTransactionId(String cscsTransactionId) {
		this.cscsTransactionId = cscsTransactionId;
	}
	public String getClientCompany() {
		return clientCompany;
	}
	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getUnitTransacted() {
		return unitTransacted;
	}
	public void setUnitTransacted(String unitTransacted) {
		this.unitTransacted = unitTransacted;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getSuspensionReason() {
		return suspensionReason;
	}
	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}

	public Boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Date getSuspendedDate() {
		return suspendedDate;
	}

	public void setSuspendedDate(Date suspendedDate) {
		this.suspendedDate = suspendedDate;
	}
}
