import {Component, OnInit} from '@angular/core';
import {User} from '../domain/user';
import {AuthenticationService} from '../api/authentication.service';
import {UserService} from '../api/user.service';
import {SocketService} from '../api/socket.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  user: User;

  constructor(private authService: AuthenticationService, private userService: UserService, private socketService: SocketService) { }

  ngOnInit(): void {
    this.loadUser();
    this.authService.loginEvent$.subscribe(() => this.loadUser());
    this.socketService.startListener();
    this.authService.loginEvent$.subscribe(() => this.loadUser());
  }

  loadUser() { this.user = this.userService.getLoggedInUser(); }
}
