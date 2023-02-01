import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { HttpClient } from '@angular/common/http';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'to-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  appName: string = "ticket office";
  loginText: string = "login";
  logoutText: string = "Logout";

  isLogged: boolean = false;

  constructor (private router: Router, public auth: AuthService) {}

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.auth.logout();
  }
}
