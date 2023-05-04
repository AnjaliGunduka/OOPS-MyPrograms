package com.junodx.api.repositories.providers;

import com.junodx.api.models.providers.Provider;
import com.junodx.api.models.providers.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SpecialityRepository extends JpaRepository<Specialty, Long> {
    List<Specialty> findAllByProvider(Provider p);
    List<Specialty> deleteSpecialtiesByProvider(Provider p);
}
