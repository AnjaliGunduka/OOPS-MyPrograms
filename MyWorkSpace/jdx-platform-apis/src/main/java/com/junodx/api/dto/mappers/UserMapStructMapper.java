package com.junodx.api.dto.mappers;

import com.junodx.api.controllers.payloads.OAuth2TokenDTO;
import com.junodx.api.dto.models.auth.UserBatchDto;
import com.junodx.api.dto.models.auth.UserCreateDto;
import com.junodx.api.dto.models.auth.UserOrderDto;
import com.junodx.api.dto.models.auth.UserSignoutDto;
import com.junodx.api.models.auth.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel="spring")
public interface UserMapStructMapper {
    //User
    //UserOrderDto userToUserOrderDto(User user);
    UserSignoutDto userToUserSignoutDto(User user);

    User userCreateDtoToUser(UserCreateDto createUser);
    UserBatchDto userToUserBatchDto(User user);

    List<UserBatchDto> userToUserBatchDtos(List<User> users);

    User userOrderDtoToUser(UserOrderDto userDto);

    default public UserOrderDto userToUserOrderDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserOrderDto userOrderDto = new UserOrderDto();

        userOrderDto.setId( user.getId() );
        userOrderDto.setFirstName( user.getFirstName() );
        userOrderDto.setLastName( user.getLastName() );
        userOrderDto.setEmail( user.getEmail() );
        userOrderDto.setDateOfBirth( user.getDateOfBirth() );
        userOrderDto.setUserType( user.getUserType() );
        userOrderDto.setStripeCustomerId( user.getStripeCustomerId() );
        userOrderDto.setShippingAddress( user.getPrimaryAddress() );

        return userOrderDto;
    }

}
