package com.junodx.api.repositories.providers;

import com.junodx.api.models.providers.MedicalLicense;
import com.junodx.api.models.providers.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MedicalLicenseRepository extends JpaRepository<MedicalLicense, Long> {
    List<MedicalLicense> findAllByProvider(Provider p);
    List<MedicalLicense> deleteMedicalLicenseByProvider(Provider p);
}
