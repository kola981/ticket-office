package org.kolesnyk.repository;


import org.kolesnyk.dao.EventDao;
import org.kolesnyk.dao.UserDao;
import org.kolesnyk.dto.UserRole;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.UserImpl;
import org.kolesnyk.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StorageFiller {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageFiller.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private Storage storage;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void initData() {
        String pathToFile = storage.getFilePath();
        LOGGER.info("Reading data from file" + pathToFile+"\n");
        populateData(pathToFile);
    }

    public void populateData(String pathToFile) {
        try {
            List<String> data = Files.readAllLines(Paths.get(pathToFile));

            for (String item:data) {
                String[] piecesOfData = item.split(";");
                if ("user".equals(piecesOfData[0]))
                    parseUser(piecesOfData);
                else if ("event".equals(piecesOfData[0]))
                    parseEvent(piecesOfData);
            }
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
        }

    }

    private void parseEvent(String[] item) {
        Event event = new EventImpl();
        event.setId(Generator.generateEventId());
        event.setTitle(item[1]);
        event.setDate(LocalDateTime.parse(item[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        eventDao.save(event);
    }

    private void parseUser(String[] item) {
        UserRole role = getRole(item[4]);

        User user = new UserImpl();
        user.setId(Generator.generateUserId());
        user.setEmail(item[1]);
        user.setName(item[2]);
        user.setPassword(item[3]);
        user.setRole(role);

        userDao.save(user);
    }

    private UserRole getRole(String role) {
        return "admin".equals(role.toLowerCase()) ? UserRole.ADMIN :
                "user".equals(role.toLowerCase()) ? UserRole.USER
                : null;
    }

}
