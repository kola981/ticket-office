package org.kolesnyk.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.impl.UserDaoImpl;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.User;
import org.kolesnyk.repository.Storage;
import org.kolesnyk.utils.NamespaceResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {
    private User user = DataGenerator.generateUser();
    private String prefix = DataGenerator.getPrefixUser() + user.getId();

    @Mock
    private Storage storage;
    @Spy
    private NamespaceResolver resolver;

    @InjectMocks
    private UserDaoImpl userDao;

    @Test
    void findByIdShouldReturnUser() {
        when(storage.get(prefix)).thenReturn(user);

        User result = userDao.findById(user.getId());

        verify(storage).get(prefix);
        assertEquals(user, result);
    }

    @Test
    void findAllUsersShouldReturnListOfUsers() {
        Set<Map.Entry<String, Object>> entries = DataGenerator.generateEntries();
        List<User> users = DataGenerator.generateUserList();
        when(storage.entries()).thenReturn(entries);

        List<User> result = userDao.findAllUsers();
        for (User user : result) {
            assertTrue(users.contains(user));
        }
    }

    @Test
    void saveShouldSaveUser() {
        when(storage.save(prefix, user)).thenReturn(user);

        User result = userDao.save(user);

        verify(storage).save(prefix, user);
        assertEquals(user, result);
    }

    @Test
    void deleteByIdShouldDeleteAndReturnUser() {
        when(storage.removeById(prefix)).thenReturn(true);

        boolean result = userDao.deleteById(user.getId());

        verify(storage).removeById(prefix);
        assertTrue(result);
    }
}
