package com.junodx.api.repositories.auth;

import com.junodx.api.models.data.UserEvent;
import com.junodx.api.models.data.types.UserEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    Page<UserEvent> findAllByUserId(String id, Pageable pageable);
    List<UserEvent> findAllByUserIdAndAndMacroReportable(String id, boolean macro);
    boolean existsUserEventByTypeAndUserId(UserEventType type, String userId);

    @Query("select e from UserEvent e where e.userId = :userId" +
            " and (:type is null or e.type = :type)" +
            " and (:macroReportable is null or e.macroReportable = :macroReportable)" +
            " and (:ipAddress is null or e.ip4Address = :ipAddress)" +
            " and (cast(:before as timestamp) is null or e.createdAt <= :before)" +
            " and (cast(:after as timestamp) is null or e.createdAt >= :after)" +
            " order by e.createdAt DESC")
    Page<UserEvent> search(@Param("userId") String userId,
                           @Param("type") UserEventType type,
                           @Param("macroReportable") Boolean macroReportable,
                           @Param("ipAddress") String ipAddress,
                           @Param("before") Calendar before,
                           @Param("after") Calendar after,
                           Pageable pageable);
}
