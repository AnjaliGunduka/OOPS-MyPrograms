package com.junodx.api.services.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.models.data.UserEvent;
import com.junodx.api.models.data.types.UserEventType;
import com.junodx.api.repositories.auth.UserEventRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserActivitiesService extends ServiceBase {
    @Autowired
    private UserEventRepository userEventRepository;

    private ObjectMapper mapper;

    public UserActivitiesService(){
        this.mapper = new ObjectMapper();
    }

    @Async
    public UserEvent save(UserEvent event) throws JdxServiceException {
        if(event == null)
            throw new JdxServiceException("Cannot save user event since the event payload is missing");

        try {
            if(!event.getType().isMacro())
                event.setMacroReportable(false);
            else
                event.setMacroReportable(true);

            if(event.getType().isAtomic())
                if(userEventRepository.existsUserEventByTypeAndUserId(event.getType(), event.getUserId()))
                    throw new JdxServiceException("Cannot add user event as it is duplicative to an existing atomic event");

            event.setCreatedAt(Calendar.getInstance());

            return userEventRepository.save(event);
        } catch (Exception e) {
            throw new JdxServiceException("Unable to store user event in DB: " + e.getMessage());
        }
    }

    public Page<UserEvent> getEventsForUser(String userId, Pageable pageable) throws JdxServiceException {
        if(userId == null)
            throw new JdxServiceException("Cannot get event records for user id " + userId);

        try {
            return userEventRepository.findAllByUserId(userId, pageable);
        } catch (Exception e) {
            throw new JdxServiceException("Cannot find event records for user due to: " + e.getMessage());
        }
    }

    public List<UserEvent> getMacroEventsForUser(String userId) throws JdxServiceException {
        if(userId == null)
            throw new JdxServiceException("Cannot get event records for user id " + userId);

        try {
            return userEventRepository.findAllByUserIdAndAndMacroReportable(userId, true);
        } catch (Exception e) {
            throw new JdxServiceException("Cannot find event records for user due to: " + e.getMessage());
        }
    }

    public Page<UserEvent> search(String userId,
                                  Optional<UserEventType> type,
                                  Optional<Boolean> macroReportable,
                                  Optional<String> ipAddress,
                                  Optional<Calendar> before,
                                  Optional<Calendar> after,
                                  Pageable pageable) throws JdxServiceException {

        UserEventType oType = null;
        Boolean oMacroReportable = null;
        String oIpAddress = null;
        Calendar oBefore = null;
        Calendar oAfter = null;

        if(type.isPresent()) oType = type.get();
        if(macroReportable.isPresent()) oMacroReportable = macroReportable.get();
        if(ipAddress.isPresent()) oIpAddress = ipAddress.get();
        if(before.isPresent()) oBefore = before.get();
        if(after.isPresent()) oAfter = after.get();

        try {
            return userEventRepository.search(userId, oType, oMacroReportable, oIpAddress, oBefore, oAfter, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot find event records for user due to: " + e.getMessage());
        }
    }
}
