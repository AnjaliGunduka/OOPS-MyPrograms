package com.junodx.api.models.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class Email {
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String domain;
    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extension;


    private String email;

    public Email(String address){
        String v[] = address.split("@");
        if(v.length == 2) {
            System.out.println("email splitting is " + v[0] + " and " + v[1]);
            this.name = v[0];
            String d[] = v[1].split(".");
            System.out.println("email splitting is " + d[0] + " and " + d[1]);
            if (d.length == 2) {

                this.domain = d[0];
                this.extension = d[1];
            }
            this.email = getEmailAddress();
        } else {
            this.name = null;
            this.domain = null;
            this.extension = null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.email = getEmailAddress();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
        this.email = getEmailAddress();
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
        this.email = getEmailAddress();
    }

    public String getEmail(){ return email; }

    @JsonIgnore
    public String getEmailAddress(){
        return name + "@" + domain + "." + extension;
    }

    @JsonIgnore
    public String getMailTo(){
        return "mailto:" + email;
    }

    @Override
    public String toString(){
        return email;
    }
}
