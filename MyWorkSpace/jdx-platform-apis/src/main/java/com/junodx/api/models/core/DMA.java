package com.junodx.api.models.core;

public class DMA {
    private String code;

    //private String city;

    public DMA(){
    }

    public DMA(String s){
        this.code = s;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static boolean isValidDMA(String dmaCode){
        if(dmaCode.length() == 3 && dmaCode.matches("[0-9]+"))
            return true;

        return false;
    }
}
