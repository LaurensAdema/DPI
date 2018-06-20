import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Message} from '../domain/message';
import {MessageService} from '../api/message.service';
import {animateChild, query, transition, trigger} from '@angular/animations';
import {User} from '../domain/user';
import {UserService} from '../api/user.service';
import {AuthenticationService} from '../api/authentication.service';

@Component({
  selector: 'app-timeline',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss'],
  providers: [MessageService],
  animations: [
    trigger('ngIfAnimation', [
      transition(':enter, :leave', [
        query('@*', animateChild())
      ])
    ])
  ]
})
export class FeedComponent implements OnInit, OnChanges {
  @Input() timelineUser: User;
  @Input() blockTimeline: boolean;

  user: User;
  messages: Message[] = [];

  constructor(private messageService: MessageService, private userService: UserService, private authService: AuthenticationService) {
  }

  ngOnInit() {
    this.loadUser();
    this.authService.loginEvent$.subscribe(() => { this.loadUser(); });
  }

  loadUser() { this.user = this.userService.getLoggedInUser(); }

  ngOnChanges(changes: SimpleChanges): void {
    this.loadMessages();
  }

  loadMessages() {

  }

  handleMessage(message: Message) {
    this.addMessage(message);
  }

  addMessage(toAdd: Message) {
    this.messages.unshift(toAdd);
  }
}
