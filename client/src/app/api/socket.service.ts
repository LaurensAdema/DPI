import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';
import {Message} from '../domain/message';
import {UserService} from './user.service';
import {Message as StompMessage} from '@stomp/stompjs';
import {StompService} from '@stomp/ng2-stompjs';

@Injectable()
export class SocketService {

  private messageUpdated = new Subject<Message>();
  messageUpdated$ = this.messageUpdated.asObservable();

  constructor(private userService: UserService, private stompService: StompService) {
  }

  startListener() {
    const user = this.userService.getLoggedInUser();
    if (user) {
      const userSub = this.stompService.subscribe(`user/${user.username}`);
      userSub.subscribe(this.onMessage);
      const groupSub = this.stompService.subscribe(`group/${user.group.name}`);
      groupSub.subscribe(this.onMessage);
    }
  }

  onMessage(message: StompMessage) {
    this.messageUpdated.next(JSON.parse(message.body));
  }

  sendMessage(message: Message) {
    this.stompService.publish('post', JSON.stringify(message));
  }
}
