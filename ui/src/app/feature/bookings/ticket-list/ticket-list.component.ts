import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/shared/model';
import { Ticket } from 'src/app/shared/model/ticket';
import { EventService } from 'src/app/shared/services/event.service';
import { ActivatedRoute } from '@angular/router';

const mockTickets: Ticket[] = [{
  id: 1,
  eventId: 1,
  userId: 2,
  category: "STANDART",
  place: 12
},
{
  id: 2,
  eventId: 1,
  userId: 2,
  category: "PREMIUM",
  place: 4
}
];

@Component({
  selector: 'to-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.scss']
})
export class TicketListComponent implements OnInit {
  tickets: Ticket[];
  event: Event;

  constructor (private route: ActivatedRoute, private eventService: EventService) {}

  ngOnInit(): void {
    this.tickets = mockTickets;
    let id: number;

    this.route.params.subscribe(params => {
      id = params['eventId'];
    });

    this.event = this.eventService.findById(id!);
  }

  isEmptyList(): boolean {
    return this.tickets === undefined || this.tickets?.length === 0;
  }

  ticketTrackBy(index: number, ticket: Ticket): number {
    return ticket.id!;
  }
}
