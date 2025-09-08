package br.com.productshop.marketplace_api.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void shouldCreateQuestionWithAllFields() {
        // Arrange
        String id = "q1";
        String question = "Is it available?";
        String answer = "Yes, it is";
        String userId = "user1";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime answeredAt = LocalDateTime.now().plusHours(1);
        Boolean answered = true;

        // Act
        Question questionObj = Question.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .userId(userId)
                .createdAt(createdAt)
                .answeredAt(answeredAt)
                .answered(answered)
                .build();

        // Assert
        assertEquals(id, questionObj.getId());
        assertEquals(question, questionObj.getQuestion());
        assertEquals(answer, questionObj.getAnswer());
        assertEquals(userId, questionObj.getUserId());
        assertEquals(createdAt, questionObj.getCreatedAt());
        assertEquals(answeredAt, questionObj.getAnsweredAt());
        assertEquals(answered, questionObj.getAnswered());
    }

    @Test
    void shouldCreateEmptyQuestion() {
        // Act
        Question question = new Question();

        // Assert
        assertNotNull(question);
        assertNull(question.getId());
        assertNull(question.getQuestion());
        assertNull(question.getAnswer());
        assertNull(question.getUserId());
        assertNull(question.getCreatedAt());
        assertNull(question.getAnsweredAt());
        assertNull(question.getAnswered());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        Question question1 = Question.builder().id("1").question("Test?").build();
        Question question2 = Question.builder().id("1").question("Test?").build();
        Question question3 = Question.builder().id("2").question("Another?").build();

        // Assert
        assertEquals(question1, question2);
        assertNotEquals(question1, question3);
        assertEquals(question1.hashCode(), question2.hashCode());
        assertNotEquals(question1.hashCode(), question3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // Arrange
        Question question = Question.builder()
                .id("1")
                .question("Test Question?")
                .answer("Test Answer")
                .build();

        // Act
        String toString = question.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("question=Test Question?"));
        assertTrue(toString.contains("answer=Test Answer"));
    }
}
