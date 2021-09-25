package com.ap.greenpole.transactioncomponent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ap.greenpole.transactioncomponent.entity.TaxExemptionValidator;

@Repository
public interface TaxExemptionRepository extends JpaRepository<TaxExemptionValidator, Long> {

}
