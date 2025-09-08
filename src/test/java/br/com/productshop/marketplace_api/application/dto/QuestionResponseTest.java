package br.com.productshop.marketplace_api.application.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class QuestionResponseTest {

    @Test
    void shouldCreateQuestionResponseWithAllFields() {
        // Arrange
        String id = "q1";
        String question = "Is it available?";
        String answer = "Yes, it is";
        String userId = "user1";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime answeredAt = LocalDateTime.now().plusHours(1);
        Boolean answered = true;

        // Act
        QuestionResponse response = QuestionResponse.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .userId(userId)
                .createdAt(createdAt)
                .answeredAt(answeredAt)
                .answered(answered)
                .build();

        // Assert
        assertEquals(id, response.getId());
        assertEquals(question, response.getQuestion());
        assertEquals(answer, response.getAnswer());
        assertEquals(userId, response.getUserId());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(answeredAt, response.getAnsweredAt());
        assertEquals(answered, response.getAnswered());
    }

    @Test
    void shouldCreateEmptyQuestionResponse() {
        // Act
        QuestionResponse response = new QuestionResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getQuestion());
        assertNull(response.getAnswer());
        assertNull(response.getUserId());
        assertNull(response.getCreatedAt());
        assertNull(response.getAnsweredAt());
        assertNull(response.getAnswered());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        QuestionResponse response1 = QuestionResponse.builder()
                .id("1")
                .question("Test?")
                .answer("Yes")
                .build();

        QuestionResponse response2 = QuestionResponse.builder()
                .id("1")
                .question("Test?")
                .answer("Yes")
                .build();

        QuestionResponse response3 = QuestionResponse.builder()
                .id("2")
                .question("Different?")
                .answer("No")
                .build();

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        QuestionResponse response = QuestionResponse.builder()
                .id("1")
                .question("Test Question?")
                .answer("Test Answer")
                .answered(true)
                .build();

        // Act
        String toString = response.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("question=Test Question?"));
        assertTrue(toString.contains("answer=Test Answer"));
        assertTrue(toString.contains("answered=true"));
    }

    @Test
    void shouldHandleUnansweredQuestion() {
        // Arrange & Act
        QuestionResponse response = QuestionResponse.builder()
                .id("1")
                .question("New Question?")
                .userId("user1")
                .createdAt(LocalDateTime.now())
                .answered(false)
                .build();

        // Assert
        assertNotNull(response);
        assertNull(response.getAnswer());
        assertNull(response.getAnsweredAt());
        assertFalse(response.getAnswered());
    }

    @Test
    void shouldHandleDateTimeFields() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusHours(1);

        // Act
        QuestionResponse response = QuestionResponse.builder()
                .id("1")
                .question("Time Test?")
                .createdAt(now)
                .answeredAt(later)
                .build();

        // Assert
        assertEquals(now, response.getCreatedAt());
        assertEquals(later, response.getAnsweredAt());
        assertTrue(response.getAnsweredAt().isAfter(response.getCreatedAt()));
    }
}
