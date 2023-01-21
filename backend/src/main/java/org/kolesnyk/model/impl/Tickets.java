package org.kolesnyk.model.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.kolesnyk.model.Ticket;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("tickets")
public class Tickets {

    @XStreamImplicit(itemFieldName = "ticket")
    private List<TicketImpl> tickets = new ArrayList<>();

    public List<TicketImpl> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketImpl> tickets) {
        this.tickets = tickets;
    }
}
