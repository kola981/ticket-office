package org.kolesnyk.service.impl;

import org.kolesnyk.dao.EventDao;
import org.kolesnyk.dao.TicketDao;
import org.kolesnyk.dao.UserDao;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.TicketImpl;
import org.kolesnyk.service.TicketService;
import org.kolesnyk.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EventDao eventDao;

    public TicketServiceImpl() {
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        List<Ticket> tickets = ticketDao.findAllTickets();

        Optional<Ticket> bookedPlace = tickets.stream()
                .filter(t -> (t.getEventId() == eventId && t.getUserId() == userId && t.getPlace() == place))
                .findFirst();

        if (bookedPlace.isPresent()) {
            LOGGER.error("Place " + place + " is already booked.");
            throw new IllegalStateException("Place " + place + " is already booked.");
        }

        Ticket ticket = new TicketImpl();
        ticket.setId(Generator.generateTicketId());
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setPlace(place);
        ticket.setCategory(category);
        ticketDao.save(ticket);
        return ticket;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.findAllTickets();

        tickets = tickets.stream()
                .filter(t -> t.getUserId() == user.getId())
                .collect(Collectors.toList());

        if (tickets.isEmpty()) {
            return tickets;
        } else {
            List<FatTicket> ticketsData = createFatTicket(tickets);
            tickets = ticketsData.stream()
                    .sorted(new ComparatorByDate())
                    .map(FatTicket::getTicket)
                    .collect(Collectors.toList());

            int from = Math.min(pageSize * pageNum, 1);
            int to = Math.min(from + pageSize, tickets.size());

            return new ArrayList<>(tickets.subList(from-1, to));
        }
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        List<Ticket> tickets = ticketDao.findAllTickets();

        tickets = tickets.stream()
                .filter(t -> t.getEventId() == event.getId())
                .collect(Collectors.toList());

        if (tickets.isEmpty()) {
            return tickets;
        } else {
            List<FatTicket> ticketsData = createFatTicket(tickets);
            tickets = ticketsData.stream()
                    .sorted(new ComparatorByEmail())
                    .map(FatTicket::getTicket)
                    .collect(Collectors.toList());

            int from = Math.min(pageSize * pageNum, 1);
            int to = Math.min(from + pageSize, tickets.size());

            return new ArrayList<>(tickets.subList(from - 1, to));
        }
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketDao.deleteById(ticketId);
    }

    private List<FatTicket> createFatTicket(List<Ticket> tickets) {
        return tickets.stream()
                .map(this::createFatTicket)
                .collect(Collectors.toList());
    }

    private FatTicket createFatTicket(Ticket ticket) {
        FatTicket fatTicket = new FatTicket();

        fatTicket.setTicket(ticket);
        fatTicket.setEvent(eventDao.findById(ticket.getEventId()));
        fatTicket.setUser(userDao.findById(ticket.getUserId()));

        return fatTicket;
    }

    public void setTicketDao(TicketDao ticketDao){
        this.ticketDao = ticketDao;
    }

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void setEventDao(EventDao eventDao){
        this.eventDao = eventDao;
    }

    static class FatTicket {
        private Ticket ticket;
        private Event event;
        private User user;

        public Ticket getTicket() {
            return ticket;
        }

        public void setTicket(Ticket ticket) {
            this.ticket = ticket;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TicketServiceImpl.FatTicket fatTicket = (TicketServiceImpl.FatTicket) o;

            if (!ticket.equals(fatTicket.ticket)) {
                return false;
            }
            if (!event.equals(fatTicket.event)) {
                return false;
            }
            return user.equals(fatTicket.user);
        }

        @Override
        public int hashCode() {
            int result = ticket.hashCode();
            result = 31 * result + event.hashCode();
            result = 31 * result + user.hashCode();
            return result;
        }
    }

    class ComparatorByDate implements Comparator<FatTicket> {
        @Override
        public int compare(FatTicket o1, FatTicket o2) {
            return o2.getEvent().getDate().compareTo(o1.getEvent().getDate());
        }
    }

    class ComparatorByEmail implements Comparator<FatTicket> {
        @Override
        public int compare(FatTicket o1, FatTicket o2) {
            return o1.getUser().getEmail().compareTo(o2.getUser().getEmail());
        }
    }
}
