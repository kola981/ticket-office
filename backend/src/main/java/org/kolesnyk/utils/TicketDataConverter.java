package org.kolesnyk.utils;

import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.TicketData;

public class TicketDataConverter {

    public static TicketData convert(Ticket ticket, User user, Event event) {

        TicketData ticketData = new TicketData();
        ticketData.setId(ticket.getId());
        ticketData.setName(user.getName());
        ticketData.setEmail(user.getEmail());
        ticketData.setTitle(event.getTitle());
        ticketData.setDate(event.getDate());
        ticketData.setPlace(ticket.getPlace());
        ticketData.setCategory(ticket.getCategory());

        return ticketData;
    }
}
