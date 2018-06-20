import { Injectable } from '@angular/core';
import {User} from '../domain/user';
import 'rxjs/add/observable/of';
import {Group} from '../domain/group';

@Injectable()
export class UserService {

  constructor() { }

  getLoggedInUser() {
    if (localStorage.getItem('username')) {
      const user = new User();
      user.username = localStorage.getItem('username');
      user.group = new Group();
      user.group.name = localStorage.getItem('group');
      return user;
    }
    return null;
  }
}
