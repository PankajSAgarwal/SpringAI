package com.example.boardgamebuddy;

import jakarta.validation.constraints.NotBlank;

public record Question(@NotBlank(message = "Game Title is required") String gameTitle, @NotBlank(message = "Question is required") String question) {
}
