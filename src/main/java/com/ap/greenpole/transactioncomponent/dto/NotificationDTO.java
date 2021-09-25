package com.ap.greenpole.transactioncomponent.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


public class NotificationDTO {

	public String moduleName;
	public String modulePermission;
	public String approvalRequestId;
	public String dataOwnerName;
	public String dataRequesterName;
	public List<String> dataOwnerPhoneNumbers;
	public List<String> dataOwnerEmails;
	public List<String> dataRequesterPhoneNumbers;
	public List<String> dataRequesterEmails;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModulePermission() {
		return modulePermission;
	}

	public void setModulePermission(String modulePermission) {
		this.modulePermission = modulePermission;
	}

	public String getApprovalRequestId() {
		return approvalRequestId;
	}

	public void setApprovalRequestId(String approvalRequestId) {
		this.approvalRequestId = approvalRequestId;
	}

	public String getDataOwnerName() {
		return dataOwnerName;
	}

	public void setDataOwnerName(String dataOwnerName) {
		this.dataOwnerName = dataOwnerName;
	}

	public String getDataRequesterName() {
		return dataRequesterName;
	}

	public void setDataRequesterName(String dataRequesterName) {
		this.dataRequesterName = dataRequesterName;
	}

	public List<String> getDataOwnerPhoneNumbers() {
		return dataOwnerPhoneNumbers;
	}

	public void setDataOwnerPhoneNumbers(List<String> dataOwnerPhoneNumbers) {
		this.dataOwnerPhoneNumbers = dataOwnerPhoneNumbers;
	}

	public List<String> getDataOwnerEmails() {
		return dataOwnerEmails;
	}

	public void setDataOwnerEmails(List<String> dataOwnerEmails) {
		this.dataOwnerEmails = dataOwnerEmails;
	}

	public List<String> getDataRequesterPhoneNumbers() {
		return dataRequesterPhoneNumbers;
	}

	public void setDataRequesterPhoneNumbers(List<String> dataRequesterPhoneNumbers) {
		this.dataRequesterPhoneNumbers = dataRequesterPhoneNumbers;
	}

	public List<String> getDataRequesterEmails() {
		return dataRequesterEmails;
	}

	public void setDataRequesterEmails(List<String> dataRequesterEmails) {
		this.dataRequesterEmails = dataRequesterEmails;
	}

	@Override
	public String toString() {
		return "NotificationDTO{" +
				"moduleName='" + moduleName + '\'' +
				", modulePermission='" + modulePermission + '\'' +
				", approvalRequestId='" + approvalRequestId + '\'' +
				", dataOwnerName='" + dataOwnerName + '\'' +
				", dataRequesterName='" + dataRequesterName + '\'' +
				", dataOwnerPhoneNumbers=" + dataOwnerPhoneNumbers +
				", dataOwnerEmails=" + dataOwnerEmails +
				", dataRequesterPhoneNumbers=" + dataRequesterPhoneNumbers +
				", dataRequesterEmails=" + dataRequesterEmails +
				'}';
	}
}