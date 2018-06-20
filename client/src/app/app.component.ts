import {Component, OnInit} from '@angular/core';
import {UserService} from './api/user.service';
import {AuthenticationService} from './api/authentication.service';
import {SocketService} from './api/socket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AuthenticationService, UserService, SocketService]
})
export class AppComponent implements OnInit {
  constructor(private socketService: SocketService, private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.socketService.startListener();
    this.authService.loginEvent$.subscribe(() => this.socketService.startListener());
  }
}


