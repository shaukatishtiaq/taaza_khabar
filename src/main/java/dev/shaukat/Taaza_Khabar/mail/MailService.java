package dev.shaukat.Taaza_Khabar.mail;

import java.util.List;
import java.util.Map;

public interface MailService {
    void sendMails(List<String> users, Map<String, Object> pdfPaths);

    void sendVerificationEmailToUser(String userEmailsExpression, String verificationCode);
}
