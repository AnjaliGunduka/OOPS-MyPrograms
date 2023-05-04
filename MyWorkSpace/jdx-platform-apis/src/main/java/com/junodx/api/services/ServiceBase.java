package com.junodx.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.core.Meta;
import com.junodx.api.services.auth.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public abstract class ServiceBase {

    private final static Logger log = LoggerFactory.getLogger(ServiceBase.class);

    protected ObjectMapper mapper;

    /**
     * Utility method for creating a new entity metadata object
     * @param user
     * @return
     */
    protected Meta buildMeta(UserDetailsImpl user){
        Meta meta = new Meta();
        meta.setCreatedAt(Calendar.getInstance());
        if(user != null ) {
            meta.setCreatedBy(user.getEmail());
            meta.setCreatedById(user.getUserId());
            meta.setLastModifiedBy(user.getEmail());
            meta.setLastModifiedById(user.getUserId());
        }

        meta.setLastModifiedAt(Calendar.getInstance());


        return meta;
    }

    /**
     * Utility method for updating entity metadata
     * @param meta
     * @param user
     * @return
     */
    protected Meta updateMeta(Meta meta, UserDetailsImpl user){
        if(meta == null) {
            meta = new Meta();
            meta.setCreatedAt(Calendar.getInstance());
            if(user != null ) {
                meta.setCreatedBy(user.getEmail());
                meta.setCreatedById(user.getUserId());
            }
        }

        if(meta.getCreatedAt() == null)
            meta.setCreatedAt(Calendar.getInstance());
        if(meta.getCreatedBy() == null)
            meta.setCreatedBy(user.getEmail());
        if(meta.getCreatedById() == null)
            meta.setCreatedById(user.getUserId());

        meta.setLastModifiedAt(Calendar.getInstance());
        if(user!=null) {
            meta.setLastModifiedBy(user.getEmail());
            meta.setLastModifiedById(user.getUserId());
        }

        return meta;
    }

    /**
     * Common log routine to propogate decorator functions to external or system logging
     * @param code
     * @param message
     * @param user
     */
    protected void log(LogCode code, String message, UserDetailsImpl user){
        log.info(code + "; User: " + user.getUsername() + "(" + user.getUserId() + "); " + message);
    }

    /**
     * Template response template for managing control flow back to calling classes to know if something succeeded for failed
     * @param <T>
     */
    public class ServiceResponse<T> {
        private LogCode code;
        private T responseValue;
        private boolean succeeded;

        public ServiceResponse(){}

        public ServiceResponse(LogCode code, T value, boolean succeeded) {
                this.code = code;
                this.responseValue = value;
                this.succeeded = succeeded;
        }

        public LogCode getCode() {
            return code;
        }

        public void setCode(LogCode code){
            this.code = code;
        }

        public T getResponseValue() {
            return responseValue;
        }

        public void setResponseValue(T responseValue) {
            this.responseValue = responseValue;
        }

        public boolean isSucceeded() {
            return succeeded;
        }

        public void setSucceeded(boolean succeeded) {
            this.succeeded = succeeded;
        }
    }
}
