package org.kolesnyk.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.handler.CustomExceptionResolver;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.TicketData;
import org.kolesnyk.model.impl.TicketImpl;
import org.kolesnyk.model.impl.UserImpl;
import org.kolesnyk.utils.TicketDataConverter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {
    public static final String BOOK_TICKET = "/tickets/book/0/0";
    public static final String CANCEL_TICKET = "/tickets/0";
    public static final String FIND_TICKETS_BY_USER = "/tickets/user/0";
    public static final String FIND_TICKETS_BY_EVENT = "/tickets/event/0";

    private static final String TICKETS_BY_USER = "ticketsByUser";
    private static final String TICKET = "ticket";
    private static final String TICKETS = "tickets";
    private static final String TICKETS_BY_EVENT = "ticketsByEvent";

    private final Ticket ticket = new TicketImpl();
    private final Event event = new EventImpl();
    private final User user = new UserImpl();
    private final TicketData ticketData = new TicketData();
    private final List<Ticket> ticketList = Arrays.asList(ticket);

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private TicketController controller;

    private static MockedStatic<TicketDataConverter> converterMockedStatic;

    private MockMvc mockMvc;

    @BeforeAll
    static void init(){
        converterMockedStatic = mockStatic(TicketDataConverter.class);
    }

    @AfterAll
    static void cleanup(){
        converterMockedStatic.closeOnDemand();
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(CustomExceptionResolver.class)
                .build();
        ticket.setId(0);
        event.setId(1L);
        user.setId(2L);
        ticketData.setId(0);
        ticketData.setEventId(1L);
        ticketData.setUserId(2L);
    }

    @Test
    void bookTicketShouldReturnTicket() throws Exception {
        when(bookingFacade.bookTicket(anyLong(),anyLong(),anyInt(),any(Ticket.Category.class))).thenReturn(ticket);
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(bookingFacade.getEventById(anyLong())).thenReturn(event);
        given(TicketDataConverter.convert(any(Ticket.class), any(User.class), any(Event.class))).willReturn(ticketData);

        mockMvc.perform(post(BOOK_TICKET)
                        .param("place", "1")
                        .param("category", "STANDARD"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TICKET, ticketData))
                .andExpect(view().name(TICKET));
    }

    @Test
    void bookTicketShouldThrowIllegalStateExceptionIfPlaceBooked() throws Exception {
        when(bookingFacade.bookTicket(anyLong(),anyLong(),anyInt(),any(Ticket.Category.class)))
                .thenThrow(IllegalStateException.class);
        MvcResult result = mockMvc.perform(post(BOOK_TICKET)
                        .param("place", "1")
                        .param("category", "STANDARD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Exception illegalStateException = result.getResolvedException();
        assertTrue(illegalStateException instanceof IllegalStateException);
    }


    @Test
    void cancelTicketShouldReturnVoid() throws Exception {
        when(bookingFacade.cancelTicket(anyLong())).thenReturn(true);
        mockMvc.perform(delete(CANCEL_TICKET))
                .andExpect(status().isNoContent());
    }

    @Test
    void findTicketsByUserShouldReturnListOfTickets() throws Exception {
        when(bookingFacade.getBookedTickets(any(User.class),anyInt(),anyInt())).thenReturn(ticketList);
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(bookingFacade.getEventById(anyLong())).thenReturn(event);
        given(TicketDataConverter.convert(any(Ticket.class), any(User.class), any(Event.class))).willReturn(ticketData);
        mockMvc.perform(get(FIND_TICKETS_BY_USER)
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TICKETS, Arrays.asList(ticketData)))
                .andExpect(view().name(TICKETS_BY_USER));
    }

    @Test
    void findTicketsByEventShouldReturnListOfTickets() throws Exception {
        when(bookingFacade.getBookedTickets(any(Event.class),anyInt(),anyInt())).thenReturn(ticketList);
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(bookingFacade.getEventById(anyLong())).thenReturn(event);
        given(TicketDataConverter.convert(any(Ticket.class), any(User.class), any(Event.class))).willReturn(ticketData);
        mockMvc.perform(get(FIND_TICKETS_BY_EVENT)
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(TICKETS, Arrays.asList(ticketData)))
                .andExpect(view().name(TICKETS_BY_EVENT));
    }
}
