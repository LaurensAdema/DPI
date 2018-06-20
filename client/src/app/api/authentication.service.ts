import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable()
export class AuthenticationService {
  private loginEvent = new Subject();
  loginEvent$ = this.loginEvent.asObservable();

  constructor() {}

  login(username: string, group: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('group', group);
    this.loginEvent.next();
  }

  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('group');
    this.loginEvent.next();
  }
}
