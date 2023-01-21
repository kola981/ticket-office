package org.kolesnyk.controller;


import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.impl.EventImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("events")
public class EventController {

    private BookingFacade bookingFacade;

    @Autowired
    public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("{id}")
    public ModelAndView getEventById(@PathVariable("id") long eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put("event", bookingFacade.getEventById(eventId));
        return new ModelAndView("event", params);
    }

    @GetMapping("/find/title")
    public ModelAndView getEventsByTitle(@RequestParam(value = "title") String title,
                                         @RequestParam(value = "size") int pageSize,
                                         @RequestParam(value = "num") int pageNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("events", bookingFacade.getEventsByTitle(title, pageSize, pageNum));
        return new ModelAndView("events", params);
    }

    @GetMapping("/find/day")
    public ModelAndView getEventsForDay(@RequestParam(value = "day") String day,
                                        @RequestParam(value = "size") int pageSize,
                                        @RequestParam(value = "num") int pageNum) {

        LocalDateTime date = LocalDateTime.of(LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalTime.MIN);
        Map<String, Object> params = new HashMap<>();
        params.put("events", bookingFacade.getEventsForDay(date, pageSize, pageNum));
        return new ModelAndView("events", params);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createEvent(@RequestParam(value = "title") String title,
                                            @RequestParam(value = "date") String date) {
        Event event = new EventImpl();
        event.setTitle(title);
        event.setDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        bookingFacade.createEvent(event);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable("id") Long eventId,
                                            @RequestParam(value = "title") String title,
                                            @RequestParam(value = "date") String date) {
        Event event = new EventImpl();
        event.setId(eventId);
        event.setTitle(title);
        event.setDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        bookingFacade.updateEvent(event);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") long eventId) {
        bookingFacade.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

}
