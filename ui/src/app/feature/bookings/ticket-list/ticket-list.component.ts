import { Component, OnInit } from '@angular/core';
import { Ticket } from 'src/app/shared/model/ticket';

@Component({
  selector: 'to-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.scss']
})
export class TicketListComponent implements OnInit {

  tickets: Ticket[];

  constructor() { }

  ngOnInit(): void {
  }

  isEmptyList(): boolean {
    return this.tickets === undefined || this.tickets?.length === 0;
  }

  ticketTrackBy(index: number, ticket: Ticket): number {
    return ticket.id!;
  }
}
