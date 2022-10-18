package com.mixtoler.numerical.nonlinearsolvers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import org.mariuszgromada.math.mxparser.*;

public class Bisection {
    private int maxIterations = 50;
    private double errorTolerance = 0.000001;
    private int precision = 6;
    private double xLower, xUpper;
    private boolean noRoot = false;
    private String function = "x^3-0.165*x^2+3.993*10^-4";
    private final ArrayList<Double> solution = new ArrayList<>();
    // returns true if there's no root between the given boundaries xL and xU
    public boolean isNoRoot() {
        if(evaluate(xLower)*evaluate(xUpper)>0){
            noRoot = true;
        }
        return noRoot;
    }

    public Bisection(int maxIterations,double errorTolerance,int precision,double xLower,double xUpper,String function){
        if(errorTolerance!= -1)this.errorTolerance = errorTolerance;
        if(maxIterations != -1)this.maxIterations = maxIterations;
        if(precision != -1)this.precision = precision;
        if(!function.equals(""))this.function = function;
        this.xLower = xLower;
        this.xUpper = xUpper;

    }
    // finds the root of the given function using bisection
    public double[] getSolution(){
        findRootBisection();
        return solution.stream().mapToDouble(Double::doubleValue).toArray();
    }

    // applies Bisection method to find the root
    void findRootBisection(){
        // if there's no root between the given bounds "bracket" sets 'noRoot' to true and ends method
        if(isNoRoot()){
            return;
        }
        solution.add(xLower);
        solution.add(xUpper);
        double xrOld = 0;
        double xR = 0;
        for (int i = 0; i < maxIterations; i++) {
            System.out.println(i);
            double approxError = Double.MAX_VALUE;
            if(i>0){
                xrOld = xR;
            }
            xR = round( (xUpper + xLower)/2);
            solution.add(xR);
            // calculates approximate error
            if (i>0){
                approxError = Math.abs((xR-xrOld)/xR);
            }
            double test = evaluate(xR) * evaluate(xLower);


            // find if the root lies between xR and xL or between xR and xU
            if (test < 0){
                xUpper = xR;
            }
            else{
                xLower = xR;
            }
            // if one of the bounds is equal to zero, then this means we have found the root,
            // so we set approximate error to zero to end iterations
            if(test == 0){
                approxError = 0;
            }
            // ends the iterations early if approximate error is smaller than the
            // given tolerance
            if(approxError < errorTolerance){
                break;
            }
        }

    }

    public double round(double number){
        BigDecimal numberBD = new BigDecimal(number);
        numberBD = numberBD.round(new MathContext(precision));
        number = numberBD.doubleValue();
        return number;
    }

    double evaluate(double x){
        Argument input = new Argument("x = " + x);
        Expression e = new Expression(function,input);
        return e.calculate();
    }



}
