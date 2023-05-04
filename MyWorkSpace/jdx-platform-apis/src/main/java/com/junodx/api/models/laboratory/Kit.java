package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.junodx.api.models.commerce.Order;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.fulfillment.Fulfillment;
import com.junodx.api.models.laboratory.types.KitType;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name="kit")
public class Kit {
    @Id
    @Column(name="id", nullable = false, updatable = false)
    private String id;

    @Column(name="code", nullable = false, updatable = false, unique = true)
    private String code;

    @Column(name="sample_number", nullable = false, updatable = false, unique = true)
    private String sampleNumber;

    @Column(name="psd_sleeve_number", nullable = true, unique = true)
    private String psdSleeveNumber;

    @Column(name="type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KitType type;

    @OneToOne (mappedBy = "kit")
    @JsonIgnore
    private TestRun testRun;

    @Column(name = "test_run_id", nullable = true)
    private String testRunId;

    @Column(name = "lims_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsId;

    @Column(name = "lims_barcode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsBarcode;

    @Column(name = "lims_sample_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String limsSampleId;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "lims_added_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Calendar addedToLimsAt;

    private boolean assigned;

    @Column(name = "activate", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean activated;

    private boolean unusable;

    private Meta meta;

    public Kit(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void makeResearchKit(){
        if(this.id == null)
            this.id = UUID.randomUUID().toString();
        this.id = "RS_" + this.id;

        if(this.code == null)
            this.code = UUID.randomUUID().toString();
        this.code = "RS_" + this.code;

        if(this.sampleNumber == null)
            this.sampleNumber = UUID.randomUUID().toString();
    }

    public void makeControlKit(){
        if(this.id == null)
            this.id = UUID.randomUUID().toString();
        this.id = "CTL_" + this.id;

        if(this.code == null)
            this.code = UUID.randomUUID().toString();
        this.code = "CTL_" + this.code;

        if(this.sampleNumber == null)
            this.sampleNumber = UUID.randomUUID().toString();
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getPsdSleeveNumber() {
        return psdSleeveNumber;
    }

    public void setPsdSleeveNumber(String psdSleeveNumber) {
        this.psdSleeveNumber = psdSleeveNumber;
    }

    public KitType getType() {
        return type;
    }

    public void setType(KitType type) {
        this.type = type;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TestRun getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRun testRun) {
        this.testRun = testRun;
        if(testRun != null)
            this.testRunId = testRun.getId();
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isUnusable() {
        return unusable;
    }

    public void setUnusable(boolean unusable) {
        this.unusable = unusable;
    }

    public String getLimsId() {
        return limsId;
    }

    public void setLimsId(String limsId) {
        this.limsId = limsId;
    }

    public String getLimsBarcode() {
        return limsBarcode;
    }

    public void setLimsBarcode(String limsBarcode) {
        this.limsBarcode = limsBarcode;
    }

    public Calendar getAddedToLimsAt() {
        return addedToLimsAt;
    }

    public void setAddedToLimsAt(Calendar addedToLimsAt) {
        this.addedToLimsAt = addedToLimsAt;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(String testRunId) {
        this.testRunId = testRunId;
    }

    public String getLimsSampleId() {
        return limsSampleId;
    }

    public void setLimsSampleId(String limsSampleId) {
        this.limsSampleId = limsSampleId;
    }
}
