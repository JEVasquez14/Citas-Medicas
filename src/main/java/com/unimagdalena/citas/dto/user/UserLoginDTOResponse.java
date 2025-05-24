package com.unimagdalena.citas.dto.user;

public record UserLoginDTOResponse(String user, String password, Long role_id, String token) {

}
