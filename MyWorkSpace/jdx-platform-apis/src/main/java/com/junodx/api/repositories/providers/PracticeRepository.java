package com.junodx.api.repositories.providers;

import com.junodx.api.models.providers.Practice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PracticeRepository extends JpaRepository<Practice, String> {
    Optional<Practice> findById(String id);
    Optional<Practice> findPracticeByDefaultPracticeIsTrue();
    Optional<Practice> findPracticeByLimsId(String id);
    Optional<Practice> findPracticeByXifinId(String id);
    Optional<Practice> findPracticeBySalesforceId(String id);

}
