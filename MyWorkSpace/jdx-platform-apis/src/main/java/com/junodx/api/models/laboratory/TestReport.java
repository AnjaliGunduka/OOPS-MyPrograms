package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.*;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.laboratory.reports.Report;
import com.junodx.api.models.laboratory.reports.types.SignedOutType;
import com.junodx.api.models.laboratory.types.ConfidenceIndexType;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.models.laboratory.types.ReportType;
import com.junodx.api.models.patient.PatientDetails;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.UUID;

@Entity
@Table(name="test_report")
public class TestReport {
    @Id
    private String id;

    @Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name="report_configuration")
    private ReportConfiguration reportConfiguration;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "estimated_to_be_available_at")
    private Calendar estimatedToBeAvailableAt;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "first_available_at")
    private Calendar firstAvailableAt;

    @Column(name = "approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean approved;

    @Column(name = "signed_out", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean signedOut;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="signout_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Signout signoutDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="results_url")
    private String resultsUrl;

    @JoinColumn(name = "order_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderId;

    @Column(name = "order_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orderNumber;

    @JoinColumn(name = "lab_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String labId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User patient;

    @JoinColumn(name="laboratory_order_id", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String laboratoryOrderId;

    @Column(name = "no_order", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean noOrder;

    @Column(name = "control", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean control;

    @Column(name = "research_sample")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean researchSample;

    @Enumerated(EnumType.STRING)
    @Column(name="report_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReportType reportType;

    @Column(name = "research_project_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String researchProjectName;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "test_run_id", nullable = true, unique = true)
    @JsonIgnore
    private TestRun testRun;

    @Column(name = "batch_run_id", nullable = true)
    private String batchRunId;

    @Column(name = "pipeline_run_id", nullable = true)
    private String pipelineRunId;

    @Column(name = "sequence_run_id", nullable = true)
    private String sequenceRunId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="completed_at")
    private Calendar completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="signed_out_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected SignedOutType signedOutType;

    @OneToOne(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Report resultData;

    //Note, its possible that the same sampleNumber would be used across different testRuns, but the testRuns must
    //be different
    @Column(name = "sample_number", nullable = false)
    private String sampleNumber;

    @Column(name = "delivered_to_provider", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deliveredToProvider;

    @Column(name = "delivered_to_patient", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deliveredToPatient;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "deliveredToPatientAt", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar deliveredToPatientAt;

    @Column(name = "viewed_by_patient", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean viewedByPatient;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "viewed_by_patient_at", nullable = true)
    private Calendar viewedByPatientAt;

    @Column(name = "retest_requested", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean retestRequested;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "retest_request_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar retestRequestDate;

    @Column(name = "retest_requester_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String retestRequester;

    @Column(name = "reportable", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean reportable;

    private Meta meta;

    public TestReport(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public ReportConfiguration getReportConfiguration() {
        return reportConfiguration;
    }

    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    public Calendar getEstimatedToBeAvailableAt() {
        return estimatedToBeAvailableAt;
    }

    public void setEstimatedToBeAvailableAt(Calendar estimatedToBeAvailableAt) {
        this.estimatedToBeAvailableAt = estimatedToBeAvailableAt;
    }

    public Calendar getFirstAvailableAt() {
        return firstAvailableAt;
    }

    public void setFirstAvailableAt(Calendar firstAvailableAt) {
        this.firstAvailableAt = firstAvailableAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isSignedOut() {
        return signedOut;
    }

    public void setSignedOut(boolean signedOut) {
        this.signedOut = signedOut;
    }

    public Signout getSignoutDetails() {
        return signoutDetails;
    }

    public void setSignoutDetails(Signout signoutDetails) {
        this.signoutDetails = signoutDetails;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLaboratoryOrderId() {
        return laboratoryOrderId;
    }

    public void setLaboratoryOrderId(String laboratoryOrderId) {
        this.laboratoryOrderId = laboratoryOrderId;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
    }

    public Calendar getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Calendar completedAt) {
        this.completedAt = completedAt;
    }

    public SignedOutType getSignedOutType() {
        return signedOutType;
    }

    public void setSignedOutType(SignedOutType signedOutType) {
        this.signedOutType = signedOutType;
    }

    public Report getResultData() {
        return resultData;
    }

    public void setResultData(Report resultData) {
        this.resultData = resultData;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public boolean isNoOrder() {
        return noOrder;
    }

    public void setNoOrder(boolean noOrder) {
        this.noOrder = noOrder;
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public boolean isResearchSample() {
        return researchSample;
    }

    public void setResearchSample(boolean researchSample) {
        this.researchSample = researchSample;
    }

    public String getResearchProjectName() {
        return researchProjectName;
    }

    public void setResearchProjectName(String researchProjectName) {
        this.researchProjectName = researchProjectName;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
        if(this.reportType.equals(ReportType.CONTROL)) {
            this.control = true;
            this.researchSample = false;
        }
        if(this.reportType.equals(ReportType.RESEARCH)) {
            this.control = false;
            this.researchSample = true;
        }
    }

    @JsonProperty(value = "testRunId", access = JsonProperty.Access.READ_ONLY)
    public String getTestRunId(){
        if(this.testRun != null)
            return this.testRun.getId();

        else
            return null;
    }



    @JsonProperty(value = "age", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public int getAge(){
        if(this.completedAt != null)
            return ((int)(Calendar.getInstance().getTimeInMillis() - this.completedAt.getTimeInMillis())/3600000);

        else
            return 0;
    }

    public boolean isDeliveredToProvider() {
        return deliveredToProvider;
    }

    public void setDeliveredToProvider(boolean deliveredToProvider) {
        this.deliveredToProvider = deliveredToProvider;
    }

    public boolean isDeliveredToPatient() {
        return deliveredToPatient;
    }

    public void setDeliveredToPatient(boolean deliveredToPatient) {
        this.deliveredToPatient = deliveredToPatient;
    }

    public Calendar getDeliveredToPatientAt() {
        return deliveredToPatientAt;
    }

    public void setDeliveredToPatientAt(Calendar deliveredToPatientAt) {
        this.deliveredToPatientAt = deliveredToPatientAt;
    }

    public Boolean getViewedByPatient() {
        return viewedByPatient;
    }

    public void setViewedByPatient(Boolean viewedByPatient) {
        this.viewedByPatient = viewedByPatient;
    }

    public Calendar getViewedByPatientAt() {
        return viewedByPatientAt;
    }

    public void setViewedByPatientAt(Calendar viewedByPatientAt) {
        this.viewedByPatientAt = viewedByPatientAt;
    }

    public String getBatchRunId() {
        return batchRunId;
    }

    public void setBatchRunId(String batchRunId) {
        this.batchRunId = batchRunId;
    }

    public String getPipelineRunId() {
        return pipelineRunId;
    }

    public void setPipelineRunId(String pipelineRunId) {
        this.pipelineRunId = pipelineRunId;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public boolean isRetestRequested() {
        return retestRequested;
    }

    public void setRetestRequested(boolean retestRequested) {
        this.retestRequested = retestRequested;
    }

    public Calendar getRetestRequestDate() {
        return retestRequestDate;
    }

    public void setRetestRequestDate(Calendar retestRequestDate) {
        this.retestRequestDate = retestRequestDate;
    }

    public String getRetestRequester() {
        return retestRequester;
    }

    public void setRetestRequester(String retestRequester) {
        this.retestRequester = retestRequester;
    }

    public String getSequenceRunId() {
        return sequenceRunId;
    }

    public void setSequenceRunId(String sequenceRunId) {
        this.sequenceRunId = sequenceRunId;
    }

    public boolean isReportable() {
        return reportable;
    }

    public void setReportable(boolean reportable) {
        this.reportable = reportable;
    }

    //sample_number,seq_run_number,pipeline_run_id,sca,fetal_sex,read_counts,x_vec,y_vec,y_vec_2,x_z_scores,gender_confidence,sca_confidence,trisomy_13,z_score_chr13,trisomy_18,z_score_chr18,trisomy_21,z_score_chr21,autosome_aneuploidy
    //1711100234,220211_VH00682_39_AAAKLYKHV,9100bed5-3644-457f-8cae-7d6e281d0c16,XYY,MALE,20350631,0.05818321483129785,0.00021093574125016264,0.0005606632453721901,,0.9999969143399785,0.9999877446344987,euploid,-0.38,euploid,-0.36,euploid,-1.08,eup
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getRawCsvData() {
        String returnString = "";
        //sample_number
        if(this.sampleNumber != null)
            returnString += this.sampleNumber + ",";
        else
            returnString += ",";

        //seq_run_number
        if(this.sequenceRunId != null)
            returnString += this.sequenceRunId + ",";
        else
            returnString += ",";

        //pipeline_run_id
        if(this.pipelineRunId != null)
            returnString += this.pipelineRunId + ",";
        else
            returnString += ",";

        //sca, fetal_sex
        if((this.resultData.getData().getSca() != null && (reportConfiguration.equals(ReportConfiguration.NIPS_PLUS) || reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)))) {
            returnString += this.resultData.getData().getSca().getScaResult() + ",";
            returnString += this.resultData.getData().getSca().getGenderResult() + ",";
        } else if (this.resultData.getData().getFst() != null && reportConfiguration.equals(ReportConfiguration.FST)) {
            returnString += this.resultData.getData().getFst().getScaResult() + ",";
            returnString += this.resultData.getData().getFst().getGenderResult() + ",";
        }
        else
            returnString += ",,";

        //read_counts
        if(this.resultData.getQc().getRawCounts() != null)
            returnString += this.resultData.getQc().getRawCounts() + ",";
        else
            returnString += ",";

        //x_vec,y_vec,y_vec_2,x_z_scores,gender_confidence,sca_confidence
        if(this.resultData.getData().getSca() != null && reportConfiguration.equals(ReportConfiguration.NIPS_PLUS) || reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)) {
            if (this.resultData.getData().getSca().getxVec() != null)
                returnString += this.resultData.getData().getSca().getxVec() + ",";
            else
                returnString += ",";
            if (this.resultData.getData().getSca().getyVec() != null)
                returnString += this.resultData.getData().getSca().getyVec() + ",";
            else
                returnString += ",";
            if (this.resultData.getData().getSca().getyVec2() != null)
                returnString += this.resultData.getData().getSca().getyVec2() + ",";
            else
                returnString += ",";
            if (this.resultData.getData().getSca().getXzScores() != null)
                returnString += this.resultData.getData().getSca().getXzScores() + ",";
            else
                returnString += ",";
            if (this.resultData.getData().getSca().getGenderConfidence() != null)
                returnString += this.resultData.getData().getSca().getGenderConfidence() + ",";
            else
                returnString += ",";
            if (this.resultData.getData().getSca().getScaConfidence() != null)
                returnString += this.resultData.getData().getSca().getScaConfidence() + ",";
            else
                returnString += ",";
        } else if(this.resultData.getData().getFst() != null && reportConfiguration.equals(ReportConfiguration.FST)) {
            if(this.resultData.getData().getFst().getxVec() != null)
                returnString += this.resultData.getData().getFst().getxVec() + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getFst().getyVec() != null)
                returnString += this.resultData.getData().getFst().getyVec() + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getFst().getyVec2() != null)
                returnString += this.resultData.getData().getFst().getyVec2() + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getFst().getXzScores() != null)
                returnString += this.resultData.getData().getFst().getXzScores() + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getFst().getGenderConfidence() != null)
                returnString += this.resultData.getData().getFst().getGenderConfidence() + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getFst().getScaConfidence() != null)
                returnString += this.resultData.getData().getFst().getScaConfidence() + ",";
            else
                returnString += ",";
        } else
            returnString += ",,,,,,";

        //trisomy_13,z_score_chr13,trisomy_18,z_score_chr18,trisomy_21,z_score_chr21
        if(this.resultData.getData().getT13() != null && (
                this.reportConfiguration.equals(ReportConfiguration.NIPS_BASIC)
                || this.reportConfiguration.equals(ReportConfiguration.NIPS_PLUS)
                || this.reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)
                )) {
            if(this.resultData.getData().getT13().getCall() != null && this.resultData.getData().getT13().getCall().booleanValue())
                returnString += "aneuploidy" + ",";
            if(this.resultData.getData().getT13().getCall() != null && !this.resultData.getData().getT13().getCall().booleanValue())
                returnString += "euploid" + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getT13().getzScore() != null)
                returnString += this.resultData.getData().getT13().getzScore() + ",";
            else
                returnString += ",";
        }

        if(this.resultData.getData().getT18() != null && (
                this.reportConfiguration.equals(ReportConfiguration.NIPS_BASIC)
                        || this.reportConfiguration.equals(ReportConfiguration.NIPS_PLUS)
                        || this.reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)
        )) {
            if(this.resultData.getData().getT18().getCall() != null && this.resultData.getData().getT18().getCall().booleanValue())
                returnString += "aneuploidy" + ",";
            if(this.resultData.getData().getT18().getCall() != null && !this.resultData.getData().getT18().getCall().booleanValue())
                returnString += "euploid" + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getT18().getzScore() != null)
                returnString += this.resultData.getData().getT18().getzScore() + ",";
            else
                returnString += ",";
        }

        if(this.resultData.getData().getT21() != null && (
                this.reportConfiguration.equals(ReportConfiguration.NIPS_BASIC)
                        || this.reportConfiguration.equals(ReportConfiguration.NIPS_PLUS)
                        || this.reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)
        )) {
            if(this.resultData.getData().getT21().getCall() != null && this.resultData.getData().getT21().getCall().booleanValue())
                returnString += "aneuploidy" + ",";
            if(this.resultData.getData().getT21().getCall() != null && !this.resultData.getData().getT21().getCall().booleanValue())
                returnString += "euploid" + ",";
            else
                returnString += ",";
            if(this.resultData.getData().getT21().getzScore() != null)
                returnString += this.resultData.getData().getT21().getzScore() + ",";
            else
                returnString += ",";
        }

        //autosome_aneuploidy
        if(this.resultData.getData().getEuploid() != null && this.resultData.getData().getEuploid().isEuploid().booleanValue())
            returnString += "eup";


        return returnString;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //,sample_number,x_vec,y_vec,y_vec_2,x_z_scores,percent_rank_y_vec,sca,fetal_sex
    public String getRawScaData() throws JdxServiceException {
        String returnString = "";
        if(reportConfiguration.equals(ReportConfiguration.FST)) {

            returnString += this.sampleNumber + ",";

            if(this.resultData.getData().getFst().getxVec() != null)
                returnString += this.resultData.getData().getFst().getxVec() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getFst().getyVec() != null)
                returnString += this.resultData.getData().getFst().getyVec() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getFst().getyVec2() != null)
                returnString += this.resultData.getData().getFst().getyVec2() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getFst().getXzScores() != null)
                returnString += this.resultData.getData().getFst().getXzScores() + ",";
            else
                returnString += ",";


            returnString += ","; //Don't have percentRankYVec

            if(this.resultData.getData().getFst().getScaResult() != null)
                returnString += this.resultData.getData().getFst().getScaResult() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getFst().getGenderResult() != null)
                returnString += this.resultData.getData().getFst().getGenderResult();

        } else if (reportConfiguration.equals(ReportConfiguration.NIPS_PLUS)
                || reportConfiguration.equals(ReportConfiguration.NIPS_ADVANCED)){
            returnString += this.sampleNumber + ",";

            if(this.resultData.getData().getSca().getxVec() != null)
                returnString += this.resultData.getData().getSca().getxVec() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getSca().getyVec() != null)
                returnString += this.resultData.getData().getSca().getyVec() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getSca().getyVec2() != null)
                returnString += this.resultData.getData().getSca().getyVec2() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getSca().getXzScores() != null)
                returnString += this.resultData.getData().getSca().getXzScores() + ",";
            else
                returnString += ",";


            returnString += ","; //Don't have percentRankYVec

            if(this.resultData.getData().getSca().getScaResult() != null)
                returnString += this.resultData.getData().getSca().getScaResult() + ",";
            else
                returnString += ",";

            if(this.resultData.getData().getSca().getGenderResult() != null)
                returnString += this.resultData.getData().getSca().getGenderResult();
        }

        return returnString;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + id.hashCode();
        hash = 31 * hash + (patient.getId() == null ? 0 : patient.getId().hashCode());
        hash = 31 * hash + (laboratoryOrderId == null ? 0 : laboratoryOrderId.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;

        TestReport report = (TestReport) obj;
        return this.patient.getId().equals(report.patient.getId())
                && this.id.equals(report.id)
                && this.laboratoryOrderId.equals(report.laboratoryOrderId);
    }
}

