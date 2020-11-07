package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.TaskRepository;
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
class TaskServiceImplTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;


    Task expected;

    final Long TASK_ID = 1L;
    final String NAME = "name";

    @BeforeEach
    void setUp() {
        expected = new Task();

        expected.setId(TASK_ID);
        expected.setName(NAME);
        expected.setPriority(Priority.LOW);
        expected.setState(new State());
        expected.setTodo(new ToDo());
    }

    @Test
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(expected);

        Task actual = taskService.create(expected);

        verify(taskRepository).save(any(Task.class));
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionAndNotCreateTask() {
        when(taskRepository.save(any(Task.class))).thenThrow(new IllegalArgumentException());

        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            taskService.create(expected);
        });

        Assertions.assertEquals("Task cannot be 'null'", exception.getMessage());
    }

    @Test
    void shouldReadByIdAndReturnTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        Task actual = taskService.readById(TASK_ID);

        verify(taskRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionReadById() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.readById(TASK_ID);
        });

        Assertions.assertEquals("Task with id " + TASK_ID + " not found", exception.getMessage());
    }

    @Test
    void shouldUpdateTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(taskRepository.save(any(Task.class))).thenReturn(expected);

        Task actual = taskService.update(expected);

        verify(taskRepository).save(any(Task.class));
        verify(taskRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionUpdateTask() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            taskService.update(null);
        });

        Assertions.assertEquals("Task cannot be 'null'", exception.getMessage());
        verifyNoInteractions(taskRepository);
    }

    @Test
    void shouldNotUpdateTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.update(expected);
        });

        Assertions.assertEquals("Task with id 1 not found", exception.getMessage());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        taskService.delete(TASK_ID);

        verify(taskRepository).findById(anyLong());
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionDeleteTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.delete(TASK_ID);
        });

        Assertions.assertEquals("Task with id " + TASK_ID + " not found", exception.getMessage());
        verify(taskRepository).findById(anyLong());
        verify(taskRepository, times(0)).delete(any(Task.class));
    }

    @Test
    void shouldReturnNewArrayListGetAll() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<Task> actual = taskService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnListToDoGetAll() {
        List<Task> expected = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(expected);

        List<Task> actual = taskService.getAll();

        Assertions.assertEquals(expected.size(), actual.size());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldReturnNewArrayListGetByTodoId() {
        when(taskRepository.getByTodoId(anyLong())).thenReturn(Collections.emptyList());

        List<Task> actual = taskService.getByTodoId(1L);

        Assertions.assertEquals(0, actual.size());
        verify(taskRepository).getByTodoId(anyLong());
    }

    @Test
    void shouldReturnListToDoGetByTodoId() {
        List<Task> expected = Arrays.asList(new Task(), new Task());
        when(taskRepository.getByTodoId(anyLong())).thenReturn(expected);

        List<Task> actual = taskService.getByTodoId(1L);

        Assertions.assertEquals(expected.size(), actual.size());
        verify(taskRepository).getByTodoId(anyLong());
    }
}
