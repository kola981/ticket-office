package org.kolesnyk.dao.impl;

import org.kolesnyk.dao.EventDao;
import org.kolesnyk.model.Event;
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
public class EventDaoImpl implements EventDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventDaoImpl.class);
    private static final String PREFIX = "event:";
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
    public Event findById(long eventId) {
        return (Event) storage.get(PREFIX+eventId);
    }

    @Override
    public List<Event> findAllEvents() {
        Set<Map.Entry<String, Object>> entries = storage.entries();
        return entries.stream()
                .filter(e -> resolver.isEvent(e.getKey()))
                .map(ev -> (Event) ev.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public Event save(Event event) {
        return (Event) storage.save(PREFIX+event.getId(), event);
    }

    @Override
    public boolean deleteById(long eventId) {
        return storage.removeById(PREFIX+eventId);
    }
}
