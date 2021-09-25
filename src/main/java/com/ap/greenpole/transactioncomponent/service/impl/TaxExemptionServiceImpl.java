package com.ap.greenpole.transactioncomponent.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ap.greenpole.transactioncomponent.entity.TaxExemptionValidator;
import com.ap.greenpole.transactioncomponent.repository.TaxExemptionRepository;
import com.ap.greenpole.transactioncomponent.service.TaxExemptionService;

@Service
public class TaxExemptionServiceImpl implements TaxExemptionService {

	@Autowired
	private TaxExemptionRepository taxExemptionRepository;
	
	@Override
	public List<TaxExemptionValidator> getTaxExemptionValidator() {
		return taxExemptionRepository.findAll();
	}

	@Override
	public TaxExemptionValidator saveTaxExemptionValidator(TaxExemptionValidator tev) {
		return taxExemptionRepository.save(tev);
	}

}
