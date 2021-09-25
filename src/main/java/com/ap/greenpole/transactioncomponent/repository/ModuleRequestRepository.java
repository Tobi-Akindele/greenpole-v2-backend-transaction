package com.ap.greenpole.transactioncomponent.repository;

import com.ap.greenpole.transactioncomponent.entity.ModuleRequest;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRequestRepository extends JpaRepository<ModuleRequest, Long> {

	ModuleRequest findModuleRequestByRequestIdAndModules(Long requestId, String modules);

	public List<ModuleRequest> findAllModuleRequestByModules(String modules);

	public Page<ModuleRequest> findAllModuleRequestByModules(String modules, Pageable pageable);

	public Page<ModuleRequest> findAllModuleRequestByModulesAndStatus(String module, int status, Pageable pageable);

	public Page<ModuleRequest> findAllModuleRequestByModulesAndStatusAndRequesterId(String module, int status,
			Long requesterId, Pageable pageable);

	public Page<ModuleRequest> findAllModuleRequestByModulesAndStatusAndOldRecordContainingOrNewRecordContaining(
			String module, int status, String oldRecord, String newRecord, Pageable pageable);
	
	public Page<ModuleRequest> findAllModuleRequestByModulesAndStatusAndRequesterIdAndOldRecordContainingOrNewRecordContaining(
			String module, int status, Long userId, String oldRecord, String newRecord, Pageable pageable);
	
	public Page<ModuleRequest> findAllModuleRequestByModulesAndCreatedOnBetweenAndStatus(String module, Date startDate,
			Date endDate, int status, Pageable pageable);
	
	public Page<ModuleRequest> findAllModuleRequestByModulesAndCreatedOnBetweenAndStatusAndRequesterId(String module,
			Date dateObject, Date date, int status, Long userId, Pageable pageable);
}
