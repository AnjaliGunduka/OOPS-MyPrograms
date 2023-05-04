package com.junodx.api.models.laboratory.tests;

public class EuploidTest extends Test {
    private static final String Name = "Euploid";

    private Boolean euploid;

    public EuploidTest(){
        this.name = Name;
    }
    public EuploidTest(Boolean val){
        this.euploid = val;
        this.name = Name;
    }

    public Boolean isEuploid() {
        return euploid;
    }

    public void setEuploid(Boolean euploid) {
        this.euploid = euploid;
    }
}
