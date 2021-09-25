package com.ap.greenpole.transactioncomponent.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ap.greenpole.transactioncomponent.dto.Result;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApprovalStatus;
import com.ap.greenpole.transactioncomponent.repository.ModuleRequestRepository;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.model.User;

@Service
public class ModuleRequestServiceImpl implements ModuleRequestService {

	private static Logger logger = LoggerFactory.getLogger(ModuleRequestServiceImpl.class);

	@Autowired
	private ModuleRequestRepository moduleRequestRepository;

	@Override
	public ModuleRequest createApprovalRequest(ModuleRequest moduleRequest, List<String> dataOwnerEmail, String dataOwnerName,
									  List<String> dataOwnerPhones, User user) {

		moduleRequest.setStatus(ApprovalStatus.PENDING.getCode());
		moduleRequest.setCreatedOn(new Date());
		moduleRequest.setRequesterId(user.getId());
		moduleRequest.setModules(ApplicationConstant.MODULE);
		moduleRequest.setRequestCode(requestCode(ApplicationConstant.MODULE));
		moduleRequest = moduleRequestRepository.save(moduleRequest);
		logger.info("[+] Resource Creation Response {} ", moduleRequest);

//		Notification notification = new Notification(dataOwnerName, user.getFirstName(), dataOwnerPhones,
//				dataOwnerEmail, Utils.commaSeperatedToList(user.getPhone()),
//				Utils.commaSeperatedToList(user.getEmail()));
//
//		determineVerdictAndCallNotificationService(notification, ConstantUtils.PENDING);

		return moduleRequest;
	}

	private String requestCode(String stockBroker) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmsss");
		String date = simpleDateFormat.format(new Date());
		return stockBroker + date;
	}

	@Override
	public ModuleRequest getApprovalRequestById(Long approvalRequestId) {
		return moduleRequestRepository.findModuleRequestByRequestIdAndModules(approvalRequestId, ApplicationConstant.MODULE);
	}

	@Override
	public ModuleRequest save(ModuleRequest moduleRequest) {
		logger.info("[+] Creating Resource {} ", moduleRequest);
		return moduleRequestRepository.save(moduleRequest);
	}
	
	@Override
	public List<ModuleRequest> getAllApprovalRequest() {
		return moduleRequestRepository.findAllModuleRequestByModules(ApplicationConstant.MODULE);
	}
	
	@Override
	public Result<ModuleRequest> getAllApprovalRequest(int pageNumber, int pageSize, Pageable pageable) {

		Page<ModuleRequest> allRecords = moduleRequestRepository.findAllModuleRequestByModules(ApplicationConstant.MODULE,
				pageable);
		long noOfRecords = allRecords.getTotalElements();

		return new Result<>(0, allRecords.getContent(), noOfRecords, pageNumber, pageSize);
	}
	
	@Override
	public Result<ModuleRequest> getTransactionApprovalNotificationsByStatusAndType(Pageable pageable, String status,
			String type, Long userId) {
		
		if(Utils.isEmptyString(type) || ApplicationConstant.TYPES[1].equalsIgnoreCase(type)) {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndStatus(ApplicationConstant.MODULE,
							Utils.getApprovalStatusCode(status), pageable);     

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		}else {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndStatusAndRequesterId(ApplicationConstant.MODULE,
							Utils.getApprovalStatusCode(status), userId, pageable);     

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		}
	}
	
	@Override
	public Result<ModuleRequest> getApprovalRequestSearch(String query, String status, String type, Long userId,
			Pageable pageable) {
		if (Utils.isEmptyString(type) || ApplicationConstant.TYPES[1].equalsIgnoreCase(type)) {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndStatusAndOldRecordContainingOrNewRecordContaining(
							ApplicationConstant.MODULE, Utils.getApprovalStatusCode(status), query, query, pageable);

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		} else {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndStatusAndRequesterIdAndOldRecordContainingOrNewRecordContaining(
							ApplicationConstant.MODULE, Utils.getApprovalStatusCode(status), userId, query, query, pageable);

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		}
	}
	
	@Override
	public Result<ModuleRequest> getTransactionApprovalNotificationByDateCreatedAndStatusAndType(Date dateObject,
			String status, String type, Long userId, Pageable pageable) {
		DateTime dateTime = new DateTime(dateObject);
		DateTime dateBound = dateTime.plusDays(1);
		if (Utils.isEmptyString(type) || ApplicationConstant.TYPES[1].equalsIgnoreCase(type)) {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndCreatedOnBetweenAndStatus(ApplicationConstant.MODULE, dateObject,
							dateBound.toDate(), Utils.getApprovalStatusCode(status), pageable);

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		}else {
			Page<ModuleRequest> allRecords = moduleRequestRepository
					.findAllModuleRequestByModulesAndCreatedOnBetweenAndStatusAndRequesterId(ApplicationConstant.MODULE, dateObject,
							dateBound.toDate(), Utils.getApprovalStatusCode(status), userId, pageable);

			return new Result<>(0, allRecords.getContent(), allRecords.getTotalElements(), pageable.getPageNumber() + 1,
					pageable.getPageSize());
		}
	}
}
