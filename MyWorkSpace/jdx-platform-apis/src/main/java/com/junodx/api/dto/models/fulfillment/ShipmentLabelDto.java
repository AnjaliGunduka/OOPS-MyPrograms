package com.junodx.api.dto.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ShipmentLabelDto {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String orderId;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String> lineItemIds;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private boolean shippingSuccessStatus;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private float shippingRate;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String trackingNumber;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String trackingStatus;
  private String trackingUrl;
  private String labelUrl;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String eta;
  private String labelId;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public List<String> getLineItemIds() {
    return lineItemIds;
  }

  public void setLineItemIds(List<String> lineItemIds) {
    this.lineItemIds = lineItemIds;
  }

  public boolean isShippingSuccessStatus() {
    return shippingSuccessStatus;
  }

  public void setShippingSuccessStatus(boolean shippingSuccessStatus) {
    this.shippingSuccessStatus = shippingSuccessStatus;
  }

  public float getShippingRate() {
    return shippingRate;
  }

  public void setShippingRate(float shippingRate) {
    this.shippingRate = shippingRate;
  }

  public String getTrackingNumber() {
    return trackingNumber;
  }

  public void setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
  }

  public String getTrackingStatus() {
    return trackingStatus;
  }

  public void setTrackingStatus(String trackingStatus) {
    this.trackingStatus = trackingStatus;
  }

  public String getTrackingUrl() {
    return trackingUrl;
  }

  public void setTrackingUrl(String trackingUrl) {
    this.trackingUrl = trackingUrl;
  }

  public String getLabelUrl() {
    return labelUrl;
  }

  public void setLabelUrl(String labelUrl) {
    this.labelUrl = labelUrl;
  }

  public String getEta() {
    return eta;
  }

  public void setEta(String eta) {
    this.eta = eta;
  }

  public String getLabelId() {
    return labelId;
  }

  public void setLabelId(String labelId) {
    this.labelId = labelId;
  }

}
