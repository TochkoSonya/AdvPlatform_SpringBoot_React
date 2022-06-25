package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.Role;
import com.tochko.advertising_platform.model.User;
import com.tochko.advertising_platform.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u.roles FROM User u where u.username = ?1")
    Set<Role> findRolesByUsername(String username);

    @Modifying
    @Query("UPDATE User user SET user.status=?2 WHERE user.id=?1")
    void changeStatus(Long voidId, UserStatus status);
}
