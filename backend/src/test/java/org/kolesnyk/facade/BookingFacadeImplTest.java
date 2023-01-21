package org.kolesnyk.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.facade.impl.BookingFacadeImpl;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.TicketImpl;
import org.kolesnyk.model.impl.UserImpl;
import org.kolesnyk.service.EventService;
import org.kolesnyk.service.TicketService;
import org.kolesnyk.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BookingFacadeImplTest {
    private static final Long GENERIC_ID = 1L;
    private static final String GENERIC_STRING = "somestring";
    private static final int PAGE_SIZE = 3;
    private static final int PAGE_NUMBER = 1;
    private static final LocalDateTime DATE =
            LocalDateTime.parse("2020-01-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    private static final User USER = new UserImpl();
    private static final Event EVENT = new EventImpl();
    private static final Ticket TICKET = new TicketImpl();
    private static final List<Event> EVENTS = Arrays.asList(EVENT);
    private static final List<User> USERS = Arrays.asList(USER);
    private static final List<Ticket> TICKETS = Arrays.asList(TICKET);


    @Mock
    private UserService userService;

    @Mock
    private TicketService ticketService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private BookingFacadeImpl bookingFacade;

    @Test
    void getEventByIdShouldDelegateToSimilarMethodInEventService() {
        when(eventService.getEventById(anyLong())).thenReturn(EVENT);
        Event res = bookingFacade.getEventById(GENERIC_ID);
        verify(eventService).getEventById(anyLong());
        assertEquals(EVENT, res);
    }

    @Test
    void getEventsByTitleShouldDelegateToSimilarMethodInEventService() {
        when(eventService.getEventsByTitle(anyString(), anyInt(), anyInt())).thenReturn(EVENTS);
        List<Event> res = bookingFacade.getEventsByTitle(GENERIC_STRING, PAGE_SIZE, PAGE_NUMBER);
        verify(eventService).getEventsByTitle(anyString(), anyInt(), anyInt());
        assertEquals(EVENTS.get(0), res.get(0));
    }

    @Test
    void getEventsForDayShouldDelegateToSimilarMethodInEventService() {
        when(eventService.getEventsForDay(any(LocalDateTime.class), anyInt(), anyInt())).thenReturn(EVENTS);
        List<Event> res = bookingFacade.getEventsForDay(DATE, PAGE_SIZE, PAGE_NUMBER);
        verify(eventService).getEventsForDay(any(LocalDateTime.class), anyInt(), anyInt());
        assertEquals(EVENTS.get(0), res.get(0));
    }

    @Test
    void createEventShouldDelegateToSimilarMethodInEventService() {
        when(eventService.createEvent(any(Event.class))).thenReturn(EVENT);
        Event res = bookingFacade.createEvent(EVENT);
        verify(eventService).createEvent(any(Event.class));
        assertEquals(EVENT, res);
    }

    @Test
    void updateEventShouldDelegateToSimilarMethodInEventService() {
        when(eventService.updateEvent(any(Event.class))).thenReturn(EVENT);
        Event res = bookingFacade.updateEvent(EVENT);
        verify(eventService).updateEvent(any(Event.class));
        assertEquals(EVENT, res);
    }

    @Test
    void deleteEventShouldDelegateToSimilarMethodInEventService() {
        when(eventService.deleteEvent(anyLong())).thenReturn(true);
        boolean res = bookingFacade.deleteEvent(GENERIC_ID);
        verify(eventService).deleteEvent(anyLong());
        assertTrue(res);
    }

    @Test
    void getUserByIdShouldDelegateToSimilarMethodInUserService() {
        when(userService.getUserById(anyLong())).thenReturn(USER);
        User res = bookingFacade.getUserById(GENERIC_ID);
        verify(userService).getUserById(anyLong());
        assertEquals(USER, res);
    }

    @Test
    void getUserByEmailShouldDelegateToSimilarMethodInUserService() {
        when(userService.getUserByEmail(anyString())).thenReturn(USER);
        User res = bookingFacade.getUserByEmail(GENERIC_STRING);
        verify(userService).getUserByEmail(anyString());
        assertEquals(USER, res);
    }

    @Test
    void getUsersByNameShouldDelegateToSimilarMethodInUserService() {
        when(userService.getUsersByName(anyString(), anyInt(), anyInt())).thenReturn(USERS);
        List<User> res = bookingFacade.getUsersByName(GENERIC_STRING, PAGE_SIZE, PAGE_NUMBER);
        verify(userService).getUsersByName(anyString(), anyInt(), anyInt());
        assertEquals(USERS.get(0), res.get(0));
    }

    @Test
    void createUserShouldDelegateToSimilarMethodInUserService() {
        when(userService.createUser(any(User.class))).thenReturn(USER);
        User res = bookingFacade.createUser(USER);
        verify(userService).createUser(any(User.class));
        assertEquals(USER, res);
    }

    @Test
    void updateUserShouldDelegateToSimilarMethodInUserService() {
        when(userService.updateUser(any(User.class))).thenReturn(USER);
        User res = bookingFacade.updateUser(USER);
        verify(userService).updateUser(any(User.class));
        assertEquals(USER, res);
    }

    @Test
    void deleteUserShouldDelegateToSimilarMethodInUserService() {
        when(userService.deleteUser(anyLong())).thenReturn(true);
        boolean res = bookingFacade.deleteUser(GENERIC_ID);
        verify(userService).deleteUser(anyLong());
        assertTrue(res);
    }

    @Test
    void bookTicketShouldDelegateToSimilarMethodInTicketService() {
        when(ticketService.bookTicket(anyLong(), anyLong(), anyInt(), any(Ticket.Category.class)))
                .thenReturn(TICKET);
        Ticket res = bookingFacade.bookTicket(GENERIC_ID, GENERIC_ID, 1, Ticket.Category.STANDARD);
        verify(ticketService).bookTicket(anyLong(), anyLong(), anyInt(), any(Ticket.Category.class));
        assertEquals(TICKET, res);
    }

    @Test
    void getBookedTicketsForUserShouldDelegateToSimilarMethodInTicketService() {
        when(ticketService.getBookedTickets(any(User.class), anyInt(), anyInt())).thenReturn(TICKETS);
        List<Ticket> res = bookingFacade.getBookedTickets(USER, PAGE_SIZE, PAGE_NUMBER);
        verify(ticketService).getBookedTickets(any(User.class), anyInt(), anyInt());
        assertEquals(TICKETS.get(0), res.get(0));
    }

    @Test
    void getBookedTicketsForEventShouldDelegateToSimilarMethodInTicketService() {
        when(ticketService.getBookedTickets(any(Event.class), anyInt(), anyInt())).thenReturn(TICKETS);
        List<Ticket> res = bookingFacade.getBookedTickets(EVENT, PAGE_SIZE, PAGE_NUMBER);
        verify(ticketService).getBookedTickets(any(Event.class), anyInt(), anyInt());
        assertEquals(TICKETS.get(0), res.get(0));
    }

    @Test
    void cancelTicketShouldDelegateToSimilarMethodInTicketService() {
        when(ticketService.cancelTicket(GENERIC_ID)).thenReturn(true);
        boolean res = bookingFacade.cancelTicket(GENERIC_ID);
        verify(ticketService).cancelTicket(GENERIC_ID);
        assertTrue(res);
    }
}
