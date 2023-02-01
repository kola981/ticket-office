import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Event } from 'src/app/shared/model';


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
  dataSource = new MatTableDataSource(mockEvents);
  displayedColumns: string[] = ['id', 'title', 'date', 'actions'];

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

}
