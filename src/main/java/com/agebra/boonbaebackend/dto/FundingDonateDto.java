package com.agebra.boonbaebackend.dto;

import com.agebra.boonbaebackend.domain.PaymentBank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        @NotNull
        private Long card_number;
        @NotNull
        private Long cvc_number;
        @NotNull
        private Long account_number;
        @NotNull
        private PaymentBank payment_bank;
        @NotNull
        private String phone_number;
    }


}
