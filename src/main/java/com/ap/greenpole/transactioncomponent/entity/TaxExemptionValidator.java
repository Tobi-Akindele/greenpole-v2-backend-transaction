package com.ap.greenpole.transactioncomponent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
@Table(name = "tbl_tax_exemption_validator")
public class TaxExemptionValidator {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String keyWords;
	
	public TaxExemptionValidator() {
	}

	public TaxExemptionValidator(String keyWords) {
		this.keyWords = keyWords;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
