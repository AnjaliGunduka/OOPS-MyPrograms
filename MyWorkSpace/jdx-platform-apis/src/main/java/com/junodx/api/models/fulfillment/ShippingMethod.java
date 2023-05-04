package com.junodx.api.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.fulfillment.types.ShippingDeliveryType;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "shipping_method")
public class ShippingMethod {
	@Id
	private String id;

	@OneToOne
	@JoinColumn(name = "shipping_details_id", nullable = false)
	@JsonIgnore
	private ShippingDetails shippingDetails;

	@OneToOne
	@JoinColumn(name = "shipping_carrer_id", unique = false)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ShippingCarrier carrier;

	@Enumerated(EnumType.STRING)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ShippingDeliveryType type;

	private String deliveryDescription;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String trackingCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String trackingUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Calendar eta;

	@Column(name = "is_shipped", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean shipped;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Column(name = "label_url", length = 1024)
	private String labelUrl;

	private String labelId;

	@Column(name = "is_return", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
	private boolean isReturn;

	public ShippingMethod() {
		this.id = UUID.randomUUID().toString();
	}

	public ShippingCarrier getCarrier() {
		return carrier;
	}

	public void setCarrier(ShippingCarrier carrier) {
		this.carrier = carrier;
	}

	public ShippingDeliveryType getType() {
		return type;
	}

	public void setType(ShippingDeliveryType type) {
		this.type = type;
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

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public String getLabelUrl() {
		return labelUrl;
	}

	public void setLabelUrl(String labelUrl) {
		this.labelUrl = labelUrl;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public Calendar getEta() {
		return eta;
	}

	public void setEta(Calendar eta) {
		this.eta = eta;
	}

	public String getDeliveryDescription() {
		return deliveryDescription;
	}

	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}

	public boolean isReturn() {
		return isReturn;
	}

	public void setReturn(boolean aReturn) {
		isReturn = aReturn;
	}
}
