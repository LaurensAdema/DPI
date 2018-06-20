import { BrowserModule } from '@angular/platform-browser';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FeedComponent } from './feed/feed.component';
import { PostMessageComponent } from './post-message/post-message.component';
import { MessageComponent } from './message/message.component';
import {HttpClientModule} from '@angular/common/http';
import { MomentModule } from 'angular2-moment';
import {FormsModule} from '@angular/forms';
import { AppRoutingModule } from './/app-routing.module';
import { HomeComponent } from './home/home.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {StompConfig, StompService} from '@stomp/ng2-stompjs';
import {environment} from '../environments/environment';

export function tokenGetter() {
  return localStorage.getItem('access_token');
}

const stompConfig: StompConfig = {
  // Which server?
  url: `${environment.ws_protocol}${environment.api_domain}/messages/`,
  headers: {
    login: 'guest',
    passcode: 'guest'
  },
  heartbeat_in: 0, // Typical value 0 - disabled
  heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
  reconnect_delay: 5000,
  debug: true
};

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FeedComponent,
    PostMessageComponent,
    MessageComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MDBBootstrapModule.forRoot(),
    MomentModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [StompService,
    {
      provide: StompConfig,
      useValue: stompConfig
    }],
  bootstrap: [AppComponent],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class AppModule { }
