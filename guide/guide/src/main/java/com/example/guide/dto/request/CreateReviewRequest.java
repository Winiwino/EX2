package com.example.guide.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank(message = "Имя автора обязательно")
        String author,
        @NotBlank(message = "Текст отзыва обязателен")
        @Size(max = 1000, message = "Текст отзыва не должен превышать 1000 символов")
        String text
) {
}
