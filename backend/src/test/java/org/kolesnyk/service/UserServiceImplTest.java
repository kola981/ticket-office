package org.kolesnyk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.UserDao;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.User;
import org.kolesnyk.service.impl.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private final User user = DataGenerator.generateUser();
    private final List<User> users = DataGenerator.generateUserList();

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserShouldCreateUser() {
        when(userDao.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        verify(userDao).save(any(User.class));
        assertEquals(user, result);
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userDao.findAllUsers()).thenReturn(users);

        User result = userService.updateUser(user);

        verify(userDao).save(any(User.class));
        assertEquals(user, result);
    }

    @Test
    void deleteUserShouldReturnTrue() {
        when(userDao.deleteById(user.getId())).thenReturn(true);

        boolean result = userService.deleteUser(user.getId());

        verify(userDao).deleteById(user.getId());
    }

    @Test
    void getUserByIdShouldExecuteDaoOnce() {
        userService.getUserById(user.getId());
        verify(userDao).findById(user.getId());
    }

    @Test
    void getUserByEmailShouldReturnUser() {
        when(userDao.findAllUsers()).thenReturn(users);

        User result = userService.getUserByEmail(user.getEmail());

        assertEquals(user, result);
    }

    @Test
    void getUsersByNameShouldReturnUser() {
        when(userDao.findAllUsers()).thenReturn(users);

        List<User> result = userService.getUsersByName(user.getName(), 2, 1);

        assertEquals(user, result.get(0));
        assertTrue(result.size() == 1);
    }
}
