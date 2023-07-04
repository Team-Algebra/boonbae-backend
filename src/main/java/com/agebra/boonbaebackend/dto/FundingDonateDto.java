package com.agebra.boonbaebackend.dto;

import com.agebra.boonbaebackend.domain.PaymentBank;
import com.agebra.boonbaebackend.domain.QnAType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FundingDonateDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Request_Card{
        @NotNull
        private Long cardNumber;
        @NotNull
        private Long CVCNumber;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Request_Account{
        @NotNull
        private Long accountNumber;
        @NotNull
        private PaymentBank paymentBank;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Request_PhoneNumber{
        @NotNull
        private Long phoneNumber;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request_All{
        private Long cardNumber;
        private Long CVCNumber;
        private Long accountNumber;
        private PaymentBank paymentBank;
        private Long phoneNumber;
    }


}
