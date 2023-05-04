package com.junodx.api.models.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.core.types.DataType;
import com.junodx.api.models.patient.types.VitalType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "patient_vital")
public class Vital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@Enumerated(EnumType.STRING)
	private VitalType type;

	@Column(name = "value", columnDefinition = "TEXT")
	private String value;

	@Enumerated(EnumType.STRING)
	@Column(name = "value_type")
	private DataType valueType;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Calendar recordedAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String recordedBy;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "medical_details_id")
	@JsonIgnore
	private MedicalDetails medicalDetails;

	public Vital() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VitalType getType() {
		return type;
	}

	public void setType(VitalType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Calendar getRecordedAt() {
		return recordedAt;
	}

	public void setRecordedAt(Calendar recordedAt) {
		this.recordedAt = recordedAt;
	}

	public String getRecordedBy() {
		return recordedBy;
	}

	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}

	public MedicalDetails getMedicalDetails() {
		return medicalDetails;
	}

	public void setMedicalDetails(MedicalDetails medicalDetails) {
		this.medicalDetails = medicalDetails;
	}

	public DataType getValueType() {
		return valueType;
	}

	public void setValueType(DataType valueType) {
		this.valueType = valueType;
	}
}
