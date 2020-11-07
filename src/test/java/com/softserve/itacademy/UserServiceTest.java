package com.softserve.itacademy;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    User expected;
    Role role;
    final Long userId = 1L;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setName("USER");

        expected = new User();
        expected.setId(userId);
        expected.setFirstName("Sara");
        expected.setLastName("Black");
        expected.setEmail("sara@mail.com");
        expected.setPassword("0000");
        expected.setRole(role);
    }

    @Test
    public void shouldCreateUser() {
        Mockito.when(userRepository.save(expected)).thenReturn(expected);

        User actual = userService.create(expected);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionAndNotCreateUser() {
        Mockito.when(userRepository.save(null)).thenThrow(new IllegalArgumentException());

        Exception exception = Assertions.assertThrows(NullEntityReferenceException.class, () -> userService.create(null));
        Assertions.assertEquals("User cannot be 'null'", exception.getMessage());
    }

    @Test
    public void shouldReadByIdAndReturnUser() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(expected));

        User actual = userService.readById(userId);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionReadById() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> userService.readById(userId));
        Assertions.assertEquals("User with id " + userId + " not found", exception.getMessage());
    }

    @Test
    public void shouldUpdateUser() {
        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(expected));
        Mockito.when(userRepository.save(expected)).thenReturn(expected);

        expected.setFirstName("Lora");
        expected.setPassword("9999");
        User actual = userService.update(expected);

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void shouldThrowNullEntityReferenceExceptionUpdateUser() {
        Exception exception = Assertions.assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
        Assertions.assertEquals("User cannot be 'null'", exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionAndNotUpdateUser() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        expected.setFirstName("Lora");
        expected.setPassword("9999");

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> userService.update(expected));
        Assertions.assertEquals("User with id " + userId + " not found", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }

    @Test
    public void shouldDeleteUser() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(expected));

        userService.delete(userId);

        Mockito.verify(userRepository).findById(Mockito.anyLong());
        Mockito.verify(userRepository).delete(Mockito.any(User.class));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionDeleteUser() {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> userService.delete(userId));
        Assertions.assertEquals("User with id " + userId + " not found", exception.getMessage());
        Mockito.verify(userRepository).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(0)).delete(Mockito.any(User.class));
    }

    @Test
    public void shouldReturnNewArrayListGetAll() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> actual = userService.getAll();

        Assertions.assertEquals(0, actual.size());
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void shouldReturnListUserGetAll() {
        List<User> expectedList = Arrays.asList(expected);
        Mockito.when(userRepository.findAll()).thenReturn(expectedList);

        List<User> actual = userService.getAll();

        Assertions.assertTrue(actual.containsAll(expectedList) && expectedList.containsAll(actual));
        Mockito.verify(userRepository).findAll();
    }

}
