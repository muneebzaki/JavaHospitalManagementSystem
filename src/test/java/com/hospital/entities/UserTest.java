package com.hospital.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john_doe", "password123", "DOCTOR", 1);
    }

    @Test
    void testUserCreation() {
        assertEquals("john_doe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("DOCTOR", user.getRole());
        assertEquals(1, user.getReferenceId());
        assertTrue(user.isActive());
    }

    @Test
    void testSetAndGetMethods() {
        user.setUsername("jane_doe");
        user.setPassword("newpassword123");
        user.setRole("NURSE");
        user.setReferenceId(2);
        user.setActive(false);

        assertEquals("jane_doe", user.getUsername());
        assertEquals("newpassword123", user.getPassword());
        assertEquals("NURSE", user.getRole());
        assertEquals(2, user.getReferenceId());
        assertFalse(user.isActive());
    }

    @Test
    void testRoleValidation() {
        // Test valid roles
        assertDoesNotThrow(() -> user.setRole("ADMIN"));
        assertDoesNotThrow(() -> user.setRole("DOCTOR"));
        assertDoesNotThrow(() -> user.setRole("NURSE"));
        assertDoesNotThrow(() -> user.setRole("PATIENT"));

        // Test invalid role
        assertThrows(IllegalArgumentException.class, () -> user.setRole("INVALID_ROLE"));
    }

    @Test
    void testPasswordValidation() {
        // Test empty password
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));

        // Test null password
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));

        // Test valid password
        assertDoesNotThrow(() -> user.setPassword("validPassword123"));
    }

    @Test
    void testUsernameValidation() {
        // Test empty username
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(""));

        // Test null username
        assertThrows(IllegalArgumentException.class, () -> user.setUsername(null));

        // Test valid username
        assertDoesNotThrow(() -> user.setUsername("validUsername"));
    }
} 