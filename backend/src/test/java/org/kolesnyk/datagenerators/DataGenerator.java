package org.kolesnyk.datagenerators;

import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.EventImpl;
import org.kolesnyk.model.impl.TicketImpl;
import org.kolesnyk.model.impl.UserImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataGenerator {
    private static final String PREFIX_EVENT = "event:";
    private static final String PREFIX_TICKET = "ticket:";
    private static final String PREFIX_USER = "user:";

    private static final String USER_1 = "4;AAA;a@a";
    private static final String USER_2 = "5;BBB;ba@ba";
    private static final String USER_3 = "6;CCC;bb@bb";

    private static final String EVENT_1 = "4;Concert;2021-12-15 21:00";
    private static final String EVENT_2 = "5;Movie;2021-10-10 20:00";
    private static final String EVENT_3 = "6;Language classes;2021-12-15 19:00";

    private static final String TICKET_1 = "1;4;4;1";
    private static final String TICKET_2 = "2;4;6;1";
    private static final String TICKET_3 = "3;5;5;1";
    private static final String TICKET_4 = "3;6;5;2";

    private static final Event event1 = generateEvent(EVENT_1);
    private static final Event event2 = generateEvent(EVENT_2);
    private static final Event event3 = generateEvent(EVENT_3);

    private static final User user1 = generateUser(USER_1);
    private static final User user2 = generateUser(USER_2);
    private static final User user3 = generateUser(USER_3);

    private static final Ticket ticket1 = generateTicket(TICKET_1);
    private static final Ticket ticket2 = generateTicket(TICKET_2);
    private static final Ticket ticket3 = generateTicket(TICKET_3);
    private static final Ticket ticket4 = generateTicket(TICKET_4);

    private static Map<String, Object> map = new HashMap<>();

    public static Set<Map.Entry<String, Object>> generateEntries() {
        map.put(PREFIX_EVENT+event1.getId(), event1);
        map.put(PREFIX_EVENT+event2.getId(), event2);
        map.put(PREFIX_EVENT+event3.getId(), event3);
        map.put(PREFIX_USER+user1.getId(), user1);
        map.put(PREFIX_USER+user2.getId(), user2);
        map.put(PREFIX_USER+user3.getId(), user3);
        map.put(PREFIX_TICKET+ticket1.getId(), ticket1);
        map.put(PREFIX_TICKET+ticket2.getId(), ticket2);
        map.put(PREFIX_TICKET+ticket3.getId(), ticket3);
        map.put(PREFIX_TICKET+ticket4.getId(), ticket4);
        return map.entrySet();
    }

    public static List<User> generateUserList(){
        return Arrays.asList(user1, user2, user3);
    }

    public static List<Event> generateEventList(){
        return Arrays.asList(event1, event2, event3);
    }

    public static List<Ticket> generateTicketList(){
        return Arrays.asList(ticket1, ticket2, ticket3, ticket4);
    }

    public static User generateUser() {
        return user1;
    }

    private static User generateUser(String user_string) {
        String[] userData = user_string.split(";");

        User user = new UserImpl();
        user.setId(Long.valueOf(userData[0]));
        user.setName(userData[1]);
        user.setEmail(userData[2]);

        return user;
    }

    public static Event generateEvent(){
        return event1;
    }

    private static Event generateEvent(String event_string) {
        String[] eventData = event_string.split(";");

        Event event = new EventImpl();
        event.setId(Long.valueOf(eventData[0]));
        event.setTitle(eventData[1]);
        event.setDate(LocalDateTime.parse(eventData[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return event;
    }

    public static Ticket generateTicket(){
        return ticket1;
    }

    private static Ticket generateTicket(String ticket_string) {
        String[] ticketData = ticket_string.split(";");

        Ticket ticket = new TicketImpl();
        ticket.setId(Long.valueOf(ticketData[0]));
        ticket.setUserId(Long.valueOf(ticketData[1]));
        ticket.setEventId(Long.valueOf(ticketData[2]));
        ticket.setPlace(Integer.valueOf(ticketData[3]));
        ticket.setCategory(Ticket.Category.STANDARD);

        return ticket;
    }

    public static String getPrefixEvent(){
        return PREFIX_EVENT;
    }

    public static String getPrefixUser(){
        return PREFIX_USER;
    }

    public static String getPrefixTicket(){
        return PREFIX_TICKET;
    }
}
