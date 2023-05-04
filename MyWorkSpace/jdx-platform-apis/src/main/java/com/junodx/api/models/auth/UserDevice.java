package com.junodx.api.models.auth;

import com.junodx.api.models.core.Meta;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "user_device")
public class UserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "device_id", unique = true)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String deviceInfo;

    private String browserInfo;

    private Calendar lastAccessFrom;

    private Meta meta;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public Calendar getLastAccessFrom() {
        return lastAccessFrom;
    }

    public void setLastAccessFrom(Calendar lastAccessFrom) {
        this.lastAccessFrom = lastAccessFrom;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
