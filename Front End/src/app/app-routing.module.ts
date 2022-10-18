import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LinearComponent } from './linear/linear.component';
import { NonLinearComponent } from './non-linear/non-linear.component';

const routes: Routes = [
  {path:'non-linear', component:NonLinearComponent},
  {path:'**', component:LinearComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
