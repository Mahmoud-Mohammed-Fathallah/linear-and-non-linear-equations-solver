import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import functionPlot from 'function-plot';
import { FunctionPlotAnnotation, FunctionPlotDatum, FunctionPlotDatumSecant } from 'function-plot/dist/types';
import * as tad from "tadiff"
@Component({
  selector: 'app-non-linear',
  templateUrl: './non-linear.component.html',
  styleUrls: ['./non-linear.component.css']
})
export class NonLinearComponent {

  @ViewChild('firstPointB', { static: false }) firstPointB: ElementRef = {} as ElementRef;
  @ViewChild('secondPointDiv', { static: false }) secondPointDiv: ElementRef = {} as ElementRef;
  @ViewChild('secondPointB', { static: false }) secondPointB: ElementRef = {} as ElementRef;
  @ViewChild('resultTxt', { static: false }) resultTxt: ElementRef = {} as ElementRef;

  constructor(private http: HttpClient, private renderer: Renderer2) { }

  method: string = "Bisection";
  SGF: number = 16;
  iterationsNum = 50;
  relativeError = 0.00001;

  draw(eq: string[], method: string, equ: string): void {
    equ = equ.replace(/e/g, "2.7182");

    let target:string = "#"+method;
    var data: FunctionPlotDatum[] = [];
    var fu: FunctionPlotAnnotation[] = [];
    var sec:FunctionPlotDatumSecant[] = []
    
    if (method == "Bisection" || method == "False-Position") {
      for (let i = 0; i < eq.length; i++) {
        let data1: FunctionPlotAnnotation = {}
        data1.x = Number.parseFloat(eq[i]);
        fu.push(data1);
      }
      let data1: FunctionPlotDatum = {}
      data1.fn = equ;
      
      data.push(data1)
      


    }
    else if (method == "Secant-Method"){
      console.log(data);
      for (let i=0; i<eq.length-2; i++){
        let data1:FunctionPlotDatumSecant = {"x0":5};
        data1.x0 = Number.parseFloat(eq[i]);
        data1.x1 = Number.parseFloat(eq[i+1]);
        sec.push(data1); 
      }
      let data1:FunctionPlotDatum = {}
      data1.secants = sec;
      data1.color = "red";
      data1.fn = equ;
      data.push(data1);
      let data2:FunctionPlotDatum = {};
      data2.fn = "";
      data.push(data2);
    }
    else if (method == "Fixed-point") {
      let data1: FunctionPlotDatum = {}
      data1.fn = eq[0].replace(/e/g, "2.7182")
      let data2: FunctionPlotDatum = {}
      data2.fn = "x";
      data.push(data1);
      data.push(data2);
    }
    else if (method == "Newton-Raphson"){
      let data1: FunctionPlotDatum = {}
      data1.fn = equ;
      let data2: FunctionPlotDatum = {}
      const derivative = tad.parseExpression("D(x, "+equ+")");
      data2.fn = derivative.evaluateToString();
      data.push(data1);
      data.push(data2);
    }
    
    const instance = functionPlot({
      title: 'Graph',
      target: target,
      width: 500,
      height: 1000,
      disableZoom: false,
      xAxis: {
        label: 'x - axis',
      },
      yAxis: {
        label: 'y - axis'
      },
      data: data,
      annotations: fu

    });
    
  }

  changeMethod(input: string) {
    this.method = input;
    if (this.method == "Bisection" || this.method == "False-Position") {
      this.renderer.setStyle(this.secondPointDiv.nativeElement, "display", "block");
      this.renderer.setProperty(this.firstPointB.nativeElement, "textContent", "Lower bound point: ");
      this.renderer.setProperty(this.secondPointB.nativeElement, "textContent", "Upper bound point: ");
    } else if (this.method == "Fixed-point" || this.method == "Newton-Raphson") {
      this.renderer.setStyle(this.secondPointDiv.nativeElement, "display", "none");
      this.renderer.setProperty(this.firstPointB.nativeElement, "textContent", "Initial guess: ");
    } else {
      this.renderer.setStyle(this.secondPointDiv.nativeElement, "display", "block");
      this.renderer.setProperty(this.firstPointB.nativeElement, "textContent", "First initial guess: ");
      this.renderer.setProperty(this.secondPointB.nativeElement, "textContent", "Second initial guess: ");
    }
  }

  changeIterations(input: string) {
    if (input == '')
      this.iterationsNum = 50;
    else
      this.iterationsNum = Number(input);
  }

  changeError(input: string) {
    if (input == '')
      this.relativeError = 0.00001;
    else
      this.relativeError = Number(input);
  }

  changeSGF(input: string) {
    if (input == '')
      this.SGF = 16;
    else
      this.SGF = Number(input);
  }

  solve(equation: string, firstPoint: string, secondPoint: string) {
    let equ = equation;
    equation = equation.replace(/\+/g, "˖");
    var data = new HttpParams;
    if (this.method == "Fixed-point" || this.method == "Newton-Raphson")
      data = data.append('firstPoint', Number(firstPoint));
    else {
      data = data.append('firstPoint', Number(firstPoint));
      data = data.append('secondPoint', Number(secondPoint));
    }
    data = data.append('equation', equation);
    data = data.append('SGF', this.SGF);
    data = data.append('relativeError', this.relativeError);
    data = data.append('iterationsNum', this.iterationsNum);

    this.http.post("http://localhost:8080/solve/non-linear/" + this.method, data).subscribe((response: any) => {
      
      if (response == null)
        this.resultTxt.nativeElement.value = "Error";
      else {
        this.resultTxt.nativeElement.value = "x = " + response[response.length - 2] + "\nSolving Time:" + response[response.length - 1] + " Microsecond"
        console.log(response);
        this.draw(response, this.method, equ);
      }
    });
  }

}
