package com.junodx.api.models.configuration.lims;

import javax.persistence.*;

@Entity
@Table(name = "lims_data_sources")
public class ElementsDataSources {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "lims_configuration_id")
    private LIMSConfigurationEntity configuration;

    private String dataSourceId;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
