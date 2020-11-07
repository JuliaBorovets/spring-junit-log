package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoService todoService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Test
    void createGetTest() throws Exception {
        Long ownerId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/create/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.model().attribute("ownerId", ownerId))
                .andExpect(MockMvcResultMatchers.view().name("create-todo"));
    }


    @Test
    void readGetTest() throws Exception {
        long id = 7L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo", "tasks", "users"))
                .andExpect(MockMvcResultMatchers.view().name("todo-tasks"));
    }

    @Test
    void updateGetTest() throws Exception {
        long todoId = 7L;
        long ownerId = 6L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + todoId + "/update/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo"))
                .andExpect(MockMvcResultMatchers.view().name("update-todo"));
    }

    @Test
    void updatePostTest() throws Exception {
        long todoId = 7L;
        long ownerId = 4L;

        ToDo toDo = new ToDo();
        toDo.setId(todoId);
        toDo.setTitle("title");
        toDo.setCreatedAt(LocalDateTime.now());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todos/" + todoId + "/update/users/" + ownerId)
                        .flashAttr("todo", toDo)
        )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/all/users/" + ownerId));

    }

    @Test
    void updatePostTestException() throws Exception {
        long todoId = 7L;
        long ownerId = 4L;

        ToDo toDo = new ToDo();
        toDo.setId(todoId);
        toDo.setTitle(" ");
        toDo.setCreatedAt(LocalDateTime.now());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/todos/" + todoId + "/update/users/" + ownerId)
                        .flashAttr("todo", toDo)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update-todo"));

    }

    @Test
    void deleteGetTest() throws Exception {
        long todoId = 13L;
        long ownerId = 6L;

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + todoId + "/delete/users/" + ownerId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/all/users/" + ownerId));
    }

    @Test
    void getAllTest() throws Exception {
        long userId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/all/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("todos", "user"))
                .andExpect(MockMvcResultMatchers.view().name("todos-user"));
    }

    @Test
    void addCollaborator() throws Exception {
        long id = 12L;
        long userId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/add").param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/" + id + "/tasks"));
    }

    @Test
    void removeCollaborator() throws Exception {
        long id = 7L;
        long userId = 6L;
        mockMvc.perform(MockMvcRequestBuilders.get("/todos/" + id + "/remove").param("user_id", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/todos/" + id + "/tasks"));
    }
}
