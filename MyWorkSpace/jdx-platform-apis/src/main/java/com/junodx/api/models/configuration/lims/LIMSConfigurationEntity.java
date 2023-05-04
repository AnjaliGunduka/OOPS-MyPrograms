package com.junodx.api.models.configuration.lims;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junodx.api.models.configuration.TokenApiClientConfiguration;
import com.junodx.api.models.configuration.WebhookServerConfiguration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lims_configuration")
public class LIMSConfigurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "limsConfiguration", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportTypes> reportTypes;

    private String hostCode;
    private String distributorId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "token_api_conf_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TokenApiClientConfiguration apiClientConfiguration;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "webhook_conf_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private WebhookServerConfiguration webhookServerConfiguration;

    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ElementsDataSources> dataSources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ReportTypes> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<ReportTypes> reportTypes) {
        this.reportTypes = reportTypes;
        for(ReportTypes t : this.reportTypes)
            t.setLimsConfiguration(this);
    }

    public void addReportType(ReportTypes reportType) {
        if(this.reportTypes == null)
            this.reportTypes = new ArrayList<>();

        reportType.setLimsConfiguration(this);

        if(!this.reportTypes.stream().filter(x->x.getLimsId().equals(reportType.getLimsId())).findAny().isPresent())
            this.reportTypes.add(reportType);
    }

    public String getHostCode() {
        return hostCode;
    }

    public void setHostCode(String hostCode) {
        this.hostCode = hostCode;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public TokenApiClientConfiguration getApiClientConfiguration() {
        return apiClientConfiguration;
    }

    public void setApiClientConfiguration(TokenApiClientConfiguration apiClientConfiguration) {
        this.apiClientConfiguration = apiClientConfiguration;
    }

    public WebhookServerConfiguration getWebhookServerConfiguration() {
        return webhookServerConfiguration;
    }

    public void setWebhookServerConfiguration(WebhookServerConfiguration webhookServerConfiguration) {
        this.webhookServerConfiguration = webhookServerConfiguration;
    }

    public List<ElementsDataSources> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<ElementsDataSources> dataSources) {
        this.dataSources = dataSources;
    }
}
