package com.tochko.advertising_platform.service.impl;

import com.tochko.advertising_platform.model.Role;
import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.model.UserPlatformDetails;
import com.tochko.advertising_platform.model.enums.ERole;
import com.tochko.advertising_platform.model.enums.UserStatus;
import com.tochko.advertising_platform.repository.RoleRepository;
import com.tochko.advertising_platform.repository.UserRepository;
import com.tochko.advertising_platform.service.UserService;
import com.tochko.advertising_platform.util.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No such user %s", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthority(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthority(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName().name())).collect(Collectors.toList());
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void add(User user) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void addPlatformDetails(Long userId, UserPlatformDetails platformDetails) {
        User user = get(userId).get();
        user.setUserPlatformDetails(platformDetails);
        userRepository.save(user);
    }

    public User update(User user) {
        User updatedUser = get(user.getId()).get();
        user.setRoles(updatedUser.getRoles());
        userRepository.save(user);
        return null;
    }

    public void changeUserStatus(Long userId, UserStatus status) {
        userRepository.changeStatus(userId, status);
    }

    public void changeUserRole(Long userId, ERole role) {
        User user = get(userId).get();
        Role newRole = roleRepository.findByName(role).get();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
        if (newRole.equals(userRole)) {
            if (user.getRoles().contains(adminRole)) {
                user.getRoles().remove(adminRole);
            }
        } else {
            if (!user.getRoles().contains(adminRole)) {
                user.getRoles().add(adminRole);
            }
        }
        userRepository.save(user);
    }

    public void changePassword(User user) {
        Optional<User> optUser = get(user.getId());
        optUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(optUser.get());
    }

    public void delete(Long id) {
        Optional<User> deletedUser = get(id);
        deletedUser.ifPresent(userRepository::delete);
    }

    public void uploadFile(long userId, MultipartFile file) throws IOException {
        User updatedUser = get(userId).get();
        Byte[] bytes = UploadFileUtil.getBytesFromMultipartFile(file);
        if (updatedUser != null) {
            if (bytes.length == 0) {
                return;
            } else {
                updatedUser.setImageData(bytes);
            }
            userRepository.save(updatedUser);
        }
    }
}
