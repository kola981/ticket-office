import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookingsComponent } from './bookings.component';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { TicketListComponent } from './ticket-list/ticket-list.component';
import { TicketComponent } from './ticket/ticket.component';


@NgModule({
  declarations: [
    BookingsComponent,
    TicketListComponent,
    TicketComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    BookingsComponent,
    TicketListComponent
  ],
})
export class BookingsModule { }
