package ai.qodo.dao;

import ai.qodo.pojo.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserProfileDao.
 * 
 * SECURITY WARNING: UserProfileDao.createUserProfile() and UserProfileDao.updateUserProfile() 
 * use string concatenation which creates SQL injection vulnerabilities. These tests cover the 
 * current implementation but the code should be refactored to use prepared statements.
 */
@ExtendWith(MockitoExtension.class)
class UserProfileDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserProfileDao userProfileDao;

    @Test
    void testCreateUserProfile() {
        // Arrange
        String username = "john_doe";
        String email = "john.doe@example.com";

        // Act
        userProfileDao.createUserProfile(username, email);

        // Assert
        // NOTE: SQL injection vulnerability - string concatenation used
        String expectedSql = "INSERT INTO user_profile (username, email) VALUES ('john_doe', 'john.doe@example.com')";
        verify(jdbcTemplate).execute(eq(expectedSql));
    }

    @Test
    void testUpdateUserProfile() {
        // Arrange
        int id = 1;
        String username = "jane_doe";
        String email = "jane.doe@example.com";

        // Act
        userProfileDao.updateUserProfile(id, username, email);

        // Assert
        // NOTE: SQL injection vulnerability - string concatenation used
        String expectedSql = "UPDATE user_profile SET username = 'jane_doe', email = 'jane.doe@example.com' WHERE id = 1";
        verify(jdbcTemplate).execute(eq(expectedSql));
    }

    @Test
    void testReadUserProfiles() {
        // Arrange
        UserProfile user1 = new UserProfile(1, "john_doe", "john.doe@example.com");
        UserProfile user2 = new UserProfile(2, "jane_doe", "jane.doe@example.com");
        List<UserProfile> expectedProfiles = Arrays.asList(user1, user2);
        
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedProfiles);

        // Act
        List<UserProfile> result = userProfileDao.readUserProfiles();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedProfiles, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM user_profile"), any(RowMapper.class));
    }

    @Test
    void testReadUserProfilesEmpty() {
        // Arrange
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        // Act
        List<UserProfile> result = userProfileDao.readUserProfiles();

        // Assert
        assertTrue(result.isEmpty());
        verify(jdbcTemplate).query(eq("SELECT * FROM user_profile"), any(RowMapper.class));
    }
}
