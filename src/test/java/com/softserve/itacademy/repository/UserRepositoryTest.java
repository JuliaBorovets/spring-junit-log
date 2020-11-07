package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void shouldGetUserByEmail() {
        User expected = new User();
        expected.setFirstName("Sara");
        expected.setLastName("Black");
        expected.setEmail("sara@mail.com");
        expected.setPassword("0000");
        expected.setRole(roleRepository.getOne(2L));

        expected = userRepository.save(expected);
        User actual = userRepository.getUserByEmail("sara@mail.com");

        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void shouldReturnNullGetUserByEmail() {
        User actual = userRepository.getUserByEmail("com");

        Assertions.assertNull(actual);
    }
}
