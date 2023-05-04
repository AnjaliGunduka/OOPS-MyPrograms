package com.junodx.api.models.laboratory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.core.Meta;
import com.junodx.api.models.core.types.IntervalType;

import javax.persistence.*;

@Entity
@Table(name="laboratory_statistics")
public class LaboratoryStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laboratory_id", nullable = false, unique = true)
    private Laboratory laboratory;

    private int interval;
    private IntervalType intervalType;
    private int runsToBeApproved;
    private int resultsAwaitingSignOutConfirmation;
    private int resultsAwaitingInvestigation;
    private int recentlySignedOut;
    private int recentlyManuallySignedOut;
    private int recentlyAutomaticallySignedOut;
    private int upcomingResults;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
    }

    public int getRunsToBeApproved() {
        return runsToBeApproved;
    }

    public void setRunsToBeApproved(int runsToBeApproved) {
        this.runsToBeApproved = runsToBeApproved;
    }

    public int getResultsAwaitingSignOutConfirmation() {
        return resultsAwaitingSignOutConfirmation;
    }

    public void setResultsAwaitingSignOutConfirmation(int resultsAwaitingSignOutConfirmation) {
        this.resultsAwaitingSignOutConfirmation = resultsAwaitingSignOutConfirmation;
    }

    public int getResultsAwaitingInvestigation() {
        return resultsAwaitingInvestigation;
    }

    public void setResultsAwaitingInvestigation(int resultsAwaitingInvestigation) {
        this.resultsAwaitingInvestigation = resultsAwaitingInvestigation;
    }

    public int getRecentlySignedOut() {
        return recentlySignedOut;
    }

    public void setRecentlySignedOut(int recentlySignedOut) {
        this.recentlySignedOut = recentlySignedOut;
    }

    public int getRecentlyManuallySignedOut() {
        return recentlyManuallySignedOut;
    }

    public void setRecentlyManuallySignedOut(int recentlyManuallySignedOut) {
        this.recentlyManuallySignedOut = recentlyManuallySignedOut;
    }

    public int getRecentlyAutomaticallySignedOut() {
        return recentlyAutomaticallySignedOut;
    }

    public void setRecentlyAutomaticallySignedOut(int recentlyAutomaticallySignedOut) {
        this.recentlyAutomaticallySignedOut = recentlyAutomaticallySignedOut;
    }

    public int getUpcomingResults() {
        return upcomingResults;
    }

    public void setUpcomingResults(int upcomingResults) {
        this.upcomingResults = upcomingResults;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
