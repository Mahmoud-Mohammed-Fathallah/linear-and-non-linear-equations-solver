package com.mixtoler.numerical.nonlinearsolvers;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class NewtonRaphson {
    public String func;
    public String dfunc;
    public int SF;
    //      Methode to round numbers        //
    public double round (double  x) {
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
    //method to get the f(x)
    public double fun(double X){
        Argument x = new Argument("x = "+String.valueOf(X));
        Expression e = new Expression(func, x);
        double result = e.calculate();
        return result;
    }
    //method to get derivative of f(x)
    public double der(double X){
        Argument x = new Argument("x = "+String.valueOf(X));
        Expression e = new Expression(dfunc, x);
        double result = e.calculate();
        return result;
    }
    /*
    * this method takes String containing the function "infun" and intial guess "x0"
    * and maximum relative error "es", maximum number of iterations "imax" and number
    * of Significant figures "sf"
    * it returns an approximation of the root of equation obtained with Newton Raphson method
    * */
    public double Newton(String infun,double x0,double es,int imax,int sf){
        dfunc = "der("+infun+",x)";
        func = infun;
        SF = sf;
        double xold=x0,x=x0;
        for(int i=1; i<=imax; i++){
            xold = x;
            x = round(round(x) - round(round(fun(xold))/round(der(xold))));
            if(Math.abs((x-xold)/x)<es){
                break;
            }
        }
        return x;
    }
}