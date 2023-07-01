package com.agebra.boonbaebackend.dto;

import com.agebra.boonbaebackend.domain.QnAType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QnADto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        private String title;
        private QnAType qnaType;
        private String description;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response_oneQnA{
        private QnAType qnaType;
        private String status;
        private int isReply;
        private String replyText;
        private String title;
        private String userName;
        private LocalDateTime create_date;
        private String description;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response_AllQnA{
        private Long qnaPk;
        private QnAType qnaType;
        private String status;
        private String title;
        private String userName;
        private LocalDateTime create_date;
    }
}
