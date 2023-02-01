import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Login } from '../../shared/model';
import { AuthService } from '../auth.service';

@Component({
  selector: 'to-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @Input() email: string = "";
  @Input() password: string = "";

  constructor(private router: Router, private auth: AuthService) { }

  cancel(): void {
    this.router.navigate(['/home']);
  }

  login() {
    console.log(`LOGIN ${this.email} - ${this.password}`);
    
    const login: Login = {
      username: this.email,
      password: this.password
    }
    
    if (this.auth.login(login)) {
      this.router.navigate(['/bookings']);
    }
  }  

}
