package com.ap.greenpole.transactioncomponent.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ap.greenpole.transactioncomponent.dto.Result;
import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.ModuleRequestService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;
import com.ap.greenpole.usermodule.annotation.PreAuthorizePermission;
import com.ap.greenpole.usermodule.model.User;
import com.ap.greenpole.usermodule.service.UserService;

/**
 * Created By: Oyindamola Akindele
 * Date: 8/11/2020 2:30 AM
 */

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class ModuleRequestController {

	private static Logger logger = LoggerFactory.getLogger(ModuleRequestController.class);
	
	@Autowired
	private ModuleRequestService moduleRequestService;
	
	@Autowired
    private UserService userService;
	
	@GetMapping(value = "/request/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<ModuleRequest> getTransactionApprovalRequestById(@PathVariable long requestId) {
		logger.info("[+] In getTransactionApprovalRequestById with approvalRequestId: {}", requestId);
		DefaultResponse<ModuleRequest> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		try {
			if (requestId <= 0) {
				response.setStatusMessage("Approval request ID is required");
				return response;
			}
			ModuleRequest moduleRequest = moduleRequestService.getApprovalRequestById(requestId);
			logger.info("getApprovalRequestById returned {}", moduleRequest);
			if (moduleRequest != null) {
				response.setStatus(ApiResponseCode.SUCCESS.getCode());
				response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
				response.setData(moduleRequest);
				return response;
			}
			response.setStatusMessage("Transaction Approval request not found");
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getTransactionApprovalRequestById {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
	
	@GetMapping(value = "/requests/all", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<List<ModuleRequest>> getAllTransactionApprovalRequests() {
		logger.info("[+] In getAllTransactionApprovalRequests");
		DefaultResponse<List<ModuleRequest>> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		List<ModuleRequest> allApprovalRequest = null;
		try {
			allApprovalRequest = moduleRequestService.getAllApprovalRequest();
			if(allApprovalRequest != null && !allApprovalRequest.isEmpty()){
				response.setStatus(ApiResponseCode.SUCCESS.getCode());
				response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
				response.setData(allApprovalRequest);
				return response;
			}
			response.setStatusMessage("No result found");
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getAllTransactionApprovalRequests {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
		return response;
	}
	
	@GetMapping(value = "/requests/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<List<ModuleRequest>> getAllTransactionApprovalRequestsPaginated(
			@RequestHeader(value = "pageSize", required = false, defaultValue = "" + Integer.MAX_VALUE) String pageSize,
			@RequestHeader(value = "pageNumber", required = false, defaultValue = "1") String pageNumber) {
		logger.info("[+] In getAllTransactionApprovalRequestsPaginated with pageSize: {},  pageNumber : {}", pageSize, pageNumber);
		DefaultResponse<List<ModuleRequest>> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		try {
            logger.info("[+] Attempting to parse the pagination variables");
            int page = Integer.parseInt(pageNumber);
            int size = Integer.parseInt(pageSize);
            page = Math.max(0, page - 1);
            size = Math.max(1, size);
            Pageable pageable = PageRequest.of(page, size, Sort.by("requestId").descending());

			Result<ModuleRequest> allApprovalRequest = moduleRequestService
					.getAllApprovalRequest(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), pageable);
			response.setStatus(ApiResponseCode.SUCCESS.getCode());
			response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
			response.setData(allApprovalRequest.getList());
			response.setCount(allApprovalRequest.getNoOfRecords());
			return response;
        } catch (NumberFormatException e) {
            logger.error("[-] Error {} occurred while parsing page variable with message: {}",
            		e.getClass().getSimpleName(), e.getMessage());
			response.setStatusMessage("The entered page and size must be integer values.");
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getAllTransactionApprovalRequestsPaginated {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
		}
		return response;
	}
		
	@GetMapping(value = "/requests", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<List<ModuleRequest>> getTransactionApprovalRequestsByStatusAndType(
			@RequestParam("status") String status, @RequestParam("offset") int offset, @RequestParam("limit") int limit,
			@RequestParam("type") String type, HttpServletRequest request) {
		logger.info("[+] In getTransactionApprovalRequestsByStatusAndType with status: {}, offset: {}, limit: {}, type: {}",
				status, offset, limit, type);
		DefaultResponse<List<ModuleRequest>> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		
		Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
    	if(!user.isPresent()) {
    		response.setStatusMessage("User details not found");
    		return response;
    	}
		
		try {
			status = Utils.capitalizeFirstLetter(status);
			if (!Arrays.asList(ApplicationConstant.APPROVAL_STATUS).contains(status)) {
				response.setStatusMessage("Invalid status [" + status + "], Options include "
						+ Arrays.toString(ApplicationConstant.APPROVAL_STATUS));
				return response;
			}
			if(!Utils.isEmptyString(type) && !Arrays.asList(ApplicationConstant.TYPES).contains(type.toUpperCase())) {
				response.setStatusMessage("Invalid type [" + type + "], Options include "
						+ Arrays.toString(ApplicationConstant.TYPES));
				return response;
			}
			
			logger.info("[+] Attempting to parse the pagination variables");
			int page = Math.max(0, offset - 1);
			int size = Math.max(1, limit);
			Pageable pageable = PageRequest.of(page, size, Sort.by("requestId").descending());

			Result<ModuleRequest> moduleRequest = moduleRequestService
					.getTransactionApprovalNotificationsByStatusAndType(pageable, status, type, user.get().getId());
			logger.info("getTransactionApprovalNotificationsByStatusAndType returned {}", moduleRequest);
			if (moduleRequest != null) {
				response.setStatus(ApiResponseCode.SUCCESS.getCode());
				response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
				response.setData(moduleRequest.getList());
				response.setCount(moduleRequest.getNoOfRecords());
				return response;
			}
			response.setStatusMessage("Transaction Approval request not found");
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getTransactionApprovalRequestsByStatusAndType {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
	
	@GetMapping(value = "/requests/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<List<ModuleRequest>> getTransactionApprovalSearch(@RequestParam("query") String query,
			@RequestParam("status") String status, @RequestParam("offset") int offset, @RequestParam("limit") int limit,
			@RequestParam("type")String type, HttpServletRequest request) {
		logger.info("[+] In getTransactionApprovalSearch with query: {}, status: {}, type: {}", query, status, type);
		DefaultResponse<List<ModuleRequest>> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		
		Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
    	if(!user.isPresent()) {
    		response.setStatusMessage("User details not found");
    		return response;
    	}
		
		try {
			
			status = Utils.capitalizeFirstLetter(status);
			if (!Arrays.asList(ApplicationConstant.APPROVAL_STATUS).contains(status)) {
				response.setStatusMessage("Invalid status [" + status + "], Options include "
						+ Arrays.toString(ApplicationConstant.APPROVAL_STATUS));
				return response;
			}
			if(!Utils.isEmptyString(type) && !Arrays.asList(ApplicationConstant.TYPES).contains(type.toUpperCase())) {
				response.setStatusMessage("Invalid type [" + type + "], Options include "
						+ Arrays.toString(ApplicationConstant.TYPES));
				return response;
			}
			
			logger.info("[+] Attempting to parse the pagination variables");
			int page = Math.max(0, offset - 1);
			int size = Math.max(1, limit);
			Pageable pageable = PageRequest.of(page, size, Sort.by("requestId").descending());
			
			Result<ModuleRequest> moduleRequest = moduleRequestService.getApprovalRequestSearch(query, status, type,
					user.get().getId(), pageable);
			logger.info("getApprovalRequestSearch returned {}", moduleRequest);
			if (moduleRequest != null) {
				response.setStatus(ApiResponseCode.SUCCESS.getCode());
				response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
				response.setData(moduleRequest.getList());
				response.setCount(moduleRequest.getNoOfRecords());
				return response;
			}
			response.setStatusMessage("Transaction Approval request not found");
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getTransactionApprovalSearch {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
	
	@GetMapping(value = "/requests/filter", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorizePermission({"PERMISSION_RETRIEVE_TRANSACTION_REQUEST"})
	public DefaultResponse<List<ModuleRequest>> getTransactionApprovalRequestsByDateCreatedAndStatusAndType(@RequestParam("date")String date,
			@RequestParam("status") String status, @RequestParam("offset") int offset, @RequestParam("limit") int limit,
			@RequestParam("type") String type, HttpServletRequest request) {
		logger.info(
				"[+] In getTransactionApprovalRequestsByDateCreatedAndStatusAndType with date: {}, offset: {}, limit: {}, type: {}",
				date, offset, limit);
		DefaultResponse<List<ModuleRequest>> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		
		Optional<User> user = userService.memberFromAuthorization(request.getHeader(ApplicationConstant.AUTHORIZATION));
    	if(!user.isPresent()) {
    		response.setStatusMessage("User details not found");
    		return response;
    	}
		
		try {
			
			status = Utils.capitalizeFirstLetter(status);
			if (!Arrays.asList(ApplicationConstant.APPROVAL_STATUS).contains(status)) {
				response.setStatusMessage("Invalid status [" + status + "], Options include "
						+ Arrays.toString(ApplicationConstant.APPROVAL_STATUS));
				return response;
			}
			if(!Utils.isEmptyString(type) && !Arrays.asList(ApplicationConstant.TYPES).contains(type.toUpperCase())) {
				response.setStatusMessage("Invalid type [" + type + "], Options include "
						+ Arrays.toString(ApplicationConstant.TYPES));
				return response;
			}
			if (Utils.isEmptyString(date)) {
				response.setStatusMessage("Date is required");
				return response;
			}
			if (!Utils.isValidDate(ApplicationConstant.FILTER_DATE_FORMAT, date)) {
				response.setStatusMessage("Date [" + date + "] is invalid, please use " + ApplicationConstant.FILTER_DATE_FORMAT);
				return response;
			}
			Date dateObject = Utils.getDate(ApplicationConstant.FILTER_DATE_FORMAT, date);
			if (dateObject == null) {
				response.setStatusMessage("date parsed is null");
				return response;
			}
			
			logger.info("[+] Attempting to parse the pagination variables");
			int page = Math.max(0, offset - 1);
			int size = Math.max(1, limit);
			Pageable pageable = PageRequest.of(page, size, Sort.by("requestId").descending());

			Result<ModuleRequest> moduleRequest = moduleRequestService
					.getTransactionApprovalNotificationByDateCreatedAndStatusAndType(dateObject, status, type, user.get().getId(), pageable);
			logger.info("getTransactionApprovalNotificationByDateCreatedAndStatusAndType returned {}", moduleRequest);
			if (moduleRequest != null) {
				response.setStatus(ApiResponseCode.SUCCESS.getCode());
				response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
				response.setData(moduleRequest.getList());
				response.setCount(moduleRequest.getNoOfRecords());
				return response;
			}
			response.setStatusMessage("Transaction Approval request not found");
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting getTransactionApprovalRequestsByDateCreatedAndStatusAndType {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
}
