package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.StateRepository;
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
class StateServiceImplTest {

    @Mock
    StateRepository stateRepository;

    @InjectMocks
    StateServiceImpl stateService;

    State expected;

    final Long STATE_ID = 1L;
    final String NAME = "name";

    @BeforeEach
    void setUp() {
        expected = new State();

        List<Task> taskList = Arrays.asList(new Task(), new Task());

        expected.setId(STATE_ID);
        expected.setName(NAME);
        expected.setTasks(taskList);
    }

    @Test
    void shouldCreateStateAndReturn() {
        when(stateRepository.save(any(State.class))).thenReturn(expected);

        State actual = stateService.create(expected);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).save(any(State.class));
    }

    @Test
    void shouldThrowNullEntityReferenceException() {
        when(stateRepository.save(any(State.class))).thenThrow(new IllegalArgumentException());

        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            stateService.create(expected);
        });

        Assertions.assertEquals("State cannot be 'null'", exception.getMessage());
        verify(stateRepository).save(any(State.class));
    }

    @Test
    void shouldReadById() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        State actual = stateService.readById(STATE_ID);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).findById(anyLong());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionReadById() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.readById(STATE_ID);
        });

        Assertions.assertEquals("State with id " + STATE_ID + " not found", exception.getMessage());
        verify(stateRepository).findById(anyLong());
    }

    @Test
    void shouldUpdateState() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(stateRepository.save(any(State.class))).thenReturn(expected);

        State actual = stateService.update(expected);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).findById(anyLong());
        verify(stateRepository).save(any(State.class));
    }

    @Test
    void shouldThrowNullEntityReferenceExceptionAndNotUpdateState() {
        Exception exception = assertThrows(NullEntityReferenceException.class, () -> {
            stateService.update(null);
        });

        Assertions.assertEquals("State cannot be 'null'", exception.getMessage());
        verifyNoInteractions(stateRepository);
    }

    @Test
    void shouldNotUpdateState() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.update(expected);
        });

        Assertions.assertEquals("State with id 1 not found", exception.getMessage());
        verify(stateRepository, times(0)).save(any(State.class));
    }

    @Test
    void shouldDeleteState() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        stateService.delete(STATE_ID);

        verify(stateRepository).findById(anyLong());
        verify(stateRepository).delete(any(State.class));
    }

    @Test
    void shouldThRowEntityNotFoundExceptionAndNorDeleteState() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.delete(STATE_ID);
        });

        Assertions.assertEquals("State with id " + STATE_ID + " not found", exception.getMessage());
        verify(stateRepository).findById(anyLong());
        verify(stateRepository, times(0)).delete(any(State.class));
    }

    @Test
    void shouldGetByName() {
        when(stateRepository.getByName(anyString())).thenReturn(expected);

        State actual = stateService.getByName("name");

        verify(stateRepository).getByName(anyString());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void shouldEntityNotFoundExceptionGetByName() {
        when(stateRepository.getByName(anyString())).thenReturn(null);

        String name = "name";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.getByName(name);
        });

        Assertions.assertEquals("State with name '" + name + "' not found", exception.getMessage());
        verify(stateRepository).getByName(anyString());
    }

    @Test
    void shouldReturnNewArrayList() {
        when(stateRepository.getAll()).thenReturn(Collections.emptyList());

        List<State> actual = stateService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(stateRepository).getAll();
    }

    @Test
    void shouldReturnStateList() {
        List<State> expected = Arrays.asList(new State(), new State());

        when(stateRepository.getAll()).thenReturn(expected);

        List<State> actual = stateService.getAll();

        verify(stateRepository).getAll();
        Assertions.assertEquals(expected, actual);
    }
}
