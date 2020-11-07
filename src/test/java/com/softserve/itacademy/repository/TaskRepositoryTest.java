package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.repository.TaskRepository;
import com.softserve.itacademy.repository.ToDoRepository;
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
public class TaskRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    StateRepository stateRepository;

    @Test
    public void shouldGetTasksByTodoId() {
        ToDo todo = new ToDo();
        todo.setTitle("New ToDo");
        todo.setCreatedAt(LocalDateTime.now());
        Long toDoId = toDoRepository.save(todo).getId();

        Task task1 = new Task();
        task1.setName("task1");
        task1.setTodo(todo);
        task1.setPriority(Priority.MEDIUM);
        task1.setState(stateRepository.getByName("New"));
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setName("task2");
        task2.setTodo(todo);
        task2.setPriority(Priority.HIGH);
        task2.setState(stateRepository.getByName("New"));
        taskRepository.save(task2);

        List<Task> expected = Arrays.asList(task1, task2);
        List<Task> actual = taskRepository.getByTodoId(toDoId);

        Assertions.assertEquals(2, actual.size());
        Assertions.assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }
}
