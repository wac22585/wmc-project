package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.UserRequest;
import at.spengergasse.backend.dto.UserResponse;

import java.util.List;

public interface UserService {

    void saveUser(UserRequest userRequest);

    UserResponse getUser();

    List<UserResponse> getAllUser();
}

