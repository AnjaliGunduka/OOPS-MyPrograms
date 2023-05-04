package com.student.library.services.enums;

public enum RequestStatus {
	REQUESTED,APPROVED,REJECTED,RETURNED;
	
	public String getValue() {
        return this.toString();
    }
}
