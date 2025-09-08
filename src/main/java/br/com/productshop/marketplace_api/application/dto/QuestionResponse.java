package br.com.productshop.marketplace_api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private String id;
    private String question;
    private String answer;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
    private Boolean answered;
}
