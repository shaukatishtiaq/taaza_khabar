package dev.shaukat.Taaza_Khabar.api.service.user;

import dev.shaukat.Taaza_Khabar.api.dto.user.UserCreationDto;
import dev.shaukat.Taaza_Khabar.api.entity.User;

import java.util.List;

public interface UserService {


    User createUser(UserCreationDto userPayload);

    User getUserByEmail(String userEmail);

    boolean sendVerificationCodeToUser(String userEmail);

    int updateUser(User user);

    boolean verifyUser(String userEmail, String verificationCode);

    List<String> getVerifiedEmails();

    boolean deleteUser(String userEmail);
}
