package com.junodx.api.models.laboratory.tests;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Test {

    @JsonIgnore
    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

}
