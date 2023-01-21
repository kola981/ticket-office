package org.kolesnyk.service.impl;

import com.thoughtworks.xstream.security.AnyTypePermission;
import org.kolesnyk.dao.TicketDao;
import org.kolesnyk.handler.CustomExceptionResolver;
import org.kolesnyk.model.Ticket;
import org.kolesnyk.model.impl.Tickets;
import org.kolesnyk.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class XmlReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionResolver.class);

    private final Unmarshaller unmarshaller;
    private final TicketDao ticketDao;
    //private final TransactionTemplate transactionTemplate;

    @Value("${data.xml}")
    private String ticketsXml;

    @Autowired
    public XmlReader(XStreamMarshaller marshaller, TicketDao ticketDao) { //, PlatformTransactionManager transactionManager
        Map<String, Class> map = new HashMap<>();
        map.put("ticket", Ticket.class);
        map.put("tickets", Tickets.class);
        marshaller.setAliases(map);
        marshaller.setAutodetectAnnotations(true);
        marshaller.setTypePermissions(AnyTypePermission.ANY);
        this.unmarshaller = marshaller;

        this.ticketDao = ticketDao;
        // this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void preload() {
        //  transactionTemplate.execute(new TransactionCallbackWithoutResult() {
        //      protected void doInTransactionWithoutResult(TransactionStatus status) {
        try (FileInputStream is = new FileInputStream(ticketsXml)) {
            Tickets tickets = (Tickets) unmarshaller.unmarshal(new StreamSource(is));
            List<Ticket> ticketList = tickets.getTickets()
                    .stream()
                    .map(e->(Ticket)e)
                    .collect(Collectors.toList());
            ticketList.stream()
                    .forEach(t -> {
                        t.setId(Generator.generateTicketId());
                        ticketDao.save(t);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.toString());
        }
        //    }
        // });
    }

}
