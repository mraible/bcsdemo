import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { BeerListComponent } from './beer-list/beer-list.component';
import { StormpathConfiguration, StormpathModule } from 'angular-stormpath';
import { MaterialModule } from '@angular/material';
import { AppShellModule } from '@angular/app-shell';
import { environment } from '../environments/environment';

export function stormpathConfig(): StormpathConfiguration {
  let spConfig: StormpathConfiguration = new StormpathConfiguration();
  if (environment.production) {
    spConfig.endpointPrefix = 'https://pwa-edge-service.cfapps.io';
  } else {
    spConfig.endpointPrefix = 'http://localhost:8081';
  }
  spConfig.autoAuthorizedUris.push(new RegExp(spConfig.endpointPrefix + '/*'));
  return spConfig;
}

@NgModule({
  declarations: [
    AppComponent,
    BeerListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    StormpathModule,
    MaterialModule,
    AppShellModule.runtime()
  ],
  providers: [
    {
      provide: StormpathConfiguration, useFactory: stormpathConfig
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
