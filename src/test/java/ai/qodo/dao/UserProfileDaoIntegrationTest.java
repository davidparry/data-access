package ai.qodo.dao;

import ai.qodo.pojo.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserProfileDaoIntegrationTest {

    @Autowired
    private UserProfileDao userProfileDao;

    @Autowired
    private TableCreator tableCreator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        try {
            tableCreator.init();
        } catch (Exception e) {
            // Tables might already exist
        }
        // Clean up any existing data
        jdbcTemplate.execute("DELETE FROM user_profile");
    }

    @Test
    void testCreateAndReadUserProfile() {
        userProfileDao.createUserProfile("john_doe", "john@example.com");

        List<UserProfile> profiles = userProfileDao.readUserProfiles();

        assertFalse(profiles.isEmpty());
        UserProfile profile = profiles.get(0);
        assertEquals("john_doe", profile.getUsername());
        assertEquals("john@example.com", profile.getEmail());
    }

    @Test
    void testUpdateUserProfile() {
        userProfileDao.createUserProfile("jane_smith", "jane@example.com");
        List<UserProfile> profiles = userProfileDao.readUserProfiles();
        int profileId = profiles.get(0).getId();

        userProfileDao.updateUserProfile(profileId, "jane_updated", "jane_new@example.com");

        profiles = userProfileDao.readUserProfiles();
        UserProfile updatedProfile = profiles.stream()
            .filter(p -> p.getId() == profileId)
            .findFirst()
            .orElse(null);

        assertNotNull(updatedProfile);
        assertEquals("jane_updated", updatedProfile.getUsername());
        assertEquals("jane_new@example.com", updatedProfile.getEmail());
    }

    @Test
    void testReadUserProfiles_MultipleProfiles() {
        userProfileDao.createUserProfile("user1", "user1@example.com");
        userProfileDao.createUserProfile("user2", "user2@example.com");
        userProfileDao.createUserProfile("user3", "user3@example.com");

        List<UserProfile> profiles = userProfileDao.readUserProfiles();

        assertEquals(3, profiles.size());
    }
}
