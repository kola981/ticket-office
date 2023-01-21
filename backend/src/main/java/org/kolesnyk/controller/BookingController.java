package org.kolesnyk.controller;

import org.kolesnyk.facade.BookingFacade;
import org.kolesnyk.model.Event;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.User;
import org.kolesnyk.model.impl.TicketData;
import org.kolesnyk.model.impl.UserImpl;
import org.kolesnyk.repository.Storage;
import org.kolesnyk.utils.TicketDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tickets/")
public class BookingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);

    @Autowired
    private final TemplateEngine engine;

    @Autowired
    private final BookingFacade facade;

    public BookingController(TemplateEngine templateEngine, BookingFacade bookingFacade) {
        engine = templateEngine;
        facade = bookingFacade;
    }

    @GetMapping(path = "/user/{id}", headers = {"accept=application/pdf"})
    public ResponseEntity<InputStreamResource> getBookedTickets(@PathVariable("id") Long userId,
                                                                @RequestParam(value = "email", required = false)
                                                                String email,
                                                                @RequestParam(value = "size") int pageSize,
                                                                @RequestParam(value = "num") int pageNum) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        User user = new UserImpl();
        user.setId(userId);
        user.setEmail(email);
        List<Ticket> ticketList = facade.getBookedTickets(user, pageSize, pageNum);
        List<TicketData> ticketDataList = ticketList.stream()
                .map(this::convert)
                .collect(Collectors.toList());


        Map<String, Object> params = new HashMap<>();
        params.put("tickets", ticketDataList);
        String templateAsString = processTemplate("ticketsByUser", params);
        ByteArrayInputStream is = createPdf(templateAsString, os);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=booking.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(is));
    }

    private String processTemplate(String templateName, Map map) {
        Context ctx = new Context();
        if (map != null) {
            Iterator itMap = map.entrySet().iterator();
            while (itMap.hasNext()) {
                Map.Entry pair = (Map.Entry) itMap.next();
                ctx.setVariable(pair.getKey().toString(), pair.getValue());
            }
        }

        String processedHtml = engine.process(templateName, ctx);

        return processedHtml;
    }

    private ByteArrayInputStream createPdf(String processedHtml, OutputStream os) {

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
    }

    private TicketData convert(Ticket ticket) {
        User user = facade.getUserById(ticket.getUserId());
        Event event = facade.getEventById(ticket.getEventId());

        return TicketDataConverter.convert(ticket, user, event);
    }


    @GetMapping("xml")
    public ResponseEntity<Void> loadFromXml() {
        facade.preloadTickets();
        return ResponseEntity.noContent().build();
    }

}
