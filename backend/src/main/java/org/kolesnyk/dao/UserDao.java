package org.kolesnyk.dao;

import org.kolesnyk.model.User;

import java.util.List;

public interface UserDao {

    User findById(Long id);

    List<User> findAllUsers();

    User save(User user);

    boolean deleteById(long userId);
}
