package com.junodx.api.models.configuration;

import javax.persistence.*;

@Entity
@Table(name = "webhook_conf")
public class WebhookServerConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String webhookToken;
    private String webhookHashingAlgorithm;
    private String webhookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public String getWebhookHashingAlgorithm() {
        return webhookHashingAlgorithm;
    }

    public void setWebhookHashingAlgorithm(String webhookHashingAlgorithm) {
        this.webhookHashingAlgorithm = webhookHashingAlgorithm;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }
}
