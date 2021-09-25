package com.ap.greenpole.transactioncomponent.dto;

import java.util.List;

public class RequestAuthorizationDTO {

	private List<Long> requestIds;

	private String action;

	private String rejectionReason;

	private Long approverId;

	public List<Long> getRequestIds() {
		return requestIds;
	}

	public void setRequestIds(List<Long> requestIds) {
		this.requestIds = requestIds;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	@Override
	public String toString() {
		return "RequestAuthorization{" +
				"requestIds=" + requestIds +
				", action='" + action + '\'' +
				", rejectionReason='" + rejectionReason + '\'' +
				", approverId=" + approverId +
				'}';
	}
}
