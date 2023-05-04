package com.junodx.api.dto.mappers;

import com.junodx.api.dto.models.auth.UserBatchDto;
import com.junodx.api.dto.models.auth.UserCreateDto;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.auth.UserSignoutDto;
import com.junodx.api.models.auth.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-06T11:57:40+0530",
    comments = "version: 1.4.1.Final, compiler: Eclipse JDT (IDE) 1.4.50.v20210914-1429, environment: Java 15.0.2 (Oracle Corporation)"
)
@Component
public class UserMapStructMapperImpl implements UserMapStructMapper {

    private final DatatypeFactory datatypeFactory;

    public UserMapStructMapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public UserSignoutDto userToUserSignoutDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserSignoutDto userSignoutDto = new UserSignoutDto();

        userSignoutDto.setId( user.getId() );
        userSignoutDto.setFirstName( user.getFirstName() );
        userSignoutDto.setLastName( user.getLastName() );
        userSignoutDto.setEmail( user.getEmail() );
        userSignoutDto.setPrimaryPhone( user.getPrimaryPhone() );

        return userSignoutDto;
    }

    @Override
    public User userCreateDtoToUser(UserCreateDto createUser) {
        if ( createUser == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( createUser.getFirstName() );
        user.setLastName( createUser.getLastName() );
        user.setEmail( createUser.getEmail() );
        user.setDateOfBirth( xmlGregorianCalendarToString( calendarToXmlGregorianCalendar( createUser.getDateOfBirth() ), null ) );
        user.setUserType( createUser.getUserType() );

        return user;
    }

    @Override
    public UserBatchDto userToUserBatchDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserBatchDto userBatchDto = new UserBatchDto();

        userBatchDto.setId( user.getId() );
        userBatchDto.setFirstName( user.getFirstName() );
        userBatchDto.setLastName( user.getLastName() );
        userBatchDto.setEmail( user.getEmail() );
        userBatchDto.setUsername( user.getUsername() );
        userBatchDto.setUserType( user.getUserType() );
        userBatchDto.setActivated( user.isActivated() );
        userBatchDto.setPrimaryAddress( user.getPrimaryAddress() );
        userBatchDto.setAge( user.getAge() );
        userBatchDto.setLastOrderedAt( user.getLastOrderedAt() );
        userBatchDto.setStatus( user.getStatus() );

        return userBatchDto;
    }

    @Override
    public List<UserBatchDto> userToUserBatchDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserBatchDto> list = new ArrayList<UserBatchDto>( users.size() );
        for ( User user : users ) {
            list.add( userToUserBatchDto( user ) );
        }

        return list;
    }

    @Override
    public User userOrderDtoToUser(UserOrderDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setFirstName( userDto.getFirstName() );
        user.setLastName( userDto.getLastName() );
        user.setEmail( userDto.getEmail() );
        user.setDateOfBirth( userDto.getDateOfBirth() );
        user.setUserType( userDto.getUserType() );
        user.setStripeCustomerId( userDto.getStripeCustomerId() );

        return user;
    }

    private String xmlGregorianCalendarToString( XMLGregorianCalendar xcal, String dateFormat ) {
        if ( xcal == null ) {
            return null;
        }

        if (dateFormat == null ) {
            return xcal.toString();
        }
        else {
            Date d = xcal.toGregorianCalendar().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
            return sdf.format( d );
        }
    }

    private XMLGregorianCalendar calendarToXmlGregorianCalendar( Calendar cal ) {
        if ( cal == null ) {
            return null;
        }

        GregorianCalendar gcal = new GregorianCalendar( cal.getTimeZone() );
        gcal.setTimeInMillis( cal.getTimeInMillis() );
        return datatypeFactory.newXMLGregorianCalendar( gcal );
    }
}
