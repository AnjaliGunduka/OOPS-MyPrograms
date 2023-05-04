package com.junodx.api.repositories.providers;

import com.junodx.api.models.providers.Location;
import com.junodx.api.models.providers.Practice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, String> {

    public Set<Location> findByPractice(Practice practice);
    public List<Location> findLocationsByPractice_Id(String id);
    public Set<Location> deleteLocationsByPractice(Practice practice);
    public Set<Location> deleteLocationsByPractice_Id(String id);
    public Location findLocationByIdAndPractice_Id(String id, String practiceId);
}
