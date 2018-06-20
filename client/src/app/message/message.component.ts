import {Component, Input } from '@angular/core';
import {Message} from '../domain/message';
import {animate, style, transition, trigger} from '@angular/animations';
import {User} from '../domain/user';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
  animations: [
    trigger('animateMessage', [
      transition(':enter', [
        style({transform: 'scale(0.5)', opacity: 0, height: 0, overflow: 'hidden'}),  // initial
        animate('1s cubic-bezier(.8, -0.6, 0.2, 1.5)',
          style({transform: 'scale(1)', opacity: 1, height: '*'}))  // final
      ]),
      transition(':leave', [
        style({transform: 'scale(1)', opacity: 1, height: '*'}),
        animate('1s cubic-bezier(.8, -0.6, 0.2, 1.5)',
          style({
            transform: 'scale(0.5)', opacity: 0,
            height: '0px', margin: '0px', overflow: 'hidden'
          }))
      ])
    ])
  ]
})
export class MessageComponent {
  @Input() message: Message;

  constructor() {
  }
}
