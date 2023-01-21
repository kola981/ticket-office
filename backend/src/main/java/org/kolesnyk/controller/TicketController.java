package org.kolesnyk.controller;


import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.TicketData;
import org.kolesnyk.model.impl.UserImpl;
import org.kolesnyk.utils.TicketDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tickets")
public class TicketController {

    private final BookingFacade bookingFacade;

    @Autowired
    public TicketController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @PostMapping("book/{userId}/{eventId}")
    public ModelAndView bookTicket(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId,
                                   @RequestParam(value = "place") int place,
                                   @RequestParam(value = "category") String category) {

        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.valueOf(category));

        Map<String, Object> params = new HashMap<>();
        params.put("ticket", convert(ticket));
        return new ModelAndView("ticket", params);
    }


    @GetMapping("/user/{id}")
    public ModelAndView getBookedTickets(@PathVariable("id") Long userId,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "size") int pageSize,
                                         @RequestParam(value = "num") int pageNum) {
        User user = new UserImpl();
        user.setId(userId);
        user.setEmail(email);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(user, pageSize, pageNum);
        List<TicketData> ticketDataList = ticketList.stream()
                .map(this::convert)
                .collect(Collectors.toList());

        Map<String, Object> params = new HashMap<>();
        params.put("tickets", ticketDataList);
        return new ModelAndView("ticketsByUser", params);
    }

    @GetMapping("/event/{eventId}")
    public ModelAndView getBookedTickets(@PathVariable("eventId") Long eventId,
                                         @RequestParam(value = "size") int pageSize,
                                         @RequestParam(value = "num") int pageNum) {
        Event event = new EventImpl();
        event.setId(eventId);
        List<Ticket> ticketList = bookingFacade.getBookedTickets(event, pageSize, pageNum);
        List<TicketData> ticketDataList = ticketList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
        Map<String, Object> params = new HashMap<>();
        params.put("tickets", ticketDataList);
        return new ModelAndView("ticketsByEvent", params);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable("id") Long ticketId) {
        bookingFacade.cancelTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    private TicketData convert(Ticket ticket) {
        User user = bookingFacade.getUserById(ticket.getUserId());
        Event event = bookingFacade.getEventById(ticket.getEventId());

        return TicketDataConverter.convert(ticket, user, event);
    }
}
