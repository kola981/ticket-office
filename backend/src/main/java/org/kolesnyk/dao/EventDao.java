package org.kolesnyk.dao;

import org.kolesnyk.model.Event;

import java.util.List;

public interface EventDao {

    Event findById(long eventId);

    List<Event> findAllEvents();

    Event save(Event event);

    boolean deleteById(long eventId);

}
