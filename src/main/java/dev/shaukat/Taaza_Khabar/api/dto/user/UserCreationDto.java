package dev.shaukat.Taaza_Khabar.api.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreationDto(
        @JsonProperty(value = "email")
        @JsonAlias(value = {"userEmail"})
        @NotBlank(message = "Email can't be empty")
        @Email(message = "Invalid email format", regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
        String userEmail) {
}
