package org.kolesnyk.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.impl.EventDaoImpl;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.Event;
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
public class EventDaoTest {
    private Event event = DataGenerator.generateEvent();
    private String prefix = DataGenerator.getPrefixEvent() + event.getId();

    @Mock
    private Storage storage;
    @Spy
    private NamespaceResolver resolver;

    @InjectMocks
    private EventDaoImpl eventDao;

    @Test
    void findByIdShouldReturnEvent() {
        when(storage.get(prefix)).thenReturn(event);

        Event result = eventDao.findById(event.getId());

        verify(storage).get(prefix);
        assertEquals(event, result);
    }

    @Test
    void findAllUsersShouldReturnListOfEvents() {
        Set<Map.Entry<String, Object>> entries = DataGenerator.generateEntries();
        List<Event> events = DataGenerator.generateEventList();
        when(storage.entries()).thenReturn(entries);

        List<Event> result = eventDao.findAllEvents();
        for (Event event : result) {
            assertTrue(events.contains(event));
        }
    }

    @Test
    void saveShouldSaveEvent() {
        when(storage.save(prefix, event)).thenReturn(event);

        Event result = eventDao.save(event);

        verify(storage).save(prefix, event);
        assertEquals(event, result);
    }

    @Test
    void deleteByIdShouldDeleteAndReturnEvent() {
        when(storage.removeById(prefix)).thenReturn(true);

        boolean result = eventDao.deleteById(event.getId());

        verify(storage).removeById(prefix);
        assertTrue(result);
    }
}
