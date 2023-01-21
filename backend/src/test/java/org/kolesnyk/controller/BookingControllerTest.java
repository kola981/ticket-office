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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    public static final String USER_ID = "/tickets/user/0";
    public static final String PRELOAD_TICKETS = "/tickets/xml";

    private static final String TICKETS = "tickets";
    private static final String TICKETS_BY_USER = "ticketsByUser";

    private final Ticket ticket = new TicketImpl();
    private final Event event = new EventImpl();
    private final User user = new UserImpl();
    private final TicketData ticketData = new TicketData();
    private final List<Ticket> ticketList = Arrays.asList(ticket);

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private BookingController controller;

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
    void findTicketsByUserShouldReturnListOfTickets() throws Exception {
        when(bookingFacade.getBookedTickets(any(User.class),anyInt(),anyInt())).thenReturn(ticketList);
        when(bookingFacade.getUserById(anyLong())).thenReturn(user);
        when(bookingFacade.getEventById(anyLong())).thenReturn(event);
        given(TicketDataConverter.convert(any(Ticket.class), any(User.class), any(Event.class))).willReturn(ticketData);
        mockMvc.perform(get(USER_ID)
                        .param("size", "2")
                        .param("num", "1")
                        .header("accept", "application/pdf"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void loadFromXmlShouldReturn200OK() throws Exception {
        doNothing().when(bookingFacade).preloadTickets();
        mockMvc.perform(get(PRELOAD_TICKETS))
                .andExpect(status().isNoContent());
    }
}
