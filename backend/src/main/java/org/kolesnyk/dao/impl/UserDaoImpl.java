package org.kolesnyk.dao.impl;

import org.kolesnyk.dao.UserDao;
import org.kolesnyk.model.User;
import org.kolesnyk.repository.Storage;
import org.kolesnyk.utils.NamespaceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private static final String PREFIX = "user:";
    @Autowired
    private Storage storage;
    @Autowired
    private NamespaceResolver resolver;

    public void setStorage(Storage storage){
        this.storage = storage;
    }

    public void setResolver(NamespaceResolver resolver){
        this.resolver = resolver;
    }

    @Override
    public User findById(Long id) {
        return (User) storage.get(PREFIX+id);
    }

    @Override
    public List<User> findAllUsers() {
        Set<Map.Entry<String, Object>> entries = storage.entries();
        return entries.stream()
                .filter(e -> resolver.isUser(e.getKey()))
                .map(u -> (User) u.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        return (User) storage.save(PREFIX+user.getId(), user);
    }

    @Override
    public boolean deleteById(long userId) {
        return storage.removeById(PREFIX+userId);
    }
}
