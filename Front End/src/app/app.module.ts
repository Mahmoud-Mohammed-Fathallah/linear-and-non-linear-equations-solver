import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LinearComponent } from './linear/linear.component';
import { NonLinearComponent } from './non-linear/non-linear.component';

@NgModule({
  declarations: [
    AppComponent,
    LinearComponent,
    NonLinearComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
