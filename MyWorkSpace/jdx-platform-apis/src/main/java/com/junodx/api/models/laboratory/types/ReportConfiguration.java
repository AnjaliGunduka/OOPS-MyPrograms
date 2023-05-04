package com.junodx.api.models.laboratory.types;

public enum ReportConfiguration {
    NIPS_BASIC,
    NIPS_PLUS,
    NIPS_ADVANCED,
    FST,
    UCS;

    public static ReportConfiguration getType(String type){
        return valueOf(type);
    }
}
