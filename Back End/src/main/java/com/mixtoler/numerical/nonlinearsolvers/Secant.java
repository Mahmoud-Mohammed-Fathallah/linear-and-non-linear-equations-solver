package com.mixtoler.numerical.nonlinearsolvers;
import org.mariuszgromada.math.mxparser.*;

import java.util.Stack;


public class Secant {
    int SF; //Significant figure
    String Exp; //string to hold function
    Stack<Double> stack = new Stack<Double>(); //stack to contain all Xi

    // methode evaluate (Fx) value//
    public  double FxGetter (double X ){
        Argument x = new Argument("x = "+String.valueOf(X));
        Expression e = new Expression(Exp, x);
        double result = e.calculate();
        return result;
    }
    // methode get Xr value//
    public  double XrSecant (double Xi,double Xp ){//Xi:xi now, Xp:previos X'X(i-1)'
        double Xr= round(Xi - round( round(FxGetter(Xi) * (round(Xp-Xi)))/ round(FxGetter(Xp)-FxGetter(Xi) ) ) ) ;
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
    //          Solving by Secant Method               //
    public  double[] SecantMethod  (String exp,double Xi,double Xp,double Es,double Iter,int sf){
        SF=sf;
        Exp=exp;
        Xp=round(Xp);
        Xi=round(Xi);
        stack.push(Xp);
        stack.push(Xi);
        double Fxl;
        double Xr=0;
        double Er=100;
        double XrOld=0;
        double Fxr;
        int i=0;
        while( Er>Es && Iter>i){
            Xr=round(XrSecant(Xi, Xp));
            stack.push(Xr);
            Er= Math.abs((Xr-Xi)/Xr);
            Xp = Xi;
            Xi = Xr;
            i++;
        }
        int L=stack.size();
        double[] Arr=new double[L];
        System.out.println(stack);
        for (int s=Arr.length-1;s>=0;s--){
            Arr[s]=stack.pop() ;
        }
        return Arr;//Array contain (X0,X1,X2,X3,.....)
    }
}
