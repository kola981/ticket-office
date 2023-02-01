
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Event } from '../../../shared/model';

@Component({
  selector: 'to-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent {
  @Input() event?: Event;
  @Output() eventBook: EventEmitter<number> = new EventEmitter();

  onClick(): void {
    this.eventBook.emit(this.event?.id);
  }
}
