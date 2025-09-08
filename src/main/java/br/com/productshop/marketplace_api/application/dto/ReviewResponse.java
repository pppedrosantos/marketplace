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
public class ReviewResponse {
    private String id;
    private String userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
    private Boolean verifiedPurchase;
    private Integer likes;
}
