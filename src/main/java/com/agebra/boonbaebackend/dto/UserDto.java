package com.agebra.boonbaebackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UserDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        @NotNull
        private String id;
        @NotNull
        private String username; //domain에서는 nickname임
        @NotNull
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

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Info {
        private String username;
        private String id;
        private int eco_point;
        private String user_img;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        @NotNull
        private String id;
        @NotNull
        private String password;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class modifyPassword {
        @NotNull
        private String nowPassword;
        @NotNull
        private String changePassword;

    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginResponse {
        private String token;
        private UserInfo user;

        public LoginResponse(String token, String id, String username, String role) {
            this.token = token;
            this.user = new UserInfo(id, username, role);
        }

        @Data
        @AllArgsConstructor
        private class UserInfo {
            private String id;
            private String username;
            private String role;
        }

        public String getId() {
            return this.user.id;
        }
    }

}
