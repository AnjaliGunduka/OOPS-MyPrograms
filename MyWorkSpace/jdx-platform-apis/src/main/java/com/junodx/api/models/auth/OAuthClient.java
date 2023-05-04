package com.junodx.api.models.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class OAuthClient {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    private String clientSecret;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_user_id", nullable = true)
    private User accountUser;

    private String scope;
    private String grantTypes;
    private long accessTokenValidityDuration;
    private long refreshTokenValidityDuration;
    private boolean usesAWSCognito;
    private String cognitoUserPoolId;
    private String awsRegion;

    public OAuthClient(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public long getAccessTokenValidityDuration() {
        return accessTokenValidityDuration;
    }

    public void setAccessTokenValidityDuration(long accessTokenValidityDuration) {
        this.accessTokenValidityDuration = accessTokenValidityDuration;
    }

    public long getRefreshTokenValidityDuration() {
        return refreshTokenValidityDuration;
    }

    public void setRefreshTokenValidityDuration(long refreshTokenValidityDuration) {
        this.refreshTokenValidityDuration = refreshTokenValidityDuration;
    }

    public boolean isUsesAWSCognito() {
        return usesAWSCognito;
    }

    public void setUsesAWSCognito(boolean usesAWSCognito) {
        this.usesAWSCognito = usesAWSCognito;
    }

    public String getCognitoUserPoolId() {
        return cognitoUserPoolId;
    }

    public void setCognitoUserPoolId(String cognitoUserPoolId) {
        this.cognitoUserPoolId = cognitoUserPoolId;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public User getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(User accountUser) {
        this.accountUser = accountUser;
    }
}
