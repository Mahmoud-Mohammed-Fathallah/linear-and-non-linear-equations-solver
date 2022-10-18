import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import functionPlot from 'function-plot';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements AfterViewInit {
  title = 'Numerical';

  @ViewChild('equationsSystemSel', { static: false }) equationsSystemSel: ElementRef = {} as ElementRef;

  constructor(private router:Router){}

  ngAfterViewInit(): void {
    this.router.navigate([this.equationsSystemSel.nativeElement.value]);
  }

  changeEquationsSystem(input:string){
    this.router.navigate([input]);
  }
}
