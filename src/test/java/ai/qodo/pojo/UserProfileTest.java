package ai.qodo.pojo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testDefaultConstructor() {
        UserProfile userProfile = new UserProfile();
        assertNotNull(userProfile);
        assertEquals(0, userProfile.getId());
        assertNull(userProfile.getUsername());
        assertNull(userProfile.getEmail());
    }

    @Test
    void testParameterizedConstructor() {
        UserProfile userProfile = new UserProfile(1, "john_doe", "john@example.com");
        
        assertEquals(1, userProfile.getId());
        assertEquals("john_doe", userProfile.getUsername());
        assertEquals("john@example.com", userProfile.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        UserProfile userProfile = new UserProfile();
        
        userProfile.setId(2);
        userProfile.setUsername("jane_smith");
        userProfile.setEmail("jane@example.com");
        
        assertEquals(2, userProfile.getId());
        assertEquals("jane_smith", userProfile.getUsername());
        assertEquals("jane@example.com", userProfile.getEmail());
    }

    @Test
    void testToString() {
        UserProfile userProfile = new UserProfile(3, "bob_jones", "bob@example.com");
        
        String result = userProfile.toString();
        
        assertTrue(result.contains("id=3"));
        assertTrue(result.contains("username='bob_jones'"));
        assertTrue(result.contains("email='bob@example.com'"));
    }

    @Test
    void testSetUsernameWithNull() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(null);
        assertNull(userProfile.getUsername());
    }

    @Test
    void testSetEmailWithNull() {
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(null);
        assertNull(userProfile.getEmail());
    }
}
