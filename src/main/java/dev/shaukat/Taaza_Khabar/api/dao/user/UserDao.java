package dev.shaukat.Taaza_Khabar.api.dao.user;

import dev.shaukat.Taaza_Khabar.api.dto.user.UserCreationDto;
import dev.shaukat.Taaza_Khabar.api.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    int createUser(UserCreationDto userPayload);

    Optional<User> getUserByEmail(String userEmail);

    boolean checkUserExists(String userEmail);

    int updateUser(User user);

    List<String> getVerifiedEmails();

    int deleteUser(Long id);
}
