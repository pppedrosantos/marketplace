package br.com.productshop.marketplace_api.application.mapper;

import br.com.productshop.marketplace_api.application.dto.ProductResponse;
import br.com.productshop.marketplace_api.application.dto.QuestionResponse;
import br.com.productshop.marketplace_api.domain.entity.Product;
import br.com.productshop.marketplace_api.domain.entity.Question;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
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
                .rating(product.getRating())
                .questions(product.getQuestions() != null ?
                    product.getQuestions().stream()
                        .map(this::toQuestionResponse)
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
}
