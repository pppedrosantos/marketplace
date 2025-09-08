package br.com.productshop.marketplace_api.application.mapper;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.dto.QuestionResponse;
import br.com.productshop.marketplace_api.application.dto.ReviewResponse;
import br.com.productshop.marketplace_api.domain.entity.Product;
import br.com.productshop.marketplace_api.domain.entity.Question;
import br.com.productshop.marketplace_api.domain.entity.Review;
import br.com.productshop.marketplace_api.domain.service.ReviewCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ReviewCalculator reviewCalculator;

    public ProductResponse toResponse(Product product) {
        double averageRating = reviewCalculator.calculateAverageRating(product.getReviews());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .category(product.getCategory())
                .stock(product.getStock())
                .images(product.getImages())
                .specifications(product.getSpecifications())
                .sellerId(product.getSellerId())
                .rating(averageRating)
                .totalReviews(product.getReviews() != null ? product.getReviews().size() : 0)
                .questions(product.getQuestions() != null ?
                    product.getQuestions().stream()
                        .map(this::toQuestionResponse)
                        .collect(Collectors.toList()) :
                    null)
                .reviews(product.getReviews() != null ?
                    product.getReviews().stream()
                        .map(this::toReviewResponse)
                        .collect(Collectors.toList()) :
                    null)
                .build();
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .answer(question.getAnswer())
                .userId(question.getUserId())
                .createdAt(question.getCreatedAt())
                .answeredAt(question.getAnsweredAt())
                .answered(question.getAnswered())
                .build();
    }

    private ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .reviewDate(review.getReviewDate())
                .verifiedPurchase(review.getVerifiedPurchase())
                .likes(review.getLikes())
                .build();
    }
}
