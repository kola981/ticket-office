package org.kolesnyk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.EventDao;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.Event;
import org.kolesnyk.service.impl.EventServiceImpl;
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
public class EventServiceImplTest {
    private final Event event = DataGenerator.generateEvent();
    private final List<Event> events = DataGenerator.generateEventList();

    @Mock
    private EventDao eventDao;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEventShouldReturnEvent() {
        when(eventDao.save(any(Event.class))).thenReturn(event);

        Event result = eventService.createEvent(event);

        verify(eventDao).save(any(Event.class));
        assertEquals(event, result);
    }

    @Test
    void updateEventShouldUpdateExistingEvent() {
        when(eventDao.save(any(Event.class))).thenReturn(event);

        Event result = eventService.updateEvent(event);

        verify(eventDao).save(any(Event.class));
        assertEquals(event, result);
    }

    @Test
    void deleteEventShouldReturnTrue() {
        when(eventDao.deleteById(event.getId())).thenReturn(true);

        boolean result = eventService.deleteEvent(event.getId());

        verify(eventDao).deleteById(event.getId());
    }

    @Test
    void getEventByIdShouldExecuteDaoOnce() {
        eventService.getEventById(event.getId());
        verify(eventDao).findById(event.getId());
    }

    @Test
    void getEventByTitleShouldReturnEvent() {
        when(eventDao.findAllEvents()).thenReturn(events);

        List<Event> result = eventService.getEventsByTitle(event.getTitle(),1,1);

        assertEquals(event, result.get(0));
        assertTrue(result.size()==1);
    }

    @Test
    void getEventsByDateShouldReturnEvent() {
        when(eventDao.findAllEvents()).thenReturn(events);

        List<Event> result = eventService.getEventsForDay(event.getDate(), 2, 1);

        assertEquals(event, result.get(0));
        assertTrue(result.size() == 2);
    }
}
