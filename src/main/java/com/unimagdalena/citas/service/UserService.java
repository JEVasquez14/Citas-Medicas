package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.user.UserRegisterDTO;
import com.unimagdalena.citas.model.User;


public interface UserService {
    User register(UserRegisterDTO dto);
}
