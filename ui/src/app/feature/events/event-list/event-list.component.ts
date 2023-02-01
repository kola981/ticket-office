import { Component, OnInit, Input } from '@angular/core';
import { Event } from '../../../shared/model';

@Component({
  selector: 'to-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  @Input() events?: Event[];

  constructor() { }

  ngOnInit(): void {
  }

  eventTrackBy(index: number, event: Event): number {
    return event.id!;
  }
}
