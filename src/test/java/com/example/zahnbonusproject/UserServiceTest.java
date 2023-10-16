package com.example.zahnbonusproject;

import com.example.zahnbonusproject.Entity.User;
import com.example.zahnbonusproject.Repository.UserRepository;
import com.example.zahnbonusproject.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateAge() {
        // Given
        User mockUser = new User();
        Date dob = new Date(100, 0, 1);  // January 1, 2000
        mockUser.setDob(dob);
        mockUser.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When
        int age = userService.calculateAge(mockUser.getUserId());

        // Then
        int expectedAge = 23;  // This will be valid in 2023
        assertEquals(expectedAge, age);
    }
}
