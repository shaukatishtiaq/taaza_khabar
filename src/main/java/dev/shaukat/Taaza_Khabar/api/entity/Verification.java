package dev.shaukat.Taaza_Khabar.api.entity;

import java.util.Date;

public record Verification(
        Long verificationId,
        Date validUpto,
        String verificationCode,
        Long userId
) {
}
