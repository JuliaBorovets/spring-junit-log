package com.softserve.itacademy;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.impl.RoleServiceImpl;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Test
    public void shouldCreateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/create"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
                .param("firstName", "Sara")
                .param("lastName", "Black")
                .param("password", "0000")
                .param("email", "sara@mail.com"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        User user = userRepository.getUserByEmail("sara@mail.com");
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getFirstName(), "Sara");
        Assertions.assertEquals(user.getLastName(), "Black");
    }

    @Test
    public void shouldReadByIdAndReturnUser() throws Exception {
        Long userId = 5L;
        User expected = userService.readById(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId + "/read"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", expected));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        // Change FirstName and Role (only Admin can change his Role)
        Long userId = 4L;
        String newFirstName = "Alex";
        User oldUser = userService.readById(userId);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId + "/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", oldUser))
                .andExpect(MockMvcResultMatchers.model().attributeExists("roles"))
                .andExpect(MockMvcResultMatchers.model().attribute("roles", roleService.getAll()));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/" + userId + "/update")
                .param("firstName", newFirstName)
                .param("lastName", oldUser.getLastName())
                .param("oldPassword", oldUser.getPassword())
                .param("password", oldUser.getPassword())
                .param("email", oldUser.getEmail())
                .param("roleId", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        User user = userService.readById(userId);
        oldUser.setFirstName(newFirstName);
        oldUser.setRole(roleService.readById(2L));
        Assertions.assertEquals(user, oldUser);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        Long userId = 5L;
        User userToDelete = userService.readById(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId + "/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        Assertions.assertNotNull(userToDelete);
        Assertions.assertFalse(userRepository.findById(userId).isPresent());
    }

    @Test
    public void shouldReturnListUserGetAll() throws Exception {
        List<User> expected = userRepository.findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", expected));
    }
}
