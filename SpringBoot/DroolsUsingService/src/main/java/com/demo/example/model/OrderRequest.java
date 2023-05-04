package com.demo.example.model;

public class OrderRequest {
	 private String customerNumber;
	    private Integer age;
	    private Integer amount;
	    private CustomerType customerType;
		public String getCustomerNumber() {
			return customerNumber;
		}
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public Integer getAmount() {
			return amount;
		}
		public void setAmount(Integer amount) {
			this.amount = amount;
		}
		public CustomerType getCustomerType() {
			return customerType;
		}
		public void setCustomerType(CustomerType customerType) {
			this.customerType = customerType;
		}
	    
}
