package com.mixtoler.numerical;

import com.mixtoler.numerical.nonlinearsolvers.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/solve/non-linear")
public class NonLinearController {

	//Pass the equation to the expretion handler then overwrite it with it's return well formed expression
	//Make and instance of the required solving method and start calculating the time before it start solving
	//Get the result array (containing boundary points and solution) / convert it to a string array in order
	//to be returned for the fron end and add the solving time to the array
    NonLinearExpHandler nonLinearExpHandler = new NonLinearExpHandler();

    @RequestMapping("/Bisection")
    public String[] bisection(@RequestParam double firstPoint, @RequestParam double secondPoint,
                                  @RequestParam String equation, @RequestParam int SGF,
                                  @RequestParam double relativeError, @RequestParam int iterationsNum)
    {
        try {
            equation = nonLinearExpHandler.handleExpression(equation);
            Bisection bisection = new Bisection(iterationsNum, relativeError, SGF, firstPoint, secondPoint, equation);
            long startTime = System.nanoTime();
            double[] result = bisection.getSolution();
            long solvingTime = System.nanoTime() - startTime;
            String[] data = new String[result.length + 1];
            data[result.length] = String.valueOf(solvingTime / 1000);
            for (int i = 0; i < result.length; i++)
                data[i] = String.valueOf(result[i]);
            return data;
        }catch (Exception ignored){}
        return null;
    }

    @RequestMapping("/False-Position")
    public String[] falsePosition(@RequestParam double firstPoint, @RequestParam double secondPoint,
                                   @RequestParam String equation, @RequestParam int SGF,
                                   @RequestParam double relativeError, @RequestParam int iterationsNum)
    {
        try {
            FalsePosition falsePosition = new FalsePosition();
            equation = nonLinearExpHandler.handleExpression(equation);
            long startTime = System.nanoTime();
            double[] result = falsePosition.False_Position(equation, firstPoint, secondPoint, relativeError, iterationsNum, SGF);
            long solvingTime = System.nanoTime() - startTime;
            String[] data = new String[result.length + 1];
            data[result.length] = String.valueOf(solvingTime / 1000);
            for (int i = 0; i < result.length; i++)
                data[i] = String.valueOf(result[i]);
            return data;
        }catch (Exception ignored){}
        return null;
    }


    @RequestMapping("/Secant-Method")
    public String[] secantMethod(@RequestParam double firstPoint, @RequestParam double secondPoint,
                                @RequestParam String equation, @RequestParam int SGF,
                                @RequestParam double relativeError, @RequestParam int iterationsNum)
    {
        try {
            Secant secant = new Secant();
            equation = nonLinearExpHandler.handleExpression(equation);
            long startTime = System.nanoTime();
            double[] result = secant.SecantMethod(equation, firstPoint, secondPoint, relativeError, iterationsNum, SGF);
            long solvingTime = System.nanoTime() - startTime;
            String[] data = new String[result.length + 1];
            data[result.length] = String.valueOf(solvingTime / 1000);
            for (int i = 0; i < result.length; i++)
                data[i] = String.valueOf(result[i]);
            return data;
        }catch (Exception ignored){}
        return null;
    }


    @RequestMapping("/Newton-Raphson")
    public String[] newtonRaphson(@RequestParam double firstPoint,
                               @RequestParam String equation, @RequestParam int SGF,
                               @RequestParam double relativeError, @RequestParam int iterationsNum)
    {
        try {
            NewtonRaphson newtonRaphson = new NewtonRaphson();
            equation = nonLinearExpHandler.handleExpression(equation);
            long startTime = System.nanoTime();
            double result = newtonRaphson.Newton(equation, firstPoint, relativeError, iterationsNum, SGF);
            long solvingTime = System.nanoTime() - startTime;
            String[] data = new String[2];
            data[1] = String.valueOf(solvingTime / 1000);
            data[0] = String.valueOf(result);
            return data;
        }catch (Exception ignored){}
        return null;
    }


    @RequestMapping("/Fixed-point")
    public String[] fixedPoint(@RequestParam double firstPoint,
                                @RequestParam String equation, @RequestParam int SGF,
                                @RequestParam double relativeError, @RequestParam int iterationsNum)
    {
        try {
            equation = nonLinearExpHandler.handleExpression(equation);
            long startTime = System.nanoTime();
            String[] result = new FixedPoint(equation, iterationsNum, SGF, relativeError, firstPoint).solve();
            long solvingTime = System.nanoTime() - startTime;
            String[] data = new String[3];
            data[2] = String.valueOf(solvingTime / 1000);
            data[1] = result[0];
            data[0] = result[1];
            return data;
        }catch (Exception ignored){}
        return null;
    }


}
