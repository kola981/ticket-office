package org.kolesnyk.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneratorTest {

    @BeforeAll
    static void init(){
        Generator.clear();
    }

    @Test
    void generateUserIdShouldReturnSequenceStartingFromZero(){
        for (int i=0; i<3; i++)
            assertEquals(i, Generator.generateUserId());
    }

    @Test
    void generateEventIdShouldReturnSequenceStartingFromZero(){
        for (int i=0; i<3; i++)
            assertEquals(i, Generator.generateEventId());
    }

    @Test
    void generateTicketIdShouldReturnSequenceStartingFromZero(){
        for (int i=0; i<3; i++)
            assertEquals(i, Generator.generateTicketId());
    }

    @AfterAll
    static void clear(){
        Generator.clear();
    }
}
