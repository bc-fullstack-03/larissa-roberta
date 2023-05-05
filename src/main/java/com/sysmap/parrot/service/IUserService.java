package com.sysmap.parrot.service;

import com.sysmap.parrot.dto.request.UserRequest;

public interface IUserService {
    String createUser(UserRequest requestDTO);
}
