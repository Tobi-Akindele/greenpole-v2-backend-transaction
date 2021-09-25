package com.ap.greenpole.transactioncomponent.service;


import com.ap.greenpole.transactioncomponent.dto.Result;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.usermodule.model.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ModuleRequestService {

	ModuleRequest createApprovalRequest(ModuleRequest moduleRequest, List<String> dataOwnerEmail, String dataOwnerName,
							   List<String> dataOwnerPhones, User user);

	ModuleRequest getApprovalRequestById(Long approvalRequestId);

	ModuleRequest save(ModuleRequest moduleRequest);

	List<ModuleRequest> getAllApprovalRequest();
	
	Result<ModuleRequest> getAllApprovalRequest(int pageNumber, int pageSize, Pageable pageable);
	
	Result<ModuleRequest> getTransactionApprovalNotificationsByStatusAndType(Pageable pageable, String status,
			String type, Long userId);
	
	Result<ModuleRequest> getApprovalRequestSearch(String query, String status, String type, Long userId, Pageable pageable);
	
	Result<ModuleRequest> getTransactionApprovalNotificationByDateCreatedAndStatusAndType(Date dateObject,
			String status, String type, Long userId, Pageable pageable);
}
