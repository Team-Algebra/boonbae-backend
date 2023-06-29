package com.agebra.boonbaebackend.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationResponse {
    private String token;

    private UserInfo user;

    public AuthenticationResponse(String token, String id, String username) {
        this.token = token;
        this.user = new UserInfo(id, username);
    }

    @Data
    @AllArgsConstructor
    private class UserInfo {
        private String id;
        private String username;
    }
}
