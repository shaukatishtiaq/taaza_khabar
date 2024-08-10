package dev.shaukat.Taaza_Khabar.mail;

import dev.shaukat.Taaza_Khabar.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class.getName());
    private final JavaMailSender mailSender;

    @Value("${app.host.url}")
    private String hostUrl;
    @Value("${spring.mail.username}")
    private String myEmail;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMails(List<String> users, Map<String, Object> pdfPaths) {

        StringBuilder userEmails = new StringBuilder();

        for (int i = 0; i < users.size(); i++) {
            userEmails.append(users.get(i));

            if (i == users.size() - 1) {
                continue;
            }
            userEmails.append(",");
        }

        sendMailToUsers(userEmails.toString(), pdfPaths);
    }

    @Override
    public void sendVerificationEmailToUser(String userEmail, String verificationCode) {
        String verificationLink = hostUrl + "users/" + userEmail + "/verification/" + verificationCode;
        String emailBody = " Click on the link to verify your account. " + verificationLink;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom(myEmail);
            helper.setTo(userEmail);
            helper.setSubject("Email verification.");
            helper.setText(emailBody);

            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            LOGGER.log(Level.WARNING, "Email not send due to internal error", e);
            throw new GeneralException("Email not send due to internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void sendMailToUsers(String userEmailsExpression, Map<String, Object> pdfPaths) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(InternetAddress.parse(userEmailsExpression));
            helper.setSubject("Taaza Khabar!");
            helper.setText("آج کی تازہ ترین خبریں");

            for (String key : pdfPaths.keySet()) {

                File pdfFile = Paths.get(pdfPaths.get(key).toString()).toFile();

                helper.addAttachment(key, pdfFile);
            }

            mailSender.send(mimeMessage);

        } catch (MessagingException | MailException e) {
            LOGGER.log(Level.SEVERE, "Error while creating MIMEMESSAGEHELPER.", e);
        }
    }

}
