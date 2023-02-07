import { Component, Input, OnInit } from '@angular/core';
import { Ticket } from 'src/app/shared/model/ticket';

@Component({
  selector: 'to-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent {
  @Input() ticket: Ticket;
  @Input() eventDate: string;
  @Input() eventName: string;

  onClick(): void {
    console.log(this.ticket.id);
  }

  onCancel(): void {
    console.log(`Ticket ${this.ticket.id} cancelled`);
  }
}
