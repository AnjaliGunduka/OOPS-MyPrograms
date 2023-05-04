package com.junodx.api.repositories;

import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.providers.Provider;
import com.junodx.api.services.auth.UserDetailsImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmailAndClientId(String email, String clientId);


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Page<User> findAllByUserTypeIs(UserType type, Pageable pageable);
    Page<User> findUsersByPatientDetails_ProvidersAndUserTypeIs(Provider p, UserType type, Pageable pageable);
    Page<User> findUsersByLastName(String lastName, Pageable pageable);
    Page<User> findUsersByPrimaryAddress_City(String city, Pageable pageable);
    Page<User> findUsersByPrimaryAddress_State(String city, Pageable pageable);
    Page<User> findUsersByPrimaryAddress_PostalCode(String city, Pageable pageable);

    //@Query("select u from User u where u.customer.lastName = :lastName and o.customer.dateOfBirth = :dob")
    //boolean doesUserExistByEmailAndClientId(String email, String clientId);
    //
    @Query("select u from User u where " +
            "(:firstName is null or u.firstName = :firstName)" +
            " and (:lastName is null or u.lastName = :lastName)" +
            " and (:email is null or u.email = :email)" +
            " and (:xifinId is null or u.xifinPatientId = :xifinId)" +
            " and (:stripeId is null or u.stripeCustomerId = :stripeId)" +
            " and (:city is null or u.primaryAddress.city = :city)" +
            " and (:state is null or u.primaryAddress.state = :state)" +
            " and (:postalCode is null or u.primaryAddress.postalCode = :postalCode)" +
            " and (:email is null or u.email = :email)" +
            " and (:status is null or u.status = :status)" +
            //" and (:practiceId is null or u.withInsurance = :withInsurance)" +
            //" and (:providerId is null or u.customer.activated = :customerActivated)" +
            " and (:type is null or u.userType = :type)" +
            //  " and (:containsProductId is null or r.line = :labOrderId)" +
            //  " and (:containsTests is null or r.isAvailable = :isAvailable)" +
            " order by u.lastName DESC")
    Page<User> search(@Param("lastName") String lastName,
                      @Param("firstName") String firstName,
                      @Param("state") String state,
                      @Param("city") String city,
                      @Param("postalCode") String postalCode,
                      @Param("email") String email,
                     // @Param("practiceId") String practiceId,
                      //@Param("providerId") String providerId,
                      @Param("xifinId") String xifinId,
                      @Param("stripeId") String stripeId,
                      @Param("type") UserType type,
                      @Param("status") UserStatus status,
                      Pageable pageable);


    boolean existsUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsUserByEmailAndClientId(String email, String clientId);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);




	
}
