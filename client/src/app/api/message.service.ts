import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment';
import {Message} from '../domain/message';

@Injectable()
export class MessageService {
  constructor(private http: HttpClient) {}

  postMessage(tweet) {
    return this.http.put<Message>(`${environment.api_protocol}${environment.api_domain}/message`, tweet);
  }
}
