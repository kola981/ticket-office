import { NgModule } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { EventsComponent } from './feature/events/events.component';
import { PageNotFoundComponent } from './shared/components/page-not-found/page-not-found.component';
import { LoginComponent } from './auth/login/login.component';
import { BookingsComponent } from './feature/bookings/bookings.component';
import { TicketListComponent } from './feature/bookings/ticket-list/ticket-list.component';

const routes: Routes = [
  {
    path: 'home',
    component: EventsComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'bookings/event/:eventId',
    component: TicketListComponent

  },
  {
    path: 'bookings',
    component: BookingsComponent
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    // The router will match this route if the URL requested
    // doesn't match any paths for routes defined in our configuration
    path: '**',
    component: PageNotFoundComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  constructor(router: Router) {
    const replacer = (key: string, value: any): string =>
    typeof value === 'function' ? value.name : value;
    console.log('Routes: ', JSON.stringify(router.config, replacer, 2));
   }
 }
