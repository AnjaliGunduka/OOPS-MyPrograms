package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.types.LaboratoryStatusType;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "laboratory_status")
public class LaboratoryStatus implements Comparable<LaboratoryStatus> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private LaboratoryStatusType status;

	@ManyToOne
	@JoinColumn(name = "test_run_id", nullable = false)
	@JsonIgnore
	private TestRun testRun;

	private boolean isCurrent;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Calendar createdAt;

	private String createdBy;
//	new LaboratoryStatus(LaboratoryStatusType.RESULT_AWAITING_REVIEW, run, true)
	
	public LaboratoryStatus(LaboratoryStatusType type, TestRun run, boolean current){
        this.status = type;
        this.testRun = run;
        this.isCurrent = current;
        this.createdAt = Calendar.getInstance();
    }

	public LaboratoryStatus() {
		super();
	}

	public LaboratoryStatusType getStatus() {
		return status;
	}

	public void setStatus(LaboratoryStatusType status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean current) {
		isCurrent = current;
	}

	public Calendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public TestRun getTestRun() {
		return testRun;
	}

	public void setTestRun(TestRun testRun) {
		this.testRun = testRun;
	}

	@Override
	public int compareTo(LaboratoryStatus o) {
		// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
		return this.getCreatedAt().compareTo(o.getCreatedAt());
	}
}
