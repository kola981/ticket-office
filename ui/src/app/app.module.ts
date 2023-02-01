import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {  HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { EventsModule } from './feature/events/events.module';
import { AuthModule } from './auth/auth.module';

import { MAT_DATE_LOCALE } from '@angular/material/core';
import { BookingsModule } from './feature/bookings/bookings.module';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AuthModule,
    EventsModule,
    BookingsModule,
    HttpClientModule,
    SharedModule,

    //Routing
    AppRoutingModule
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'en-GB'},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
