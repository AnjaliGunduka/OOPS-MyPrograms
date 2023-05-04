package com.junodx.api.models.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.payment.types.PaymentInstrumentType;
import com.junodx.api.models.payment.types.TransactionType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	private String id;

	private String transactionId;
	private TransactionType type;
	private String externalTransactionId;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	@JsonIgnore
	private Order order;

	@OneToOne
	@JoinColumn(name = "processor_id")
	private PaymentProcessorProvider processor;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_instrument_type")
	private PaymentInstrumentType paymentInstrumentType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar createdAt;
	private Calendar createdBy;

	@Type(type = "json")
	@Column(name = "transaction_raw_json", columnDefinition = "json")
	@JsonIgnore
	private String transactionJson;

	public Transaction() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionJson() {
		return transactionJson;
	}

	public void setTransactionJson(String transactionJson) {
		this.transactionJson = transactionJson;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public PaymentProcessorProvider getProcessor() {
		return processor;
	}

	public void setProcessor(PaymentProcessorProvider processor) {
		this.processor = processor;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public Calendar getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Calendar createdBy) {
		this.createdBy = createdBy;
	}

	public PaymentInstrumentType getPaymentInstrumentType() {
		return paymentInstrumentType;
	}

	public void setPaymentInstrumentType(PaymentInstrumentType paymentInstrumentType) {
		this.paymentInstrumentType = paymentInstrumentType;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
