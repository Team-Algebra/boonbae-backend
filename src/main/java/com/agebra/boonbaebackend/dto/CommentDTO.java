package com.agebra.boonbaebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentRequest {
        private String content;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CommentListResponse {
        private Integer count;
        private List<CommentResponse> list;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CommentResponse {
        @JsonProperty("comment_pk")
        private Long commentPk;

        @JsonProperty("username")
        private String username;

        @JsonProperty("content")
        private String content;

        @JsonProperty("create_date")
        private LocalDateTime createAt;

        @JsonProperty("like_cnt")
        private Integer likeCnt;

        @JsonProperty("like")
        private Boolean like;
    }
}
