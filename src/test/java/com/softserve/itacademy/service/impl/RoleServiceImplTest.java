package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    Role expected;

    final Long ROLE_ID = 1L;
    final String NAME = "name";

    @BeforeEach
    void setUp() {
        expected = new Role();
        expected.setName(NAME);
    }

    @Test
    void shouldCreateRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(expected);

        Role actual = roleService.create(expected);

        verify(roleRepository).save(any(Role.class));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionAndNotCreateRole() {
        when(roleRepository.save(any(Role.class))).thenThrow(new IllegalArgumentException());

        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            roleService.create(expected);
        });

        Assertions.assertEquals("Role cannot be 'null'", exception.getMessage());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void shouldReadByIdAndReturnRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        Role actual = roleService.readById(ROLE_ID);

        verify(roleRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionReadById() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roleService.readById(ROLE_ID);
        });

    }

    @Test
    void shouldUpdateRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(roleRepository.save(any(Role.class))).thenReturn(expected);

        Role actual = roleService.update(expected);

        verify(roleRepository).save(any(Role.class));
        verify(roleRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionUpdateRole() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            roleService.update(null);
        });

        Assertions.assertEquals("Role cannot be 'null'", exception.getMessage());
        verifyNoInteractions(roleRepository);
    }

    @Test
    void shouldNotUpdateRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            roleService.update(expected);
        });

        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void shouldDeleteRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        roleService.delete(ROLE_ID);

        verify(roleRepository).findById(anyLong());
        verify(roleRepository).delete(any(Role.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionDeleteRole() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            roleService.delete(ROLE_ID);
        });

        verify(roleRepository).findById(anyLong());
        verify(roleRepository, times(0)).delete(any(Role.class));
    }

    @Test
    void shouldReturnNewArrayListGetAll() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> actual = roleService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(roleRepository).findAll();
    }

    @Test
    void shouldReturnListRolesGetAll() {
        List<Role> expected = Arrays.asList(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(expected);

        List<Role> actual = roleService.getAll();

        Assertions.assertEquals(expected.size(), actual.size());
        verify(roleRepository).findAll();
    }
}
