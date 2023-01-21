package org.kolesnyk.service.impl;

import org.kolesnyk.dao.EventDao;
import org.kolesnyk.model.Event;
import org.kolesnyk.service.EventService;
import org.kolesnyk.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);
    @Autowired
    private EventDao eventDao;

    public EventServiceImpl() {
    }

    @Override
    public Event getEventById(long eventId) {
        return eventDao.findById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Event> events = eventDao.findAllEvents();

        events = events.stream()
                .filter(e -> e.getTitle().contains(title))
                .collect(Collectors.toList());

        if (events.isEmpty()) return events;
        else {
            int from = Math.min(pageSize * pageNum, 1);
            int to = Math.min(from + pageSize, events.size());
            return new ArrayList<>(events.subList(from - 1, to));
        }
    }

    @Override
    public List<Event> getEventsForDay(LocalDateTime day, int pageSize, int pageNum) {
        List<Event> events = eventDao.findAllEvents();

        events = events.stream()
                .filter(e -> Objects.equals(day.toLocalDate(), e.getDate().toLocalDate()))
                .collect(Collectors.toList());

        if (events.isEmpty()) return events;
        else {
            int from = Math.min(pageSize * pageNum, 1);
            int to = Math.min(from + pageSize, events.size());
            return new ArrayList<>(events.subList(from - 1, to));
        }
    }

    @Override
    public Event createEvent(Event event) {
        event.setId(Generator.generateEventId());
        return eventDao.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventDao.save(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventDao.deleteById(eventId);
    }

    public void setEventDao(EventDao eventDao){
        this.eventDao = eventDao;
    }
}
