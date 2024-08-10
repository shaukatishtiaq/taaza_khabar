package dev.shaukat.Taaza_Khabar.api.controller;

import dev.shaukat.Taaza_Khabar.api.dto.user.UserCreationDto;
import dev.shaukat.Taaza_Khabar.api.dto.user.UserResponseDto;
import dev.shaukat.Taaza_Khabar.api.entity.User;
import dev.shaukat.Taaza_Khabar.api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreationDto userPayload) {
        User savedUser = userService.createUser(userPayload);
        UserResponseDto result = new UserResponseDto(savedUser.getEmail(), "A verification email has been sent to " + savedUser.getEmail() + " which is valid upto 15 minutes.");
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{email}/verify")
    public ResponseEntity<UserResponseDto> sendVerificationCodeToUser(@PathVariable("email") String userEmail) {
        if (userService.sendVerificationCodeToUser(userEmail)) {
            UserResponseDto result = new UserResponseDto(userEmail, "Verification email has been sent. The link will only be valid for 15 minutes.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        UserResponseDto result = new UserResponseDto(userEmail, "Verification mail couldn't be sent. Please try again later.");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{email}/verify/{verificationCode}")
    public ResponseEntity<UserResponseDto> verifyUser(@PathVariable("email") String userEmail, @PathVariable("verificationCode") String verificationCode) {
        boolean isVerified = userService.verifyUser(userEmail, verificationCode);

        if (isVerified) {
            UserResponseDto result = new UserResponseDto(userEmail, "Your email has been verified.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        UserResponseDto result = new UserResponseDto(userEmail, "Your email wasn't verified. Click on resend verification email to verify your email.");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("email") String userEmail) {
        boolean deleted = userService.deleteUser(userEmail);

        if (deleted) {
            UserResponseDto result = new UserResponseDto(userEmail, "Your email has been removed.");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            UserResponseDto result = new UserResponseDto(userEmail, "Email coudln't be removed due to some issue. Try again later.");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
