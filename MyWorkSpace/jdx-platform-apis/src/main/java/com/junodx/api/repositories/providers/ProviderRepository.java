package com.junodx.api.repositories.providers;

import com.junodx.api.models.providers.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, String> {
    List<Provider> findByPractice_Id(String practiceId);
    Page<Provider> findAllByFirstNameIsLikeAndLastNameIsLikeAndContactAddress_CityAndContactAddress_StateAndContactAddress_PostalCode(String firstName, String lastName, String city, String state, String postalCode, Pageable pageable);
    Optional<Provider> findProviderByDefaultProviderIsTrue();
    Optional<Provider> findProviderByNpi(String npi);
    Optional<Provider> findProviderByUpin(String upin);
    Optional<Provider> findProviderByEmail(String email);
    Optional<Provider> findProviderByDefaultProviderIsTrueAndPractice_Id(String practiceId);

    List<Provider> findProvidersByIdIn(List<String> ids);


    List<Provider> deleteProvidersByPractice_Id(String id);
    //public Set<Provider> deleteProvidersById(List<String> ids);
    Provider findProviderByIdAndPractice_Id(String id, String practiceId);
}
