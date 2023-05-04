package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.commerce.OrderLineItem;
import com.junodx.api.models.commerce.ProviderApproval;
import com.junodx.api.models.commerce.types.OrderType;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.patient.Consent;
import com.junodx.api.models.patient.MedicalDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "laboratory_order")
public class LaboratoryOrder {

     @Id
     private String id;

     @OneToOne
     @JoinColumn(name = "order_line_item_id", nullable = false)
     @JsonIgnore
     private OrderLineItem orderLineItem;

     @Enumerated(EnumType.STRING)
     @Column(name="order_type")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private OrderType orderType;

     @Column(name="requisition_form_url")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String requisitionFormUrl;

     @Column(name="clinician_notes", length = 1000)
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String notes;

     @JsonInclude(JsonInclude.Include.NON_NULL)
     private ProviderApproval providerApproval;

     @OneToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "consent_id")
     private Consent patientConsent;

     @OneToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "laboratory_id", nullable = true)
     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
     private Laboratory lab;

     @OneToMany(mappedBy = "laboratoryOrder", cascade = CascadeType.ALL)
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private List<TestRun> testRuns;

     private String reportableTestRunId;

     private String reportableTestReportId;

     @JsonFormat
             (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
     @Column(name="date_received")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private Calendar dateReceivedInLab;

     @JsonFormat
             (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
     @Column(name = "est_arrival_in_lab")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private Calendar estArrivalInLab;

     @Column(name="lims_order_id")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String limsOrderId;

     @Column(name="lims_report_id")
     @JsonInclude(JsonInclude.Include.NON_NULL)
     private String limsReportId;

     @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinColumn(name = "patient_id")
     @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
     private User patient;

     @Enumerated(EnumType.STRING)
     protected ReportConfiguration reportConfiguration;

   //  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
   //  @JoinColumn(name = "medical_details_id", nullable = true)
   //  @JsonInclude(JsonInclude.Include.NON_NULL)
   //  private MedicalDetails medicalDetails;


     @Transient
     @JsonIgnore
     private Order parentOrder;

     @Column(name = "parentOrderId")
     private String parentOrderId;

     private Meta meta;

     public LaboratoryOrder(){
          this.id = UUID.randomUUID().toString();
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public ProviderApproval getProviderApproval() {
          return providerApproval;
     }

     public void setProviderApproval(ProviderApproval providerApproval) {
          this.providerApproval = providerApproval;
     }

     public Consent getPatientConsent() {
          return patientConsent;
     }

     public void setPatientConsent(Consent patientConsent) {
          this.patientConsent = patientConsent;
          this.patientConsent.setLaboratoryOrder(this);
     }

     /*
     public MedicalDetails getMedicalDetails() {
          return medicalDetails;
     }

     public void setMedicalDetails(MedicalDetails medicalDetails) {
          this.medicalDetails = medicalDetails;
     }

      */

     public String getRequisitionFormUrl() {
          return requisitionFormUrl;
     }

     public void setRequisitionFormUrl(String requisitionFormUrl) {
          this.requisitionFormUrl = requisitionFormUrl;
     }

     public String getNotes() {
          return notes;
     }

     public void setNotes(String notes) {
          this.notes = notes;
     }

     public Calendar getEstArrivalInLab() {
          return estArrivalInLab;
     }

     public void setEstArrivalInLab(Calendar estArrivalInLab) {
          this.estArrivalInLab = estArrivalInLab;
     }

     public Laboratory getLab() {
          return lab;
     }

     public void setLab(Laboratory lab) {
          this.lab = lab;
     }

     public Calendar getDateReceivedInLab() {
          return dateReceivedInLab;
     }

     public void setDateReceivedInLab(Calendar dateReceivedInLab) {
          this.dateReceivedInLab = dateReceivedInLab;
     }

     public List<TestRun> getTestRuns() {
          return testRuns;
     }

     public void setTestRuns(List<TestRun> testRuns) {
          this.testRuns = testRuns;
          for(TestRun run : this.testRuns)
               run.setLaboratoryOrder(this);
     }

     public void addTestRun(TestRun testRun){
          testRun.setLaboratoryOrder(this);
          this.testRuns.add(testRun);
     }

     public OrderLineItem getOrderLineItem() {
          return orderLineItem;
     }

     public void setOrderLineItem(OrderLineItem orderLineItem) {
          this.orderLineItem = orderLineItem;
     }

     public OrderType getOrderType() {
          return orderType;
     }

     public void setOrderType(OrderType orderType) {
          this.orderType = orderType;
     }

     public Meta getMeta() {
          return meta;
     }

     public void setMeta(Meta meta) {
          this.meta = meta;
     }

     public Order getParentOrder() {
          return parentOrder;
     }

     public void setParentOrder(Order parentOrder) {
          this.parentOrder = parentOrder;
          this.parentOrderId = this.parentOrder.getId();
     }

     public String getLimsOrderId() {
          return limsOrderId;
     }

     public void setLimsOrderId(String limsOrderId) {
          this.limsOrderId = limsOrderId;
     }

     public String getLimsReportId() {
          return limsReportId;
     }

     public void setLimsReportId(String limsReportId) {
          this.limsReportId = limsReportId;
     }

     public User getPatient() {
          return patient;
     }

     public void setPatient(User patient) {
          this.patient = patient;
     }

     public String getParentOrderId() {
          return parentOrderId;
     }

     public void setParentOrderId(String parentOrderId) {
          this.parentOrderId = parentOrderId;
     }

     public String getReportableTestRunId() {
          return reportableTestRunId;
     }

     public void setReportableTestRunId(String reportableTestRunId) {
          this.reportableTestRunId = reportableTestRunId;
     }

     public String getReportableTestReportId() {
          return reportableTestReportId;
     }

     public void setReportableTestReportId(String reportableTestReportId) {
          this.reportableTestReportId = reportableTestReportId;
     }

     public ReportConfiguration getReportConfiguration() {
          return reportConfiguration;
     }

     public void setReportConfiguration(ReportConfiguration reportConfiguration) {
          this.reportConfiguration = reportConfiguration;
     }


}
