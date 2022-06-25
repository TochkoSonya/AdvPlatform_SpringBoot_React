package com.tochko.advertising_platform.service;

import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.model.UserPlatformDetails;
import com.tochko.advertising_platform.model.enums.ERole;
import com.tochko.advertising_platform.model.enums.UserStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> get(Long id);

    List<User> getAll();

    void add(User user);

    void addPlatformDetails(Long userId, UserPlatformDetails platformDetails);

    User update(User user);

    void changeUserStatus(Long userId, UserStatus status);

    void changeUserRole(Long userId, ERole roleName);

    void changePassword(User user);

    void delete(Long id);

    void uploadFile(long userId, MultipartFile file) throws IOException;
}
