package org.kolesnyk.dao;

import org.kolesnyk.model.Ticket;

import java.util.List;

public interface TicketDao {

    Ticket save(Ticket ticket);

    List<Ticket> findAllTickets();

    boolean deleteById(long ticketId);

}
