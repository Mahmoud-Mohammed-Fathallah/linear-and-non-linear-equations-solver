package com.mixtoler.numerical.nonlinearsolvers;


import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
public class FixedPoint {
	private String equ;
	private int iteration_max;
	private int sig;
	private double tolerence;
	private double xr;
	private String g;
	
	
	public FixedPoint(String equ, int iteration_max, int sig, double tolerence, double xr) {
		this.equ = equ;
		this.iteration_max = iteration_max;
		this.sig = sig;
		this.tolerence = tolerence;
		this.xr = xr;
		this.g = getG();
	}
	
	private double round (double x) {
		if (sig == -1) {
			return x;
		}
		double temp = x;
		int count = 0;
		while (Math.abs(temp)>=1) {
			temp /= 10;
			count++;
		}
		int sigFig_modified = sig - count;
		double scale = Math.pow(10, sigFig_modified);
		x = Math.round(x * scale) / scale;
		
		
		return x;
	}
	
	private double sub (String eq, double value)  {
		
		// substitute the value of xr into the equation 
		Argument x = new Argument("x = "+value);
		double answer = 0;
		Expression e = new Expression(eq, x);
		answer = round(e.calculate());
		
		return answer;
	}
	
	private String getG () {
		/*
		 
		 f(x) = 0     a * f(x) = 0   x + a * f(x) = x
		 hence g(x) = x + a * f(x)
		 
		 for it to converge we need g`(x) < 1  we chose g`(x) to be 0.1
		 so a = (0.1-1) (because the derivative of x = 1) / f`(x)
		 
		 
		 */
		String der = "der(" + equ + ", x)";
		String result = "";
		String argu = "x = "+xr;
		Argument x = new Argument(argu);
		Expression e = new Expression(der, x);
		double check = round(e.calculate());
		double factor;
		// if the derivative is 0 we change a to 1 because we cant device by 0
		if (Math.abs(check) > 1E-5) {
			factor = round((0.00001 - 1) / check);
		}
		else {
			factor = 1;
		}
		if (factor >= 0)
			result = "x + " + factor +" * "+equ;
		else
			result = "x" + factor +" * "+equ;
		return result;
	}
	
	
	
	public String[] solve () {
		equ = "(" + equ + ")";
		g = getG();
		double xr_new = 0;
		double eps = 1;
		int k = 0;
		
		while (eps > tolerence && k<iteration_max) {
			// at each iteration we sub the value of new xr into g(x) until convergence
			g = getG();
			xr_new = sub(g, xr);
			eps = Math.abs((xr_new-xr)) / Math.abs(xr_new);
			xr = xr_new;
			k++;
		}
		
		String[] res = new String[2];
		res[0] = Double.toString(xr);
		res[1] = g;
		
		return res;
		
	}
}