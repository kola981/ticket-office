package org.kolesnyk.service.impl;

import org.kolesnyk.dao.UserDao;
import org.kolesnyk.model.User;
import org.kolesnyk.service.UserService;
import org.kolesnyk.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    public UserServiceImpl() {
    }

    @Override
    public User getUserById(long userId) {
        return userDao.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        List<User> users = userDao.findAllUsers();
        return users.stream()
                .filter(u->email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        List<User> users = userDao.findAllUsers();

        users = users.stream().filter(u->u.getName().contains(name)).collect(Collectors.toList());

        if(users.isEmpty()) return users;
        else {
            int from = Math.min(pageSize * pageNum, 1);
            int to = Math.min(from + pageSize, users.size());
            return new ArrayList<>(users.subList(from-1, to));
        }
    }

    @Override
    public User createUser(User user) {
        user.setId(Generator.generateUserId());
        return userDao.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existingRecord = getUserByEmail(user.getEmail());
        user.setId(existingRecord.getId());
        return userDao.save(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userDao.deleteById(userId);
    }

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
}
