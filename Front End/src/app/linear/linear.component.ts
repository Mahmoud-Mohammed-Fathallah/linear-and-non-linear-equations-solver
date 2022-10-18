import { AfterViewInit, Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";

@Component({
  selector: 'app-linear',
  templateUrl: './linear.component.html',
  styleUrls: ['./linear.component.css']
})
export class LinearComponent implements AfterViewInit {

  @ViewChild('luDiv', { static: false }) luDiv: ElementRef = {} as ElementRef;
  @ViewChild('methodsSel', { static: false }) methodsSel: ElementRef = {} as ElementRef;
  @ViewChild('formsSel', { static: false }) formsSel: ElementRef = {} as ElementRef;
  @ViewChild('iterationDiv', { static: false }) iterationDiv: ElementRef = {} as ElementRef;
  @ViewChild('precisionChb', { static: false }) precisionChb: ElementRef = {} as ElementRef;
  @ViewChild('precisionNum', { static: false }) precisionNum: ElementRef = {} as ElementRef;
  @ViewChild('iterationsInp', { static: false }) iterationsInp: ElementRef = {} as ElementRef;
  @ViewChild('errorInp', { static: false }) errorInp: ElementRef = {} as ElementRef;
  @ViewChild('equationsInp', { static: false }) equationsInp: ElementRef = {} as ElementRef;
  @ViewChild('initialGuessInp', { static: false }) initialGuessInp: ElementRef = {} as ElementRef;
  @ViewChild('resultTxt', { static: false }) resultTxt: ElementRef = {} as ElementRef;

  constructor(private http: HttpClient, private renderer: Renderer2){ }

  method:string = "";
  form:string = "";
  equations:string = "";
  initialGuess:string = "";
  precision:number = 5;
  iterationsNum = 50;
  relativeError = 20;
  solution:string = "";
  data = new HttpParams;


  ngAfterViewInit():void {
    this.renderer.setStyle(this.luDiv.nativeElement, "display", "none");
    this.renderer.setStyle(this.iterationDiv.nativeElement, "display", "none");
    this.renderer.setProperty(this.precisionNum.nativeElement, 'disabled', true);

    this.method = this.methodsSel.nativeElement.value;
    this.form = this.formsSel.nativeElement.value;
  }

  changeMethod() {
    this.method = this.methodsSel.nativeElement.value;
    if(this.method == "LU-Decomposition")
      this.renderer.setStyle(this.luDiv.nativeElement, "display", "block");
    else
      this.renderer.setStyle(this.luDiv.nativeElement, "display", "none");

    if(this.method == "Gauss-Seidil" || this.method == "Jacobi-Iteration")
      this.renderer.setStyle(this.iterationDiv.nativeElement, "display", "block");
    else
      this.renderer.setStyle(this.iterationDiv.nativeElement, "display", "none");
  }

  changeForm() {
    this.form = this.formsSel.nativeElement.value;
  }

  changePrecision() {
    this.precision = this.precisionNum.nativeElement.value;
  }

  togglePrecisionNum() {
    var currentState = this.precisionNum.nativeElement.disabled;
    this.renderer.setProperty(this.precisionNum.nativeElement, 'disabled', !currentState);
    if(!currentState) {
      this.renderer.setProperty(this.precisionNum.nativeElement, 'value', '');
      this.renderer.setProperty(this.precisionNum.nativeElement, 'placeholder', 'Default 5');
      this.precision = 5;
    }
  }

  changeIterations(){
    var num = this.iterationsInp.nativeElement.value;
    if(num != '')
      this.iterationsNum = num;
    else
      this.iterationsNum = 50;
  }

  changeError(){
    var error = this.errorInp.nativeElement.value;
    if(error != '')
      this.relativeError = error;
    else
      this.relativeError = 20;
  }

  solve(){
    this.equations = this.equationsInp.nativeElement.value;
    this.initialGuess = this.initialGuessInp.nativeElement.value;

    this.equations = this.equations.replace(/\+/g, "˖");
    this.initialGuess = this.initialGuess.replace(/\+/g, "˖");

    this.data = new HttpParams;
    this.data = this.data.append('equationsStr', this.equations);
    this.data = this.data.append('precision', this.precision);

    if(this.method == "Gauss-Elimination" || this.method == "Gauss-Jordan"){
      this.sendRequest();
    }else if(this.method == "LU-Decomposition"){
      this.data = this.data.append('form', this.form);
      this.sendRequest();
    }else if(this.method == "Gauss-Seidil" || this.method == "Jacobi-Iteration"){
      this.data = this.data.append('initialGuessStr', this.initialGuess);
      this.data = this.data.append('iterationsNum', this.iterationsNum);
      this.data = this.data.append('relativeError', this.relativeError);
      this.sendRequest();
    }

  }

  sendRequest(){
    this.http.post("http://localhost:8080/solve/linear/" + this.method, this.data, { responseType: 'text'}).subscribe((response:any) => {
      this.resultTxt.nativeElement.value = response;
    });
  }

}
