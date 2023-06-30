package com.agebra.boonbaebackend.dto;

import lombok.*;

public class UserDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        private String id;
        private String username; //domain에서는 nickname임
        private String password;
        private String refferer_id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class RegisterResponse {
        private String id;
        private String username;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String id;
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {
        private String token;
        private UserInfo user;

        public LoginResponse(String token, String id, String username) {
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

}
