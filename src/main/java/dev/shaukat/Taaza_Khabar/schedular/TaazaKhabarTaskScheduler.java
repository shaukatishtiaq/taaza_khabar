package dev.shaukat.Taaza_Khabar.schedular;

import dev.shaukat.Taaza_Khabar.api.service.user.UserService;
import dev.shaukat.Taaza_Khabar.greaterkashmir.service.GKService;
import dev.shaukat.Taaza_Khabar.mail.MailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
public class TaazaKhabarTaskScheduler {
    private static final Logger LOGGER = Logger.getLogger(TaazaKhabarTaskScheduler.class.getName());
    private final GKService gkService;
    private final MailService mailService;
    private final UserService userService;

    public Instant start, end;

    public TaazaKhabarTaskScheduler(GKService gkService, MailService mailService, UserService userService) {
        this.gkService = gkService;
        this.mailService = mailService;
        this.userService = userService;
    }

    public void setStart() {
        this.start = Instant.now();
    }

    public void setEnd() {
        this.end = Instant.now();
    }

    @Scheduled(cron = "0 4 1 * * *")
    public void runTask() {

        LOGGER.info("Task Scheduler - STARTED");

        Map<String, Object> pdfPaths = gkService.generatePDFsAsync();

        setStart();
        CompletableFuture<Void> sendEmailsToUsers = CompletableFuture.runAsync(() -> schedulerSendMails(pdfPaths))
                .thenAccept((e) -> {
                    LOGGER.info("Scheduler - Emails sent.");
                    setEnd();
                    System.out.println("Time taken = " + Duration.between(end, start));
                })
                .exceptionally((e) -> {
                    LOGGER.log(Level.SEVERE, "Error while sending emails using Completable Future", e);
                    throw new RuntimeException("Error while sending emails using Completable Future");
                });

        LOGGER.info("Task Scheduler - ENDED");
    }

    private void schedulerSendMails(Map<String, Object> pdfPaths) {
        LOGGER.info("Scheduler - Sending emails to user.");
        List<String> userEmails = userService.getVerifiedEmails();

        mailService.sendMails(userEmails, pdfPaths);
    }


}
