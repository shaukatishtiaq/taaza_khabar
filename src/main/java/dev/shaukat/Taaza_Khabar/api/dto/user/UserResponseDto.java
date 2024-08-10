package dev.shaukat.Taaza_Khabar.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(
        @JsonProperty("email") String userEmail,
        String message) {
}
