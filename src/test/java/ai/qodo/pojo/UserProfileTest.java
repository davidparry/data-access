package ai.qodo.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testDefaultConstructor() {
        UserProfile userProfile = new UserProfile();
        assertNotNull(userProfile);
    }

    @Test
    void testParameterizedConstructor() {
        UserProfile userProfile = new UserProfile(1, "john_doe", "john.doe@example.com");

        assertEquals(1, userProfile.getId());
        assertEquals("john_doe", userProfile.getUsername());
        assertEquals("john.doe@example.com", userProfile.getEmail());
    }

    @Test
    void testGettersAndSetters() {
        UserProfile userProfile = new UserProfile();

        userProfile.setId(2);
        userProfile.setUsername("jane_doe");
        userProfile.setEmail("jane.doe@example.com");

        assertEquals(2, userProfile.getId());
        assertEquals("jane_doe", userProfile.getUsername());
        assertEquals("jane.doe@example.com", userProfile.getEmail());
    }

    @Test
    void testToString() {
        UserProfile userProfile = new UserProfile(1, "john_doe", "john.doe@example.com");

        String result = userProfile.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("username='john_doe'"));
        assertTrue(result.contains("email='john.doe@example.com'"));
    }

    @Test
    void testSettersWithNullValues() {
        UserProfile userProfile = new UserProfile();
        
        userProfile.setUsername(null);
        userProfile.setEmail(null);

        assertNull(userProfile.getUsername());
        assertNull(userProfile.getEmail());
    }

    @Test
    void testSettersWithBoundaryValues() {
        UserProfile userProfile = new UserProfile();
        
        userProfile.setId(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, userProfile.getId());
    }

    @Test
    void testSettersWithEmptyStrings() {
        UserProfile userProfile = new UserProfile();
        
        userProfile.setUsername("");
        userProfile.setEmail("");

        assertEquals("", userProfile.getUsername());
        assertEquals("", userProfile.getEmail());
    }
}
