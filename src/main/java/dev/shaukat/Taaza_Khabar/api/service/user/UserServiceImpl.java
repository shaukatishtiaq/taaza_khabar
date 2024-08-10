package dev.shaukat.Taaza_Khabar.api.service.user;

import dev.shaukat.Taaza_Khabar.api.dao.user.UserDao;
import dev.shaukat.Taaza_Khabar.api.dao.verification.VerificationDao;
import dev.shaukat.Taaza_Khabar.api.dto.user.UserCreationDto;
import dev.shaukat.Taaza_Khabar.api.entity.User;
import dev.shaukat.Taaza_Khabar.api.entity.Verification;
import dev.shaukat.Taaza_Khabar.api.util.UserUtil;
import dev.shaukat.Taaza_Khabar.exception.GeneralException;
import dev.shaukat.Taaza_Khabar.mail.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserDao userDao;
    private final VerificationDao verificationDao;
    private final MailService mailService;

    public UserServiceImpl(UserDao userDao, VerificationDao verificationDao, MailService mailService) {
        this.userDao = userDao;
        this.verificationDao = verificationDao;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public User createUser(UserCreationDto userPayload) {
//        Check if user already exists.
        if (userDao.checkUserExists(userPayload.userEmail())) {
            throw new GeneralException("User by email = " + userPayload.userEmail() + " already exists.", HttpStatus.BAD_REQUEST);
        }
//        Add user to db
        int affectedRows = userDao.createUser(userPayload);

//        Get and return saved user from db.
        User savedUser = null;

        if (affectedRows == 1) {
            savedUser = getUserByEmail(userPayload.userEmail());
            sendVerificationCodeToUser(userPayload.userEmail());
        } else {
            throw new GeneralException("User couldn't be created", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return savedUser;
    }

    @Override
    public User getUserByEmail(String userEmail) {
        Optional<User> user = userDao.getUserByEmail(userEmail);
        if (user.isPresent()) {
            return user.get();
        }
        throw new GeneralException("User not found.", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public boolean sendVerificationCodeToUser(String userEmail) {
//        Get the user by email and if not found throw exception.
        User user = getUserByEmail(userEmail);
        if (user.isVerified()) {
            throw new GeneralException("User is already verified.", HttpStatus.BAD_REQUEST);
        }

//        Delete all the previous verification codes for the user.
        verificationDao.deleteAllVerificationCodesOfUser(user.getId());

//        Generate a new verification code.
        String verificationCode = UserUtil.generateVerificationCode();
        int rowsAffected = verificationDao.createVerificationCode(user.getId(), verificationCode);
        if (rowsAffected >= 1) {
            mailService.sendVerificationEmailToUser(user.getEmail(), verificationCode);
            return true;
        }
        return false;

    }

    @Override
    @Transactional
    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    @Transactional
    public boolean verifyUser(String userEmail, String verificationCode) {
        User user = getUserByEmail(userEmail);

        if (user.isVerified()) {
            throw new GeneralException("User is already verified.", HttpStatus.BAD_REQUEST);
        }

        Optional<Verification> verificationOptional = verificationDao.getUserVerification(user.getId());

        if (verificationOptional.isEmpty()) {
            return false;
        }

        Verification userVerificationObject = verificationOptional.get();

        if (userVerificationObject.verificationCode().equals(verificationCode) && new Date().before(userVerificationObject.validUpto())) {
            user.setVerified(true);
            int rowsAffected = updateUser(user);

            return rowsAffected >= 1;
        }
        return false;
    }

    @Override
    public List<String> getVerifiedEmails() {
        return userDao.getVerifiedEmails();
    }

    @Override
    @Transactional
    public boolean deleteUser(String userEmail) {
        User user = getUserByEmail(userEmail);

        if (user != null) {
            verificationDao.deleteAllVerificationCodesOfUser(user.getId());
            int affectedRows = userDao.deleteUser(user.getId());

            return affectedRows >= 1;
        }
        return false;
    }
}
