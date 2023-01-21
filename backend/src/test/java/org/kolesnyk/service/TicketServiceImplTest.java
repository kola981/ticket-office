package org.kolesnyk.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.dao.EventDao;
import org.kolesnyk.dao.TicketDao;
import org.kolesnyk.dao.UserDao;
import org.kolesnyk.datagenerators.DataGenerator;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.service.impl.TicketServiceImpl;
import org.kolesnyk.utils.Generator;
import org.kolesnyk.utils.NamespaceResolver;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    private final static Long USER_ID2 = 5L;
    private final static Long USER_ID3 = 6L;


    private final Ticket ticket = DataGenerator.generateTicket();
    private final List<Ticket> tickets = DataGenerator.generateTicketList();
    private final List<User> users = DataGenerator.generateUserList();
    private final List<Event> events = DataGenerator.generateEventList();




    @Mock
    private TicketDao ticketDao;
    @Mock
    private UserDao userDao;
    @Mock
    private EventDao eventDao;
    @Mock
    private NamespaceResolver resolver;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void bookTicketShouldThrowIllegalStateExceptionIfPlaceIsBooked() {
        when(ticketDao.findAllTickets()).thenReturn(tickets);

        assertThrows(IllegalStateException.class,
                () -> ticketService.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(),
                        ticket.getCategory()));
    }

    @Test
    void bookTicketShouldReturnTicket() {
        when(ticketDao.findAllTickets()).thenReturn(tickets);
        when(ticketDao.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.bookTicket(ticket.getUserId(), ticket.getEventId(), 3,
                ticket.getCategory());

        assertAll(
                () -> assertEquals(ticket.getUserId(), result.getUserId()),
                () -> assertEquals(ticket.getEventId(), result.getEventId()),
                () -> assertEquals(3, result.getPlace()),
                () -> assertEquals(ticket.getCategory(), result.getCategory()),
                () -> verify(ticketDao).save(any(Ticket.class))
        );
    }

    @Test
    void cancelTicketShouldUseDaoOnce(){
        ticketService.cancelTicket(anyLong());
        verify(ticketDao).deleteById(anyLong());
    }

    @Test
    @Disabled
    void getBookedTicketsByUserShouldReturnTicketsSortedByDate(){
        when(ticketDao.findAllTickets()).thenReturn(tickets);
        when(userDao.findAllUsers()).thenReturn(users);
        when(eventDao.findAllEvents()).thenReturn(events);

        List<Ticket> result = ticketService.getBookedTickets(users.get(0),2,1);

        assertAll(
                () -> assertEquals(tickets.get(1), result.get(0)),
                () -> assertEquals(tickets.get(0), result.get(1))
        );
    }

    @Test
    void getBookedTicketsByEventShouldReturnTicketsSortedByEmail(){
        when(ticketDao.findAllTickets()).thenReturn(tickets);
        when(userDao.findById(USER_ID2)).thenReturn(users.get(1));
        when(userDao.findById(USER_ID3)).thenReturn(users.get(2));
        when(eventDao.findById(anyLong())).thenReturn(events.get(1));

        List<Ticket> result = ticketService.getBookedTickets(events.get(1),2,1);

        assertAll(
                () -> assertEquals(tickets.get(2), result.get(0)),
                () -> assertEquals(tickets.get(3), result.get(1))
        );
    }

    @AfterAll
    static void cleanup(){
        Generator.clear();
    }
}
