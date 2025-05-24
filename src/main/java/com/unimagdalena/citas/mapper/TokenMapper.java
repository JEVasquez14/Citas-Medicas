package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.token.TokenDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenDTOResponse toDTO(String token, String role);

}
