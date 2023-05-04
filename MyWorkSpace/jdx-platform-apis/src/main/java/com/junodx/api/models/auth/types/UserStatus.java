package com.junodx.api.models.auth.types;

public enum UserStatus {
    PROVISIONAL,
    NEW,
    ACTIVATED,
    ARCHIVED;

    public static UserStatus getEnum(final String status) {
        UserStatus userStatus = null;
        for (UserStatus st: values()) {
            if (st.name().equals(status)) {
                userStatus = st;
            }
        }
        return userStatus;
    }
}
