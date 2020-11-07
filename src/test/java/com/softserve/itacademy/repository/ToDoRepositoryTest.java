package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void getByUserId() {
        ToDo toDo1 = new ToDo();
        toDo1.setTitle("title1");
        toDo1.setCreatedAt(LocalDateTime.now());

        ToDo toDo2 = new ToDo();
        toDo2.setTitle("title2");
        toDo2.setCreatedAt(LocalDateTime.now().minusDays(1));

        User owner = new User();
        owner.setEmail("test@gmail.com");
        owner.setFirstName("First");
        owner.setLastName("Last");
        owner.setPassword("password");
        owner.setMyTodos(Arrays.asList(toDo1, toDo2));

        toDo1.setOwner(owner);

        toDo2.setCollaborators(Arrays.asList(owner));

        userRepository.save(owner);
        toDoRepository.save(toDo1);
        toDoRepository.save(toDo2);

        List<ToDo> expected = Arrays.asList(toDo1, toDo2);

        List<ToDo> actual = toDoRepository.getByUserId(owner.getId());

        Assertions.assertEquals(expected.size(), actual.size());


    }
}
