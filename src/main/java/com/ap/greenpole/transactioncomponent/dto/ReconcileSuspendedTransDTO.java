package com.ap.greenpole.transactioncomponent.dto;

import java.util.Date;

public class ReconcileSuspendedTransDTO {

	private Date date;

	private Date transDate;

	private String cscsTransId;

	private String seller;

	private String buyer;

	private String clientCompany;

	private String unit;

	private String reason;

	private Boolean isCancel;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getCscsTransId() {
		return cscsTransId;
	}

	public void setCscsTransId(String cscsTransId) {
		this.cscsTransId = cscsTransId;
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

	public String getClientCompany() {
		return clientCompany;
	}

	public void setClientCompany(String clientCompany) {
		this.clientCompany = clientCompany;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Boolean getCancel() {
		return isCancel;
	}

	public void setCancel(Boolean cancel) {
		isCancel = cancel;
	}

	@Override
	public String toString() {
		return "ReconcileSuspendedTransDTO{" +
				"date=" + date +
				", transDate=" + transDate +
				", cscsTransId='" + cscsTransId + '\'' +
				", seller='" + seller + '\'' +
				", buyer='" + buyer + '\'' +
				", clientCompany='" + clientCompany + '\'' +
				", unit='" + unit + '\'' +
				", reason='" + reason + '\'' +
				", isCancel=" + isCancel +
				'}';
	}
}
