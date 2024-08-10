package dev.shaukat.Taaza_Khabar.api.dao.user;

import dev.shaukat.Taaza_Khabar.api.dto.user.UserCreationDto;
import dev.shaukat.Taaza_Khabar.api.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDataAccessService implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDataAccessService(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createUser(UserCreationDto userPayload) {
        String sql = """
                INSERT INTO users(email, created_at)
                VALUES (?,?);
                """;
        return jdbcTemplate.update(sql, userPayload.userEmail(), new Date());
    }

    @Override
    public Optional<User> getUserByEmail(String userEmail) {
        String sql = """
                SELECT * FROM users
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(rs.getLong("id"),
                                rs.getString("email"),
                                rs.getDate("created_at"),
                                rs.getBoolean("is_verified")),
                        userEmail)
                .stream()
                .findFirst();
    }

    @Override
    public boolean checkUserExists(String userEmail) {
        String sql = """
                SELECT id FROM users
                WHERE email = ?;
                """;
        long count = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("id"), userEmail)
                .size();
        return count >= 1;
    }

    @Override
    public int updateUser(User user) {
        String sql = """
                UPDATE users
                SET is_verified = ?
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, user.isVerified(), user.getId());
    }

    @Override
    public List<String> getVerifiedEmails() {
        String sql = """
                SELECT email FROM users
                WHERE is_verified = true;
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("email"));

    }

    @Override
    public int deleteUser(Long userId) {
        String sql = """
                DELETE FROM users
                WHERE id = ?;
                """;
        return jdbcTemplate.update(sql, userId);
    }
}
