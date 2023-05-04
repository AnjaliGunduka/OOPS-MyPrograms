package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.types.CustomerActionRequestType;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "lab_order_customer_action_request")
public class CustomerActionRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;

	@Enumerated(EnumType.STRING)
	@Column(name = "request_type")
	private CustomerActionRequestType customerActionRequestType;

	private String lineItemId;

	@Column(name = "active", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean active;

	@Column(name = "approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean approved;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar approvalDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Calendar resolveByDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar resolvedAt;

	private Meta meta;

	public CustomerActionRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CustomerActionRequestType getCustomerActionRequestType() {
		return customerActionRequestType;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setCustomerActionRequestType(CustomerActionRequestType customerActionRequestType) {
		this.customerActionRequestType = customerActionRequestType;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Calendar getResolveByDate() {
		return resolveByDate;
	}

	public void setResolveByDate(Calendar resolveByDate) {
		this.resolveByDate = resolveByDate;
	}

	public Calendar getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(Calendar resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Calendar getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Calendar approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
