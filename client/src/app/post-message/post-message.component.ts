import {Component, Input, OnInit} from '@angular/core';
import {Message} from '../domain/message';
import {MessageService} from '../api/message.service';
import {User} from '../domain/user';
import {UserService} from '../api/user.service';
import {AuthenticationService} from '../api/authentication.service';

@Component({
  selector: 'app-post-message',
  templateUrl: './post-message.component.html',
  styleUrls: ['./post-message.component.scss'],
  providers: [MessageService]
})
export class PostMessageComponent implements OnInit {
  user: User;
  model: Message;

  constructor(private messageService: MessageService, private userService: UserService, private authService: AuthenticationService) {
  }

  ngOnInit() {
    this.loadUser();
    this.authService.loginEvent$.subscribe(() => this.loadUser());

    this.model = new Message();
  }

  loadUser() { this.user = this.userService.getLoggedInUser(); }

  onSubmit() {
    if (this.model.message) {
      this.model.date = new Date();
      this.messageService.postMessage(this.model);
      this.model = new Message();
      return true;
    }
  }
}
