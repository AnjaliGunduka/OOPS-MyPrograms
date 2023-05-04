package com.junodx.api.models.commerce;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.providers.Provider;

import javax.persistence.*;
import java.util.Calendar;

@Embeddable
public class ProviderApproval {

	private boolean requiresApproval;
	private boolean approved;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar approvalDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider_id", nullable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Provider approvingProvider;

	public boolean isRequiresApproval() {
		return requiresApproval;
	}

	public void setRequiresApproval(boolean requiresApproval) {
		this.requiresApproval = requiresApproval;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Calendar getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Calendar approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Provider getApprovingProvider() {
		return approvingProvider;
	}

	public void setApprovingProvider(Provider approvingProvider) {
		this.approvingProvider = approvingProvider;
	}

}
