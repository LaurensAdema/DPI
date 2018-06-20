import {User} from './user';

export class Message {
  date: Date;
  message: string;
  sender: User;
  destination: string;
}
