import { Injectable } from '@angular/core';
import { Event } from '../model';

const mockEvents: Event[] = [
  {
    id: 1,
    title: "AAA",
    date: "20/1/2023"
  },
];


@Injectable({
  providedIn: 'root'
})
export class EventService {
  events: Event[] = [];
  userEvents: Event[] = mockEvents;

  constructor() { }

  findById(id: number): Event {
    return mockEvents[0];
  }
}
