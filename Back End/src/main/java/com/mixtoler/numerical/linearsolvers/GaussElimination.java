package com.mixtoler.numerical.linearsolvers;

public class GaussElimination {
	protected double[][] A;
	protected double[] b;
	protected int n;
	protected int sigFig;
	protected double[] result;
	double time;
	
	public GaussElimination(int n, double[][] A, double[] b, int sigFig) {
		this.n= n;
		this.A = A;
		this.b = b;
		this.sigFig = sigFig;
		this.result = new double[n];
	}
	public double getTime() {
		return this.time;
	}
	protected double round (double x) {
		double temp = x;
		int count = 0;
		while (Math.abs(temp)>=1) {
			temp /= 10;
			count++;
		}
		int sigFig_modified = sigFig - count;
		double scale = Math.pow(10, sigFig_modified);
		x = Math.round(x * scale) / scale;
		
		
		return x;
	}
	
	private void pivot (int i) {
		int max = i;
		// search for the largest number in the column and get its index
		for (int j=i+1;j<n;j++) {
			if (Math.abs(A[j][i]) > Math.abs(A[max][i])) {
				max = j;
			}
		}
		//replace the entire row by the max column row
		double temp1 = b[i];
		b[i] = b[max];
		b[max] = temp1;
		double[] temp = A[i];
		A[i] = A[max];
		A[max] = temp;
	}
	
	
	protected void getREF () {
		for (int i=1; i<n; i++) {
			pivot(i-1);
			for (int k=0; k<n-i; k++) {
				// the factor is the number that we want to eliminate/the pivot
				if (A[i-1][i-1] == 0) throw new RuntimeException();
				double factor = round(A[i+k][i-1] / A[i-1][i-1]);
				for (int j=0; j<n; j++) {
					// do the same chane to the whole row and the vector of constants
					A[i+k][j] = round(A[i+k][j] - round(factor * A[i-1][j]));
				}
				b[i+k] = round(b[i+k] - round(factor * b[i-1]));
			}
		}
	}
	
	private void backSub() {
		double sum = 0;
		for (int i=n-1; i>=0; i--) {
			// calculate the sum of the row except for the pivot we want to compute and then divide by
			// the constant
			sum = 0;
			for (int j=0; j<n; j++) {
				if (j == i) continue;
				sum = round(sum + round(A[i][j] * result[j]));
			}
			if (A[i][i] == 0) throw new RuntimeException();
			result[i] = round((round(b[i] - sum))/A[i][i]);
		}
		
	}
	public double[] solve () {
		// time is in milliseconds
		time = (double)System.nanoTime();
		getREF();
		backSub();
		time = ((double)System.nanoTime() - time) / 1000000;
		return result;
	}
	
	
}

