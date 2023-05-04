package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Meta;

import javax.persistence.*;
import java.util.List;

//@Entity
//@Table(name="laboratory_review_statistics")
public class LaboratoryReviewStatistics {
 //   @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratory_id", nullable = false, unique = true, insertable = false, updatable = false)
    private Laboratory laboratory;
    */
    private String laboratoryId;

   // @OneToMany(mappedBy = "laboratoryReviewStatistics", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<LaboratoryReviewGroup> review;

   // @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LaboratoryReviewGroup> getReview() {
        return review;
    }

    public void setReview(List<LaboratoryReviewGroup> reviewGroups) {
        this.review = reviewGroups;
    }

    /*
    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

     */


    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
}
