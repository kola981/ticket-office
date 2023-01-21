package org.kolesnyk.utils;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.TicketImpl;
import org.kolesnyk.model.impl.UserImpl;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class NamespaceResolverTest {

    private static final String PREFIX_EVENT = "event:1";
    private static final String PREFIX_TICKET = "ticket:2";
    private static final String PREFIX_USER = "user:3";

    private final NamespaceResolver resolver = new NamespaceResolver();

    Event event = new EventImpl();
    Ticket ticket = new TicketImpl();
    User user = new UserImpl();

    @Test
    void toNsKeyShouldReturnCorrectPrefix(){
        event.setId(1L);
        ticket.setId(2L);
        user.setId(3L);

        String result1 = resolver.toNsKey(event);
        String result2 = resolver.toNsKey(ticket);
        String result3 = resolver.toNsKey(user);

        assertAll(
                () -> assertEquals(PREFIX_EVENT, result1),
                () -> assertEquals(PREFIX_TICKET, result2),
                () -> assertEquals(PREFIX_USER, result3)
        );
    }

    @TestFactory
    Collection<DynamicTest> isTypeShouldReturnTrueIfStringStartsWithCorrectPrefixOtherwiseFalse() {
        return Arrays.asList(
                dynamicTest("True if prefix event", () -> assertTrue(resolver.isEvent(PREFIX_EVENT))),
                dynamicTest("False if prefix ticket", () -> assertFalse(resolver.isEvent(PREFIX_TICKET))),
                dynamicTest("True if prefix ticket", () -> assertTrue(resolver.isTicket(PREFIX_TICKET))),
                dynamicTest("False if prefix user", () -> assertFalse(resolver.isTicket(PREFIX_USER))),
                dynamicTest("True if prefix user", () -> assertTrue(resolver.isUser(PREFIX_USER))),
                dynamicTest("False if prefix event", () -> assertFalse(resolver.isUser(PREFIX_EVENT)))
        );
    }
}
