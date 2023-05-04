package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.commerce.types.TaxType;

import javax.persistence.*;

@Embeddable
public class Tax {

	@Column(name = "tax_amount")
	private float amount;

	@Column(name = "tax_rate")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private float rate;

	@Enumerated(EnumType.STRING)
	@Column(name = "tax_type")
	private TaxType type;

	@Column(name = "tax_jurisdiction")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String jurisdiction;

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public TaxType getType() {
		return type;
	}

	public void setType(TaxType type) {
		this.type = type;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

}
