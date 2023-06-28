package com.agebra.boonbaebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Register {
        private String id;
        private String username;
        private String password;
        private String refferer_id;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String id;
        private String password;
    }
}
