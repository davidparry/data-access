package ai.qodo.dao;

import ai.qodo.pojo.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private UserProfileDao userProfileDao;

    @BeforeEach
    void setUp() {
        userProfileDao = new UserProfileDao(jdbcTemplate);
    }

    @Test
    void testCreateUserProfile_ShouldExecuteInsertQuery() {
        // NOTE: This test validates the current implementation which has SQL injection vulnerability
        // This should be fixed in a separate security remediation task
        String username = "john_doe";
        String email = "john@example.com";

        userProfileDao.createUserProfile(username, email);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(sqlCaptor.capture());
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("INSERT INTO user_profile"));
        assertTrue(capturedSql.contains("john_doe"));
        assertTrue(capturedSql.contains("john@example.com"));
    }

    @Test
    void testUpdateUserProfile_ShouldExecuteUpdateQuery() {
        // NOTE: This test validates the current implementation which has SQL injection vulnerability
        // This should be fixed in a separate security remediation task
        int id = 1;
        String username = "jane_smith";
        String email = "jane@example.com";

        userProfileDao.updateUserProfile(id, username, email);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(sqlCaptor.capture());
        
        String capturedSql = sqlCaptor.getValue();
        assertTrue(capturedSql.contains("UPDATE user_profile"));
        assertTrue(capturedSql.contains("SET"));
        assertTrue(capturedSql.contains("WHERE id = " + id));
    }

    @Test
    void testReadUserProfiles_ShouldReturnListOfUserProfiles() {
        UserProfile user1 = new UserProfile(1, "john_doe", "john@example.com");
        UserProfile user2 = new UserProfile(2, "jane_smith", "jane@example.com");
        List<UserProfile> expectedProfiles = Arrays.asList(user1, user2);

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedProfiles);

        List<UserProfile> result = userProfileDao.readUserProfiles();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        assertEquals("jane_smith", result.get(1).getUsername());
        
        verify(jdbcTemplate).query(eq("SELECT * FROM user_profile"), any(RowMapper.class));
    }

    @Test
    void testReadUserProfiles_ShouldReturnEmptyListWhenNoProfiles() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Arrays.asList());

        List<UserProfile> result = userProfileDao.readUserProfiles();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
