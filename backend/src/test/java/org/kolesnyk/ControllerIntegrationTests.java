package org.kolesnyk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kolesnyk.controller.EventControllerTest.CREATE_EVENT;
import static org.kolesnyk.controller.EventControllerTest.EVENTS_BY_DAY;
import static org.kolesnyk.controller.EventControllerTest.EVENTS_BY_TITLE;
import static org.kolesnyk.controller.UserControllerTest.CREATE_USER;
import static org.kolesnyk.controller.UserControllerTest.FIND_BY_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
public class ControllerIntegrationTests {
    private static final String USER_URI = "/user/";
    private static final String EVENT_URI = "/events/";
    private static final String TICKET_URI = "/tickets/";
    private static final String BOOK_TICKET_URI = "/tickets/book/";
    public static final String FIND_TICKETS_BY_USER = "/tickets/user/";
    public static final String FIND_TICKETS_BY_EVENT = "/tickets/event/";

    private static final String PLACE = "10";
    private static final String CATEGORY = "STANDARD";

    private static MockMvc mockMvc;
    private static DocumentBuilder builder;
    private static XPath xpath;

    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    static void setup(WebApplicationContext wac) throws ParserConfigurationException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        XPathFactory xpathfactory = XPathFactory.newInstance();
        xpath = xpathfactory.newXPath();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    @Test
    @Order(1)
    void bookTicketIntegrationTest() throws Exception {
        String eventId = "0";
        String userId = "0";

        MvcResult result = mockMvc.perform(get(EVENT_URI + eventId))
                .andReturn();

        String title = getResultFromXPath(result, "//td[2]");
        String date = getResultFromXPath(result, "//td[3]");

        result = mockMvc.perform(get(USER_URI + userId))
                .andReturn();

        String name = getResultFromXPath(result, "//td[2]");
        String email = getResultFromXPath(result, "//td[3]");

        mockMvc.perform(post(BOOK_TICKET_URI + 0 + "/" + 0)
                        .param("place", PLACE)
                        .param("category", CATEGORY))
                .andExpect(xpath("//table[1]/tbody/tr/td[1]").string(name))
                .andExpect(xpath("//table[1]/tbody/tr/td[2]").string(email))
                .andExpect(xpath("//table[2]/tbody/tr/td[2]").string(date))
                .andExpect(xpath("//table[2]/tbody/tr/td[3]").string(title))
                .andExpect(xpath("//table[2]/tbody/tr/td[4]").string(PLACE))
                .andExpect(xpath("//table[2]/tbody/tr/td[5]").string(CATEGORY));

        mockMvc.perform(get("/tickets/xml/"))
                .andExpect(status().isNoContent());

        result = mockMvc.perform(get(EVENTS_BY_TITLE)
                        .param("title", "java")
                        .param("size", "5")
                        .param("num", "1"))
                .andExpect(xpath("(//td)[2]").string("Exciting java for android classes"))
                .andExpect(xpath("(//td)[3]").string("2021-12-31T16:00"))
                .andReturn();

        eventId = getResultFromXPath(result, "//td[1]");

        mockMvc.perform(get(FIND_TICKETS_BY_EVENT + eventId)
                        .param("size", "5")
                        .param("num", "1"))
                .andExpect(xpath("//table[1]/tbody/tr/td[1]")
                        .string("Exciting java for android classes"))
                .andExpect(xpath("//table[1]/tbody/tr/td[2]").string("2021-12-31T16:00"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[2]").string("abc@abc.com"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[3]").string("Vasja Pupkin"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[4]").string("1"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[5]").string("PREMIUM"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[2]").string("google@google.com"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[3]").string("The Google"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[4]").string("2"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[5]").string("PREMIUM"));

        result = mockMvc.perform(get(FIND_BY_EMAIL)
                        .param("email", "abc@abc.com"))
                .andReturn();

        userId = getResultFromXPath(result, "//td[1]");

        result = mockMvc.perform(get(FIND_TICKETS_BY_USER+userId)
                        .param("size", "5")
                        .param("num", "1"))
                .andExpect(xpath("//table[1]/tbody/tr/td[1]").string("Vasja Pupkin"))
                .andExpect(xpath("//table[1]/tbody/tr/td[2]").string("abc@abc.com"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[2]").string("2021-12-31T16:00"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[3]").string("Exciting java for android classes"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[4]").string("1"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[5]").string("PREMIUM"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[2]").string("2021-12-12T19:00"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[3]").string("The best concert"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[4]").string("1"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[5]").string("STANDARD"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[2]").string("2021-12-12T19:00"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[3]").string("The best concert"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[4]").string("10"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[5]").string("STANDARD"))
                .andExpect(xpath("//table[2]/tbody/tr[4]/td[2]").string("2021-12-12T19:00"))
                .andExpect(xpath("//table[2]/tbody/tr[4]/td[3]").string("The best concert"))
                .andExpect(xpath("//table[2]/tbody/tr[4]/td[4]").string("2"))
                .andExpect(xpath("//table[2]/tbody/tr[4]/td[5]").string("STANDARD"))
                .andReturn();

        String ticketId = getResultFromXPath(result, "//table[2]/tbody/tr[3]/td[1]");

        mockMvc.perform(delete(TICKET_URI+ticketId));

        mockMvc.perform(get(FIND_TICKETS_BY_USER+userId)
                        .param("size", "5")
                        .param("num", "1"))
                .andDo(print())
                .andExpect(xpath("//table[1]/tbody/tr/td[1]").string("Vasja Pupkin"))
                .andExpect(xpath("//table[1]/tbody/tr/td[2]").string("abc@abc.com"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[2]").string("2021-12-31T16:00"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[3]").string("Exciting java for android classes"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[4]").string("1"))
                .andExpect(xpath("//table[2]/tbody/tr[1]/td[5]").string("PREMIUM"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[2]").string("2021-12-12T19:00"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[3]").string("The best concert"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[4]").string("1"))
                .andExpect(xpath("//table[2]/tbody/tr[2]/td[5]").string("STANDARD"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[2]").string("2021-12-12T19:00"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[3]").string("The best concert"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[4]").string("2"))
                .andExpect(xpath("//table[2]/tbody/tr[3]/td[5]").string("STANDARD"));
    }

    @Test
    @Order(2)
    void createAndModifyAndDeleteEvent() throws Exception {

        mockMvc.perform(post(CREATE_EVENT)
                        .param("title", "Some event")
                        .param("date", "2008-01-01 18:46"))
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(get(EVENTS_BY_DAY)
                        .param("day", "2008-01-01")
                        .param("size", "2")
                        .param("num", "1"))
                .andExpect(xpath("(//td)[2]").string("Some event"))
                .andExpect(xpath("(//td)[3]").string("2008-01-01T18:46"))
                .andReturn();

        String eventId = getResultFromXPath(result, "(//td)[1]");

        mockMvc.perform(get(EVENT_URI + eventId))
                .andExpect(xpath("(//td)[1]").string(eventId))
                .andExpect(xpath("(//td)[2]").string("Some event"))
                .andExpect(xpath("(//td)[3]").string("2008-01-01T18:46"));


        mockMvc.perform(put(EVENT_URI + eventId)
                        .param("title", "The best concert")
                        .param("date", "2008-01-01 18:46"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(EVENT_URI + eventId))
                .andExpect(xpath("(//td)[1]").string(eventId))
                .andExpect(xpath("(//td)[2]").string("The best concert"))
                .andExpect(xpath("(//td)[3]").string("2008-01-01T18:46"));


        mockMvc.perform(delete(EVENT_URI + eventId))
                .andExpect(status().isNoContent());

        result = mockMvc.perform(get(EVENT_URI + eventId))
                .andDo(print())
                .andReturn();

        String table = getResultFromXPath(result, "//table");
        assertTrue(table.isEmpty());
    }

    @Test
    @Order(3)
    void createAndModifyAndDeleteUser() throws Exception {

        mockMvc.perform(post(CREATE_USER)
                        .param("name", "Andrey")
                        .param("email", "abc@google.com"))
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(get(FIND_BY_EMAIL)
                        .param("email", "abc@google.com"))
                .andExpect(xpath("(//td)[2]").string("Andrey"))
                .andExpect(xpath("(//td)[3]").string("abc@google.com"))
                .andReturn();

        String userId = getResultFromXPath(result, "(//td)[1]");

        mockMvc.perform(get(USER_URI + userId))
                .andExpect(xpath("(//td)[1]").string(userId))
                .andExpect(xpath("(//td)[2]").string("Andrey"))
                .andExpect(xpath("(//td)[3]").string("abc@google.com"));

        mockMvc.perform(put(USER_URI + userId)
                        .param("name", "Masha")
                        .param("email", "abc@google.com"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(USER_URI + userId))
                .andExpect(xpath("(//td)[1]").string(userId))
                .andExpect(xpath("(//td)[2]").string("Masha"))
                .andExpect(xpath("(//td)[3]").string("abc@google.com"));

        mockMvc.perform(delete(USER_URI + userId))
                .andExpect(status().isNoContent());

        result = mockMvc.perform(get(USER_URI + userId))
                .andDo(print())
                .andReturn();

        String table = getResultFromXPath(result, "//table");
        assertTrue(table.isEmpty());
    }

    private String getResultFromXPath(MvcResult result, String xpathString) throws Exception {
        String html = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        XPathExpression expr = xpath.compile(xpathString);
        Document doc = builder.parse(new ByteArrayInputStream(html.getBytes()));
        return expr.evaluate(doc);
    }

}
