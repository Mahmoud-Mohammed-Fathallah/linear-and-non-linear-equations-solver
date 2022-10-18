package com.mixtoler.numerical;

import java.util.ArrayList;

public class Parser {

	private int precision;
	public void setPrecision(int precision){
		this.precision = precision;
	}

	public String[] extractEquations(String input) {
		input = input.replaceAll(" ", "");

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '*' && Character.isDigit(input.charAt(i - 1))
					&& Character.isAlphabetic(input.charAt(i + 1)))
				input = input.substring(0, i) + input.substring(i + 1);
		}

		int oldLength;
		do {
			oldLength = input.length();
			input = input.replaceAll("\\+-", "-");
			input = input.replaceAll("-\\+", "-");
			input = input.replaceAll("--", "+");
			input = input.replaceAll("\\+\\+", "+");
			if(input.endsWith("\n"))
				input = input.substring(0, input.length() - 1);
		}while(input.length() != oldLength);

		return input.split("\n");
	}

	//loop through the equations to check each equation doesn't contain unsupported symbols
	//and each equation have a single equality "="
	public boolean isValidEquations(String[] equations) {
		for (String s : equations) {
			int equalities = 0;
			for (int j = 0; j < s.length(); j++) {
				char c = s.charAt(j);
				if (!Character.isAlphabetic(c)
						&& !Character.isDigit(c)
						&& !(c == '-')
						&& !(c == '+')
						&& !(c == '.')
						&& !(c == '='))
					return false;
				if (c == '=')
					equalities++;
			}
			if (equalities != 1)
				return false;
		}
		return true;
	}

	private String findVariable(String equation, int startIndex) {
		String variable = "";
		char c;
		for(int j = startIndex; j < equation.length(); j++) {
			c = equation.charAt(j);
			if (Character.isAlphabetic(c)) {
				int k = j;
				while (c != '+' && c != '-' && c != '=') {
					variable = variable + c;
					k++;
					if (k >= equation.length())
						break;
					c = equation.charAt(k);
				}
				break;
			}
			if(c == '+' || c == '-' || c == '=')
				if (j != startIndex)
					break;
		}
		return variable;
	}

	//extract variables names from the equations and add them to the "variables" arraylist
	public ArrayList<String> extractVariables(String[] equations) {
		ArrayList<String> variables = new ArrayList<>();
		for (String s : equations)
			for (int j = 0; j < s.length(); j++) {
				char c = s.charAt(j);
				if (j == 0 || c == '+' || c == '-' || c == '=') {
					//build the variable name
					String variable = findVariable(s, j);
					//if there is a variable and isn't already in the arraylist then add it
					if (!variables.contains(variable) && !variable.equals(""))
						variables.add(variable);
				}
			}
		return variables;
	}

	//Check if the number of variables less than the number of equations
	public boolean isValidVariables(ArrayList<String> variables, String[] equations) {
		return variables.size() == equations.length;
	}

	public double[] extractFreeVariables(String[] equations) {
		double[] freeVariables = new double[equations.length];
		for (int i = 0; i < equations.length; i++) {
			int signe = -1;
			for (int j = 0; j < equations[i].length(); j++) {
				char c = equations[i].charAt(j);
				if (c == '=')
					signe = 1;
				if (j == 0 || c == '+' || c == '-' || c == '=')
					if (findVariable(equations[i], j).equals("")) {
						String temp = "";
						char x = '*';
						if (c == '-')
							temp = c + temp;
						for (int k = j; k < equations[i].length(); k++) {
							if (k < equations[i].length() - 1)
								x = equations[i].charAt(k + 1);
							c = equations[i].charAt(k);
							if (Character.isDigit(c) || c == '.')
								temp = temp + c;
							if (x == '+' || x == '-' || x == '=' || k == equations[i].length() - 1) {
								if (!temp.equals(""))
									freeVariables[i] += round(Double.parseDouble(temp) * signe);
								break;
							}
							j = k;
						}
					}
			}
		}
		return freeVariables;
	}

	public double[][] extractCoefficients(ArrayList<String> variables, String[] equations) {
		double[][] coefficients = new double[equations.length][equations.length];
		for (int i = 0; i < equations.length; i++) {
			String equation = equations[i];
			int signe = 1;
			for (int j = 0; j < equation.length(); j++) {
				char c = equation.charAt(j);
				if(j == 0 || c == '+' || c == '-' ||  c == '='){
					if (c == '=')
						signe = -1;
					String variable = findVariable(equation, j);
					if(!variable.equals("")){
						String temp = "";
						if(c == '-')
							temp = c + temp;
						for(int k = j; k < equation.length(); k++){
							c = equation.charAt(k);
							if (Character.isDigit(c) || c == '.') {
								temp = temp + c;
							}else if(Character.isAlphabetic(c)) {
								if (temp.equals("") || temp.equals("-"))
									temp = temp + "1";
								coefficients[i][variables.indexOf(variable)] += round(Double.parseDouble(temp) * signe);
								break;
							}
							j = k;
						}
					}
				}
			}
		}
		return coefficients;
	}

	protected double round (double x) {
		double temp = x;
		int count = 0;
		while (Math.abs(temp)>=1) {
			temp /= 10;
			count++;
		}
		int sigFig_modified = precision - count;
		double scale = Math.pow(10, sigFig_modified);
		x = Math.round(x * scale) / scale;

		return x;
	}

}