package com.mixtoler.numerical;

public class NonLinearExpHandler {

    String handleExpression(String input){
		//Handling the naming conventions of the front end
        input = input.replaceAll(" ", "");
        input = input.replaceAll("˖", "+");

		//replace tricky casses with ordinary ones
        int oldLength;
        do {
            oldLength = input.length();
            input = input.replaceAll("\\+-", "-");
            input = input.replaceAll("-\\+", "-");
            input = input.replaceAll("--", "+");
            input = input.replaceAll("\\+\\+", "+");
        }while(input.length() != oldLength);

		//build the expression in a well defined format by adding "*" symbole before vaariables or functions or "("
        String expression = "";
        input = input.replaceAll("exp", "e^");
        for (int i=0; i<input.length(); i++) {
            if (input.charAt(i) == 'x') {
                if (i-1 >=0 ) {
                    char o = input.charAt(i-1);
                    if (!(o == '%' || o == '/' || o == '*' || o == '+' || o == '-' || o == '(')) {
                        expression += "*"+input.charAt(i);
                        continue;
                    }
                }
            }else if (input.charAt(i) == ')') {
                if (i+1 < input.length()) {
                    char o = input.charAt(i+1);
                    if (!(o == '%' || o == '/' || o == '*' || o == '+' || o == '-' || o == ')')) {
                        expression += ")*";
                        continue;
                    }
                }
            }
            expression += input.charAt(i);
        }

        return expression;
    }

}
