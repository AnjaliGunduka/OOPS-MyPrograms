package com.junodx.api.repositories.auth;

import com.junodx.api.models.auth.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
}
