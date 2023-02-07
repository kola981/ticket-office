import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Event } from 'src/app/shared/model';
import { Router } from '@angular/router';
import { EventService } from 'src/app/shared/services/event.service';


const mockEvents: Event[] = [
  {
    id: 1,
    title: "AAA",
    date: "20/1/2023"
  },
];

@Component({
  selector: 'to-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.scss']
})
export class BookingsComponent implements AfterViewInit {
  dataSource: MatTableDataSource<Event>;
  displayedColumns: string[] = ['id', 'title', 'date', 'actions'];

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor (private router: Router, private eventService: EventService) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.eventService.userEvents);
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  onView(event: Event): void {
    this.router.navigate(['bookings', 'event', event.id]);
  }
}
