package ai.qodo.dao;

import ai.qodo.DataAccessApplication;
import ai.qodo.pojo.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DataAccessApplication.class, TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class UserProfileDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserProfileDao userProfileDao;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        jdbcTemplate.execute("DELETE FROM user_profile");
    }

    @Test
    void testCreateUserProfile_WithValidData_Success() {
        // Arrange
        String username = "john_doe";
        String email = "john.doe@example.com";

        // Act
        userProfileDao.createUserProfile(username, email);

        // Assert
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        assertEquals(1, profiles.size());
        UserProfile profile = profiles.get(0);
        assertEquals(username, profile.getUsername());
        assertEquals(email, profile.getEmail());
    }

    @Test
    void testCreateUserProfile_WithNullEmail_Success() {
        // Arrange
        String username = "jane_smith";
        String email = null;

        // Act
        userProfileDao.createUserProfile(username, email);

        // Assert
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        assertEquals(1, profiles.size());
        UserProfile profile = profiles.get(0);
        assertEquals(username, profile.getUsername());
        // Note: SQL injection vulnerability causes null to be inserted as string "null"
        assertEquals("null", profile.getEmail());
    }

    @Test
    void testCreateUserProfile_SQLInjectionAttempt_VulnerabilityExists() {
        // WARNING: This test demonstrates a SQL injection vulnerability in UserProfileDao.createUserProfile()
        // The method uses string concatenation instead of parameterized queries
        // Example: "INSERT INTO user_profile (username, email) VALUES ('" + username + "', '" + email + "')"
        
        // Arrange - Malicious username with SQL injection attempt
        String maliciousUsername = "admin'; DROP TABLE user_profile; --";
        String email = "hacker@example.com";

        // Act & Assert - This will likely cause an exception due to SQL syntax error
        // In a properly secured system, this should be prevented by parameterized queries
        assertThrows(Exception.class, () -> {
            userProfileDao.createUserProfile(maliciousUsername, email);
        });
    }

    @Test
    void testUpdateUserProfile_WithValidData_Success() {
        // Arrange - Create initial user profile
        userProfileDao.createUserProfile("alice", "alice@example.com");
        List<UserProfile> initialProfiles = userProfileDao.readUserProfiles();
        int profileId = initialProfiles.get(0).getId();

        // Act - Update the user profile
        String newUsername = "alice_updated";
        String newEmail = "alice.new@example.com";
        userProfileDao.updateUserProfile(profileId, newUsername, newEmail);

        // Assert
        List<UserProfile> updatedProfiles = userProfileDao.readUserProfiles();
        assertEquals(1, updatedProfiles.size());
        UserProfile updatedProfile = updatedProfiles.get(0);
        assertEquals(newUsername, updatedProfile.getUsername());
        assertEquals(newEmail, updatedProfile.getEmail());
    }

    @Test
    void testUpdateUserProfile_SQLInjectionAttempt_VulnerabilityExists() {
        // WARNING: This test demonstrates a SQL injection vulnerability in UserProfileDao.updateUserProfile()
        // The method uses string concatenation: "UPDATE user_profile SET username = '" + username + "', email = '" + email + "' WHERE id = " + id
        
        // Arrange - Create a user profile first
        userProfileDao.createUserProfile("bob", "bob@example.com");
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        int profileId = profiles.get(0).getId();

        // Act & Assert - Malicious update attempt
        String maliciousUsername = "hacker'; DROP TABLE user_profile; --";
        String email = "hacker@example.com";
        
        assertThrows(Exception.class, () -> {
            userProfileDao.updateUserProfile(profileId, maliciousUsername, email);
        });
    }

    @Test
    void testUpdateUserProfile_NonExistentId_NoEffect() {
        // Arrange
        userProfileDao.createUserProfile("charlie", "charlie@example.com");

        // Act - Try to update non-existent user profile
        userProfileDao.updateUserProfile(99999, "fake_user", "fake@example.com");

        // Assert - Original profile should remain unchanged
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        assertEquals(1, profiles.size());
        assertEquals("charlie", profiles.get(0).getUsername());
    }

    @Test
    void testReadUserProfiles_EmptyDatabase_ReturnsEmptyList() {
        // Act
        List<UserProfile> profiles = userProfileDao.readUserProfiles();

        // Assert
        assertNotNull(profiles);
        assertTrue(profiles.isEmpty());
    }

    @Test
    void testReadUserProfiles_MultipleRecords_ReturnsAllRecords() {
        // Arrange
        userProfileDao.createUserProfile("user1", "user1@example.com");
        userProfileDao.createUserProfile("user2", "user2@example.com");
        userProfileDao.createUserProfile("user3", "user3@example.com");

        // Act
        List<UserProfile> profiles = userProfileDao.readUserProfiles();

        // Assert
        assertEquals(3, profiles.size());
    }

    @Test
    void testUserProfileRowMapper_CorrectMapping_AllFieldsSet() {
        // Arrange
        String username = "mapper_test";
        String email = "mapper@example.com";
        userProfileDao.createUserProfile(username, email);

        // Act
        List<UserProfile> profiles = userProfileDao.readUserProfiles();

        // Assert
        assertEquals(1, profiles.size());
        UserProfile profile = profiles.get(0);
        assertNotNull(profile.getId());
        assertTrue(profile.getId() > 0);
        assertEquals(username, profile.getUsername());
        assertEquals(email, profile.getEmail());
    }

    @Test
    void testCreateUserProfile_WithEmptyStrings_Success() {
        // Arrange - Testing edge case with empty strings
        String username = "";
        String email = "";

        // Act
        userProfileDao.createUserProfile(username, email);

        // Assert
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        assertEquals(1, profiles.size());
        assertEquals("", profiles.get(0).getUsername());
        assertEquals("", profiles.get(0).getEmail());
    }

    @Test
    void testCreateUserProfile_WithSpecialCharacters_Success() {
        // Arrange - Testing with special characters (non-malicious)
        String username = "user@#$%";
        String email = "special+chars@example.com";

        // Act
        userProfileDao.createUserProfile(username, email);

        // Assert
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        assertEquals(1, profiles.size());
        assertEquals(username, profiles.get(0).getUsername());
        assertEquals(email, profiles.get(0).getEmail());
    }
}
