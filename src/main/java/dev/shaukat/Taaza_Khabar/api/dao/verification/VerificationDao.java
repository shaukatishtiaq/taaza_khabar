package dev.shaukat.Taaza_Khabar.api.dao.verification;

import dev.shaukat.Taaza_Khabar.api.entity.Verification;

import java.util.Optional;

public interface VerificationDao {
    int createVerificationCode(Long userId, String verificationCode);

    void deleteAllVerificationCodesOfUser(Long userId);

    Optional<Verification> getUserVerification(Long userId);
}
