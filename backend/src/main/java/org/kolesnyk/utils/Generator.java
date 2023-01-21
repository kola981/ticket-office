package org.kolesnyk.utils;

public class Generator {
    private static long users = 0L;
    private static long events = 0L;
    private static long tickets = 0L;

    public static long generateUserId(){
        return users++;
    }

    public static long generateEventId(){
        return events++;
    }

    public static long generateTicketId(){
        return tickets++;
    }

    public static void clear(){
        users = 0L;
        events = 0L;
        tickets = 0L;
    }
}
