package com.payment.streams.response;

public class SeatResponse {
	
	private Long id;
	private String ref;
	private String status;
	private Long userid;
	private Long paymentid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Long getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(Long paymentid) {
		this.paymentid = paymentid;
	}

}
