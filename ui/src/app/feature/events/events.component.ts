import { Component, OnInit } from '@angular/core';
import { Event } from '../../shared/model';

const mockEvents: Event[] = [
  {
    id: 0,
    title: "Some title",
    date: "28/01/2023"
  },
  {
    id: 1,
    title: "Some title 1",
    date: "28/02/2023"
  },
]


@Component({
  selector: 'to-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {
  eventList?: Event[];

  constructor() { }

  ngOnInit(): void {
    this.eventList = mockEvents;
  }

  isEmptyEventList(): boolean {
    return this.eventList === undefined || this.eventList?.length === 0;
  }
}
