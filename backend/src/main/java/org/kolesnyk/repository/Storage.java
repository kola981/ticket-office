package org.kolesnyk.repository;


import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class Storage {

    private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);

    @Value("${data.file}")
    private String pathToFile;

    private final Map<String, Object> storage = new HashMap();

    public Storage() {
    }


    public void setPathToFile(String path) {
        pathToFile = path;
    }

    public Object save(String K, Object V) {
        Object o = storage.put(K, V);
        LOGGER.info("SAVE  ["+K+"]"+prettyPrint(o!=null?o:V));
        LOGGER.info(storage.toString());
        return o;
    }

    public Object get(String K) {
        Object o = storage.get(K);
        LOGGER.info("Get  ["+K+"]"+(o!=null?prettyPrint(o):null));
        LOGGER.info(storage.toString());
        return o;
    }

    public String getPath() {
        return pathToFile;
    }

    public Collection<Object> getAll() {
        return storage.values();
    }

    public Set<Map.Entry<String, Object>> entries() {
        return storage.entrySet();
    }

    public boolean removeById(String key) {
        Object o = storage.remove(key);
        LOGGER.info("REMOVE   ["+key+"]"+prettyPrint(o));
        LOGGER.info(storage.toString());
        return o != null;
    }

    public String getFilePath() {
        return pathToFile;
    }

    private String prettyPrint(Object o){
        if (o instanceof UserImpl)
            return ((User) o).toString();
        else if (o instanceof EventImpl)
            return ((Event) o).toString();
        else
            return ((Ticket) o).toString();
    }

}
