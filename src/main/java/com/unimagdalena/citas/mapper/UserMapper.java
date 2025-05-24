package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.user.UserLoginDTOResponse;
import com.unimagdalena.citas.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserLoginDTOResponse toUserLoginDtoResponse(User user, String token);

}
