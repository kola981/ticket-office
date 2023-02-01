import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent, PageNotFoundComponent } from './components';
import { MaterialModule } from './material/material.module';

@NgModule({
  declarations: [
    HeaderComponent,
    PageNotFoundComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [ 
    HeaderComponent,
    PageNotFoundComponent
  ]
})
export class SharedModule { }
