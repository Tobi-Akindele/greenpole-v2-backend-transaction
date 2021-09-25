package com.ap.greenpole.transactioncomponent.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ap.greenpole.transactioncomponent.entity.TaxExemptionValidator;
import com.ap.greenpole.transactioncomponent.enums.ApiResponseCode;
import com.ap.greenpole.transactioncomponent.response.DefaultResponse;
import com.ap.greenpole.transactioncomponent.service.TaxExemptionService;
import com.ap.greenpole.transactioncomponent.util.ApplicationConstant;
import com.ap.greenpole.transactioncomponent.util.Utils;

@RestController
@RequestMapping(ApplicationConstant.BASE_CONTEXT_URL)
public class TaxExemptionController {
	
	private static Logger logger = LoggerFactory.getLogger(TaxExemptionController.class);

	@Autowired
	private TaxExemptionService taxExemptionService;
	
	@PostMapping(value = "/tax/exemption/validator", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DefaultResponse<TaxExemptionValidator> addTaxExemptionKeywords(@RequestBody @Validated TaxExemptionValidator request){
		logger.info("[+] In addTaxExemptionKeywords with TaxExemptionValidator: {}", request);
		DefaultResponse<TaxExemptionValidator> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		try {
			if(request == null || Utils.isEmptyString(request.getKeyWords())) {
				response.setStatusMessage("RequestBody or keyWords is required");
				return response;
			}
			TaxExemptionValidator tev = null;
			List<TaxExemptionValidator> taxExemptionValidator = taxExemptionService.getTaxExemptionValidator();
			if(taxExemptionValidator != null && !taxExemptionValidator.isEmpty()) {
				tev = taxExemptionValidator.get(0);
				tev.setKeyWords(request.getKeyWords());
			} else {
				tev = new TaxExemptionValidator(request.getKeyWords());
			}
			tev = taxExemptionService.saveTaxExemptionValidator(tev);
			logger.info("saveTaxExemptionValidator returned {}", tev);
			
			response.setStatus(ApiResponseCode.SUCCESS.getCode());
			response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
			response.setData(tev);
			
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while adding TaxExemptionValidatorKeywords {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
	
	@GetMapping(value = "/tax/exemption/validator", produces = MediaType.APPLICATION_JSON_VALUE)
	public DefaultResponse<TaxExemptionValidator> getTaxExemptionKeywords(){
		logger.info("[+] In getTaxExemptionKeywords ");
		DefaultResponse<TaxExemptionValidator> response = new DefaultResponse<>();
		response.setStatus(ApiResponseCode.FAILED.getCode());
		response.setStatusMessage(ApiResponseCode.FAILED.getDescription());
		try {
			
			TaxExemptionValidator tev = null;
			List<TaxExemptionValidator> taxExemptionValidator = taxExemptionService.getTaxExemptionValidator();
			if(taxExemptionValidator != null && !taxExemptionValidator.isEmpty()) {
				tev = taxExemptionValidator.get(0);
			}
			
			response.setStatus(ApiResponseCode.SUCCESS.getCode());
			response.setStatusMessage(ApiResponseCode.SUCCESS.getDescription());
			response.setData(tev);
			
			return response;
		} catch (Exception e) {
			logger.info("[-] Exception happened while getting TaxExemptionValidator {}", e.getMessage());
			response.setStatusMessage("Error Processing Request");
			return response;
		}
	}
}
