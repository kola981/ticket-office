package org.kolesnyk.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.impl.TicketDaoImpl;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.Ticket;
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
public class TicketDaoTest {
    private Ticket ticket = DataGenerator.generateTicket();
    private String prefix = DataGenerator.getPrefixTicket() + ticket.getId();

    @Mock
    private Storage storage;
    @Spy
    private NamespaceResolver resolver;

    @InjectMocks
    private TicketDaoImpl ticketDao;

    @Test
    void findAllUsersShouldReturnListOfTickets() {
        Set<Map.Entry<String, Object>> entries = DataGenerator.generateEntries();
        List<Ticket> tickets = DataGenerator.generateTicketList();
        when(storage.entries()).thenReturn(entries);

        List<Ticket> result = ticketDao.findAllTickets();
        for (Ticket ticket : result) {
            assertTrue(tickets.contains(ticket));
        }
    }

    @Test
    void saveShouldSaveTicket() {
        when(storage.save(prefix, ticket)).thenReturn(ticket);

        Ticket result = ticketDao.save(ticket);

        verify(storage).save(prefix, ticket);
        assertEquals(ticket, result);
    }

    @Test
    void deleteByIdShouldDeleteTicket() {
        when(storage.removeById(prefix)).thenReturn(true);

        boolean result = ticketDao.deleteById(ticket.getId());

        verify(storage).removeById(prefix);
        assertTrue(result);
    }

}
