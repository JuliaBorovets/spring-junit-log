package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.ToDoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {

    @Mock
    ToDoRepository todoRepository;

    @InjectMocks
    ToDoServiceImpl toDoService;

    ToDo expected;

    final Long TODO_ID = 1L;
    final Long USER_ID = 2L;
    final String TITLE = "title";

    @BeforeEach
    void setUp() {
        expected = new ToDo();
        User owner = new User();
        owner.setId(USER_ID);

        expected.setId(TODO_ID);
        expected.setTitle(TITLE);
        expected.setOwner(owner);
    }

    @Test
    void shouldCreateToDo() {
        when(todoRepository.save(any(ToDo.class))).thenReturn(expected);

        ToDo actual = toDoService.create(expected);

        verify(todoRepository).save(any(ToDo.class));
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionAndNotCreateToDo() {
        when(todoRepository.save(any(ToDo.class))).thenThrow(new RuntimeException());

        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            toDoService.create(null);
        });

        Assertions.assertEquals("To-Do cannot be 'null'", exception.getMessage());
    }

    @Test
    void shouldReadByIdAndReturnToDo() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        ToDo actual = toDoService.readById(TODO_ID);

        verify(todoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionReadById() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.readById(TODO_ID);
        });

        Assertions.assertEquals("To-Do with id " + TODO_ID + " not found", exception.getMessage());
    }

    @Test
    void shouldUpdateToDo() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(todoRepository.save(any(ToDo.class))).thenReturn(expected);

        ToDo actual = toDoService.update(expected);

        verify(todoRepository).save(any(ToDo.class));
        verify(todoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionUpdateToDo() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            toDoService.update(null);
        });

        Assertions.assertEquals("To-Do cannot be 'null'", exception.getMessage());
        verifyNoInteractions(todoRepository);
    }

    @Test
    void shouldNotUpdateToDo() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.update(expected);
        });

        Assertions.assertEquals("To-Do with id 1 not found", exception.getMessage());
        verify(todoRepository, times(0)).save(any(ToDo.class));
    }

    @Test
    void shouldDeleteToDo() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        toDoService.delete(TODO_ID);

        verify(todoRepository).findById(anyLong());
        verify(todoRepository).delete(any(ToDo.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionDeleteToDo() {
        when(todoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            toDoService.delete(TODO_ID);
        });

        Assertions.assertEquals("To-Do with id " + TODO_ID + " not found", exception.getMessage());
        verify(todoRepository).findById(anyLong());
        verify(todoRepository, times(0)).delete(any(ToDo.class));
    }

    @Test
    void shouldReturnNewArrayListGetAll() {
        when(todoRepository.findAll()).thenReturn(Collections.emptyList());

        List<ToDo> actual = toDoService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(todoRepository).findAll();
    }

    @Test
    void shouldReturnListToDoGetAll() {
        List<ToDo> expected = Arrays.asList(new ToDo(), new ToDo());
        when(todoRepository.findAll()).thenReturn(expected);

        List<ToDo> actual = toDoService.getAll();

        Assertions.assertEquals(expected.size(), actual.size());
        verify(todoRepository).findAll();
    }

    @Test
    void shouldReturnNewArrayListGetByUserId() {
        when(todoRepository.getByUserId(anyLong())).thenReturn(Collections.emptyList());

        List<ToDo> actual = toDoService.getByUserId(USER_ID);

        Assertions.assertEquals(0, actual.size());
        verify(todoRepository).getByUserId(anyLong());
    }

    @Test
    void shouldReturnListToDoGetByUserId() {
        List<ToDo> expected = Arrays.asList(new ToDo(), new ToDo());

        when(todoRepository.getByUserId(anyLong())).thenReturn(expected);

        List<ToDo> actual = toDoService.getByUserId(USER_ID);

        verify(todoRepository).getByUserId(anyLong());
        Assertions.assertEquals(expected, actual);
    }
}
