import { Injectable } from '@angular/core';
import { Login, User } from '../shared/model';
import { isEqual } from 'lodash';

const mockData: Login = {
  username: 'aaa',
  password: '123'
}

const mockUser: User = {
  name: "Andrej"
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLogged: boolean = false;
  user: User | undefined;

  constructor() { }

  isAuthenticated(): boolean {
    return this.isLogged;
  }

  login(userLogin: Login): boolean {
    if (isEqual(userLogin, mockData)) {
      this.user = mockUser;
      this.isLogged = true;
      return true;
    }
    return false;
  }

  logout() {
    this.user = undefined;
    this.isLogged = false;
  }
}
