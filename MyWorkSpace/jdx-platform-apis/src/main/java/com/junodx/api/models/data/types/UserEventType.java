package com.junodx.api.models.data.types;

public enum UserEventType {
    REGISTERED,
    ACTIVATED,
    ORDERED,
    RESULTS_DELIVERED,
    REFUND_REQUESTED,
    REFUNDED,
    REDRAW_REQUESTED,
    REDRAW_FULFILLED,
    RESULT_VIEWED,
    LOGGED_IN,
    LOGGED_OUT,
    EMAIL_OPEN,
    CLICKED,
    VIDEO_WATCHED,
    ARTICLE_READ,
    SUPPORT_REQUESTED,
    VIDEO_CALL_SCHEDULED,
    CALL_MADE,
    VIDEO_CALL_MADE,
    CALL_COMPLETED,
    KIT_ACTIVATED,
    SAMPLE_OBTAINED,
    SAMPLE_COLLECTION_VIDEO_WATCHED,
    SAMPLE_COLLECTION_GIFS_WATCHED,
    INSURANCE_ADDED;

    public boolean isMacro() {
        if(this.equals(REGISTERED) || this.equals(ACTIVATED) || this.equals(ORDERED) || this.equals(RESULTS_DELIVERED) || this.equals(REFUNDED) || this.equals(REDRAW_REQUESTED))
            return true;

        return false;
    }

    public boolean isAtomic(){
        if(this.equals(REGISTERED) || this.equals(ACTIVATED))
            return true;

        return false;
    }
}
