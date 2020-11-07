package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class StateRepositoryTest {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnStateByName() {

        String name = "test";
        State expected = new State();
        expected.setName(name);

        stateRepository.save(expected);

        State actual = stateRepository.getByName(name);

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void shouldReturnNull() {
        String name = "test2";
        State actual = stateRepository.getByName(name);

        Assertions.assertNull(actual);
    }

    @Test
    void shouldReturnListOfStates() {
        String name = "test";
        State expected = new State();
        expected.setName(name);

        stateRepository.save(expected);

        List<State> actual = stateRepository.getAll();

        Assertions.assertNotEquals(0, actual.size());
    }
}
