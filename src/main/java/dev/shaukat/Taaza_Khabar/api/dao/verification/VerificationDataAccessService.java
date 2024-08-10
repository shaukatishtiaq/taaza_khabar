package dev.shaukat.Taaza_Khabar.api.dao.verification;

import dev.shaukat.Taaza_Khabar.api.entity.Verification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
public class VerificationDataAccessService implements VerificationDao {
    private final JdbcTemplate jdbcTemplate;

    public VerificationDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createVerificationCode(Long id, String verificationCode) {
        String sql = """
                INSERT INTO verification(verification_code, user_id, valid_upto)
                VALUES(?,?,?)
                """;
        return jdbcTemplate.update(sql, verificationCode, id, new Date(System.currentTimeMillis() + Duration.ofMinutes(15).toMillis()));
    }

    @Override
    public void deleteAllVerificationCodesOfUser(Long userId) {
        String sql = """
                DELETE FROM verification
                WHERE user_id = ?;
                """;
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public Optional<Verification> getUserVerification(Long userId) {
        String sql = """
                SELECT * FROM verification
                WHERE user_id = ?;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Verification(rs.getLong("id"),
                        rs.getTimestamp("valid_upto"),
                        rs.getString("verification_code"),
                        rs.getLong("user_id")), userId)
                .stream()
                .findFirst();
    }
}
