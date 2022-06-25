package com.tochko.advertising_platform.model.response;

import com.tochko.advertising_platform.model.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private UserStatus status;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email,
                       UserStatus status, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.roles = roles;
    }
}