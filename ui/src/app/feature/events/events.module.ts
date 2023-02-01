import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventsComponent } from './events.component';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { EventListComponent } from './event-list/event-list.component';
import { EventComponent } from './event/event.component';


@NgModule({
  declarations: [
    EventsComponent,
    EventListComponent,
    EventComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    EventsComponent
  ]
})
export class EventsModule { }
