package com.mixtoler.numerical.linearsolvers;


public class GaussJordan extends GaussElimination {
	
	
	
	public GaussJordan(int n, double[][] A, double[] b, int sigFig) {
		super(n, A, b, sigFig);
	}
	public double getTime () {
		return this.time;
	}
	private void getRREF(){
		for (int i=n-1; i>0; i--) {
			// start from the bottom row and start eliminating its above numbers
			for (int k=1; k<i+1; k++) {
				// the factor is the number that we want to eliminate/the pivot
				if (A[i][i] == 0) throw new RuntimeException();
				double factor = (round(A[i-k][i]) / round(A[i][i]));
				for (int j=0; j<n; j++) {
					// do the same chane to the whole row and the vector of constants
					A[i-k][j] = round(round(A[i-k][j]) - round(factor * round(A[i][j])));
				}
				b[i-k] = round(round(b[i-k]) - round(factor * round(b[i])));
			}
		}
		// divide by the pivot to get the final answer
		for (int i=0; i<n; i++) {
			if (A[i][i] == 0) throw new RuntimeException();
			b[i] = round(b[i] / A[i][i]);
			A[i][i] = 1;
			result[i] = b[i];
		}
		
	}
	
	
	
	@Override
	public double[] solve () {
		// time is in milliseconds
		time = System.nanoTime();
		getREF();
		getRREF();
		time = (System.nanoTime() - time) /1000000;
		return result;
		
	}
}
