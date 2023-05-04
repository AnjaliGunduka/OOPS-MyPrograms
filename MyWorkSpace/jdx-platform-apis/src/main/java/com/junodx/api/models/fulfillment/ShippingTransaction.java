package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(name = "shipping_transaction")
public class ShippingTransaction {

	@Id
	private String id;

	@OneToOne
	@JoinColumn(name = "shipping_details_id", nullable = false)
	@JsonIgnore
	private ShippingDetails shippingDetails;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar transactionDate;

	private float amount;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Currency currency;

	public ShippingTransaction() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ShippingDetails getShippingDetails() {
		return shippingDetails;
	}

	public void setShippingDetails(ShippingDetails shippingDetails) {
		this.shippingDetails = shippingDetails;
	}

	public Calendar getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Calendar transactionDate) {
		this.transactionDate = transactionDate;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
