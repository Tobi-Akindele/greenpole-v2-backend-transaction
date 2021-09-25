package com.ap.greenpole.transactioncomponent.service;

import java.util.List;

import com.ap.greenpole.transactioncomponent.entity.TaxExemptionValidator;

public interface TaxExemptionService {

	List<TaxExemptionValidator> getTaxExemptionValidator();

	TaxExemptionValidator saveTaxExemptionValidator(TaxExemptionValidator tev);
}
