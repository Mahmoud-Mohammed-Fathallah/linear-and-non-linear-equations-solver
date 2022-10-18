package com.mixtoler.numerical.nonlinearsolvers;

import org.mariuszgromada.math.mxparser.*;

import java.util.Stack;

public class FalsePosition {
    int SF;
    String Exp;
    Stack<Double> stack = new Stack<Double>(); //stack to contain all Xi

    // methode evaluate Fx value//
    public  double FxGetter (double X ){
        Argument x = new Argument("x = "+String.valueOf(X));
        Expression e = new Expression(Exp, x);
        double result = e.calculate();
        return result;
    }

    // methode get Xr value//
    public  double XrFalse (double Xl,double Xu ){
        double Xr= Xu - FxGetter(Xu) * (Xu-Xl)/(FxGetter(Xu)-FxGetter(Xl)) ;
        return Xr;
    }
    //      Methode to round numbers        //
    double  round (double  x) {
        if(SF==-1){return x;}
        double temp = x;
        int count = 0;
        while (Math.abs(temp)>=1) {
            temp /= 10;
            count++;
        }
        int sigFig_modified = SF - count;
        double scale =(double)Math.pow(10, sigFig_modified);
        x = Math.round(x * scale) / scale;


        return x;
    }
    // False position Method //
    public  double[] False_Position  (String exp,double Xl,double Xu,double Es,double Iter,int sf){
        SF=sf;
        Exp=exp;
        Xl=round(Xl);
        stack.push(Xl);
        Xu=round(Xu);
        stack.push(Xu);
        double Fxl;
        double Xr=0;
        double Er=100;
        double XrOld=0;
        double Fxr;
        int i=0;
        while( Er>Es && Iter>i){
            Xr=round(XrFalse(Xl, Xu));
            stack.push(Xr);
            Fxl=round(FxGetter(Xl));
            Fxr=round(FxGetter(Xr));
            if(Fxl*Fxr<0){Xu=Xr;}
            else{Xl=Xr;}
            if (i==0){}
            else{Er= round( Math.abs((Xr-XrOld)/Xr) );}
            XrOld = Xr;
            i++;
        }
        int L=stack.size();
        double[] Arr=new double[L];
        System.out.println(stack);
        for (int s=Arr.length-1;s>=0;s--){
            Arr[s]=stack.pop() ;
        }
        return Arr;//Array contain (Xl,Xu,Xr,Xr2,.....)
    }

}
