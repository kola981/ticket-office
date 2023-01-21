package org.kolesnyk.utils;

import org.apache.logging.log4j.util.Strings;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.springframework.stereotype.Component;

@Component
public class NamespaceResolver {
    private static final String TICKET = "ticket:";
    private static final String EVENT = "event:";
    private static final String USER = "user:";

    public String toNsKey(Object data) {
        if (data instanceof EventImpl) {
            return EVENT+((Event) data).getId();
        }
        else if (data instanceof Ticket) {
            return TICKET+((Ticket) data).getId();
        }
        else if (data instanceof User) {
            return USER+((User) data).getId();
        }

        return Strings.EMPTY;
    }

    public boolean isTicket(String str) {
        return str.startsWith(TICKET);
    }

    public boolean isEvent(String str) {
        return str.startsWith(EVENT);
    }

    public boolean isUser(String str) {
        return str.startsWith(USER);
    }
}
