package org.kolesnyk.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.impl.EventImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
    public static final String EVENT_BY_ID = "/events/0";
    public static final String EVENTS_BY_TITLE = "/events/find/title";
    public static final String EVENTS_BY_DAY = "/events/find/day";
    public static final String CREATE_EVENT = "/events/create";

    private static final String EVENT = "event";
    private static final String EVENTS = "events";
    private Event event = new EventImpl();
    private final List<Event> eventList = Arrays.asList(event);

    @Mock
    private BookingFacade bookingFacade;

    @InjectMocks
    private EventController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        event.setId(0);
    }

    @Test
    void getEventByIdShouldReturnEvent() throws Exception {
        when(bookingFacade.getEventById(0)).thenReturn(event);
        mockMvc.perform(get(EVENT_BY_ID))
                .andExpect(status().isOk())
                .andExpect(model().attribute(EVENT, event))
                .andExpect(view().name(EVENT));
    }

    @Test
    void getEventsByTitleShouldReturnListOfEvents() throws Exception {
        when(bookingFacade.getEventsByTitle(anyString(), anyInt(),anyInt())).thenReturn(eventList);
        mockMvc.perform(get(EVENTS_BY_TITLE).param("title", "aaa")
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(EVENTS, eventList))
                .andExpect(view().name(EVENTS));
    }


    @Test
    void getEventsForDayShouldReturnListOfEvents() throws Exception {
        when(bookingFacade.getEventsForDay(any(LocalDateTime.class), anyInt(),anyInt())).thenReturn(eventList);
        mockMvc.perform(get(EVENTS_BY_DAY).param("day", "2008-01-01")
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(EVENTS, eventList))
                .andExpect(view().name(EVENTS));
    }

    @Test
    void createEventByIdShouldReturnVoid() throws Exception {
        mockMvc.perform(post(CREATE_EVENT)
                        .param("title", "aaa")
                        .param("date", "2008-01-01 18:46"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateEventShouldReturnVoid() throws Exception {
        mockMvc.perform(put(EVENT_BY_ID)
                        .param("title", "aaa")
                        .param("date", "2008-01-01 18:46"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEventShouldReturnVoid() throws Exception {
        mockMvc.perform(delete(EVENT_BY_ID))
                .andExpect(status().isNoContent());
    }

}
