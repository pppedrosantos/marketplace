package br.com.productshop.marketplace_api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String id;
    private String question;
    private String answer;
    private String productId;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
    private Boolean answered;
}
