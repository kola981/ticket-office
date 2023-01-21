package org.kolesnyk.dao.impl;

import org.kolesnyk.dao.TicketDao;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.repository.Storage;
import org.kolesnyk.utils.NamespaceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TicketDaoImpl implements TicketDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDaoImpl.class);
    private static final String PREFIX = "ticket:";

    @Autowired
    private Storage storage;
    @Autowired
    private NamespaceResolver resolver;

    public void setStorage(Storage storage){
        this.storage = storage;
    }

    public void setResolver(NamespaceResolver resolver){
        this.resolver = resolver;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return (Ticket) storage.save(PREFIX+ticket.getId(), ticket);
    }

    @Override
    public List<Ticket> findAllTickets() {
        Set<Map.Entry<String, Object>> entries = storage.entries();
        return entries.stream()
                .filter(e -> resolver.isTicket(e.getKey()))
                .map(u -> (Ticket) u.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(long ticketId) {
        return storage.removeById(PREFIX+ticketId);
    }
}
