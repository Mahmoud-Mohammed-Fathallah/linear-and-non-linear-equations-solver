package com.mixtoler.numerical;

import com.mixtoler.numerical.linearsolvers.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@RestController
@RequestMapping("/solve/linear")
public class LinearController {

    String[] equations;
    ArrayList<String> variables;
    double[] freeVariables;
    double[][] coefficients;
    long startTime;

    //extract the equations/variables/freeVariables/coefficients from the user input
    private void setParameters(String equationsStr, int precision) throws Exception{
        Parser parser = new Parser();
        parser.setPrecision(precision);
        equationsStr = equationsStr.replaceAll("˖", "+");
        equations = parser.extractEquations(equationsStr);
        if (!parser.isValidEquations(equations))
            throw new Exception();
        variables = parser.extractVariables(equations);
        if(!parser.isValidVariables(variables, equations))
            throw new Exception();
        freeVariables = parser.extractFreeVariables(equations);
        coefficients = parser.extractCoefficients(variables, equations);
    }

    //construct the solution for ang assigning it with it's variable name to be returned to the backend
    private String buildSolution(double[] result){
        //calculate time of the solution
        long solvingTime = System.nanoTime() - startTime;
        String solution = "";
        for (int i = 0; i < result.length; i++)
            solution = solution + variables.get(i) + " = " + result[i] + "\n";
        solution = solution + "Solving Time: " + solvingTime / 1000 + " Microsecond";
        return solution;
    }

    //set the paramaters / solve the equations / construct and return the solution
    @RequestMapping("/Gauss-Elimination")
    public String gaussElimination(@RequestParam String equationsStr, @RequestParam int precision) {
       try {
           setParameters(equationsStr, precision);
           GaussElimination gaussElimination = new GaussElimination(variables.size(), coefficients, freeVariables, precision);
           startTime = System.nanoTime();
           return buildSolution(gaussElimination.solve());
       }catch (Exception ignored){}
        return "Error";
    }

    //set the paramaters / solve the equations / construct and return the solution
    @RequestMapping("/Gauss-Jordan")
    public String gaussJordan(@RequestParam String equationsStr, @RequestParam int precision) {
        try {
            setParameters(equationsStr, precision);
            GaussJordan gaussJordan = new GaussJordan(variables.size(), coefficients, freeVariables, precision);
            startTime = System.nanoTime();
            return buildSolution(gaussJordan.solve());
        }catch (Exception ignored){}
        return "Error";
    }

    //set the paramaters / solve the equations / construct and return the solution
    @RequestMapping("/LU-Decomposition")
    public String luDecomposition(@RequestParam String equationsStr, @RequestParam int precision, @RequestParam String form) {
        try {
            setParameters(equationsStr, precision);
            LUDecomposition luDecomposition = new LUDecomposition(variables.size(), coefficients, freeVariables, precision);
            startTime = System.nanoTime();
            if (!luDecomposition.solve(form.equals("Crout-Form")))
                throw new Exception();
            return buildSolution(luDecomposition.solution());
        }catch (Exception ignored){}
        return "Error";
    }

    @RequestMapping("/Gauss-Seidil")
    public String gaussSeidil(@RequestParam String equationsStr, @RequestParam int precision,
                              @RequestParam String initialGuessStr, @RequestParam int iterationsNum,
                              @RequestParam double relativeError) {
        try {
            //extract the equations parameters from the user input
            setParameters(equationsStr, precision);
            //extract the initial guess from the user input
            Parser inParser = new Parser();
            inParser.setPrecision(precision);
            initialGuessStr = initialGuessStr.replaceAll("˖", "+");
            String[] inEquations = inParser.extractEquations(initialGuessStr);
            if (!inParser.isValidEquations(inEquations))
                throw new Exception();
            ArrayList<String> inVariables = inParser.extractVariables(inEquations);
            if(!inParser.isValidVariables(inVariables, inEquations))
                throw new Exception();
            double[] inFreeVariables = inParser.extractFreeVariables(inEquations);
            double[][] inCoefficients = inParser.extractCoefficients(inVariables, inEquations);

            //check that the initial guess have the same variables name and number of the variables of the equations
            for (int i = 0; i < variables.size(); i++) {
                if (inVariables.size() != variables.size() || !inVariables.contains(variables.get(i))) {
                    throw new Exception();
                }
            }

            //simplify the initial guess (because it may be 2x = 2) so we get the simplified (x = 1)
            GaussElimination gaussElimination = new GaussElimination(inVariables.size(), inCoefficients, inFreeVariables, precision);
            double[] initialGuessTemp = gaussElimination.solve();

            //reorder the initial guess to the equation's variable name index
            double[] initialGuess = new double[initialGuessTemp.length];
            for (int i = 0; i < variables.size(); i++)
                initialGuess[i] = initialGuessTemp[inVariables.indexOf(variables.get(i))];

            //solve and return the solution
            Seidel seidel = new Seidel();
            //calculate the start time of solving with the method
            startTime = System.nanoTime();
            String solution = buildSolution(seidel.SeidelCalc(coefficients, freeVariables, initialGuess, iterationsNum, relativeError/100, precision));
            return solution;
        }catch (Exception ignored){}
        return "Error";
    }

    @RequestMapping("/Jacobi-Iteration")
    public String jacobiIteration(@RequestParam String equationsStr, @RequestParam int precision,
                              @RequestParam String initialGuessStr, @RequestParam int iterationsNum,
                              @RequestParam double relativeError) {
        try {
            //extract the equations parameters from the user input
            setParameters(equationsStr, precision);
            //extract the initial guess from the user input
            Parser inParser = new Parser();
            inParser.setPrecision(precision);
            initialGuessStr = initialGuessStr.replaceAll("˖", "+");
            String[] inEquations = inParser.extractEquations(initialGuessStr);
            if (!inParser.isValidEquations(inEquations))
                throw new Exception();
            ArrayList<String> inVariables = inParser.extractVariables(inEquations);
            if(!inParser.isValidVariables(inVariables, inEquations))
                throw new Exception();
            double[] inFreeVariables = inParser.extractFreeVariables(inEquations);
            double[][] inCoefficients = inParser.extractCoefficients(inVariables, inEquations);

            //check that the initial guess have the same variables name and number of the variables of the equations
            for (int i = 0; i < variables.size(); i++) {
                if (inVariables.size() != variables.size() || !inVariables.contains(variables.get(i))) {
                    throw new Exception();
                }
            }

            //simplify the initial guess (because it may be 2x = 2) so we get the simplified (x = 1)
            GaussElimination gaussElimination = new GaussElimination(inVariables.size(), inCoefficients, inFreeVariables, precision);
            double[] initialGuessTemp = gaussElimination.solve();

            //reorder the initial guess to the equation's variable name index
            double[] initialGuess = new double[initialGuessTemp.length];
            for (int i = 0; i < variables.size(); i++)
                initialGuess[i] = initialGuessTemp[inVariables.indexOf(variables.get(i))];
            
            //solve and return the solution
            Jacobi jacobi = new Jacobi();
            //calculate the start time of solving with the method
            startTime = System.nanoTime();
            String solution = buildSolution(jacobi.JacobiCalc(coefficients, freeVariables, initialGuess, iterationsNum, relativeError/100, precision));
            return solution;
        }catch (Exception ignored){}
        return "Error";
    }

}
