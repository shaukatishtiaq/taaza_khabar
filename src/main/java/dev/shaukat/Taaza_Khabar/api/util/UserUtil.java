package dev.shaukat.Taaza_Khabar.api.util;

import java.util.Random;

public class UserUtil {
    public static String generateVerificationCode() {
        Random random = new Random();

        return String.valueOf(random.nextInt(100000, 999999));
    }
}
