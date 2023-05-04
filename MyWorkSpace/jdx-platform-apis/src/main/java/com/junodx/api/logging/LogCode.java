package com.junodx.api.logging;

public enum LogCode {
    SUCCESS("Success", 200),
    RESOURCE_GET("Got a single resource", 1000),
    RESOURCE_GET_ERROR("Got a single resource", 1010),
    RESOURCE_GET_MULTIPLE("Got multiple resources", 1100),
    RESOURCE_GET_MULTIPLE_ERROR("Error when retrieving multiple resources", 1110),
    RESOURCE_CREATE("Create single resource", 2000),
    RESOURCE_CREATE_ERROR("Error when creating a single resource", 2010),
    RESOURCE_MULTIPLE_CREATE("Create multiple resources", 2100),
    RESOURCE_MULTIPLE_CREATE_ERROR("Error when creating multiple resources", 2110),
    RESOURCE_UPDATE("Update single resource", 3000),
    RESOURCE_UPDATE_ERROR("Error when updating a single resource", 3010),
    RESOURCE_MULTIPLE_UPDATES("Update multiple resources", 3100),
    RESOURCE_MULTIPLE_UPDATES_ERROR("Error when updating multiple resources", 3110),
    RESOURCE_DELETE("Delete single resource", 4000),
    RESOURCE_DELETE_ERROR("Error when deleting a single resource", 4010),
    RESOURCE_MULTIPLE_DELETE("Delete multiple resources", 4100),
    RESOURCE_MULTIPLE_DELETE_ERROR("Error when deleting multiple resources", 4110);


    public final String label;
    public final int code;

    private LogCode(String label, int code){
        this.label = label;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Code: " + this.code + " ; " + this.label;
    }
}
