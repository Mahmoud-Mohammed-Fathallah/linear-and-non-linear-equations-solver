package com.mixtoler.numerical.linearsolvers;

import java.math.BigDecimal;
import java.math.MathContext;

public class LUDecomposition {
    protected double[][] A;
    protected double[] b;
    protected int n;
    protected int precision;
    protected double[][] L;
    protected double[][] U;
    protected double[] y;
    protected double[] x;
    protected int[] piv;

    public void setPrecision(int precision) {
        this.precision = precision;
    }


    public LUDecomposition(int n, double[][] A, double[] b, int precision){
        this.A = A;
        this.b = b;
        this.n = n;
        this.precision = precision;
        this.L = new double[n][n];
        this.U = new double[n][n];
        this.x = new double[n];
        this.y = new double[n];
        this.piv = new int[n];
        //this.s = new double[n];
        for (int i = 0; i < n ; i++) {
            piv[i] = i;
        }

    }

    // solves the linear system of equations if there exists a solution
    // apply forward and backward substitution to the L and U matrices to get the solution
    // to the system of linear equations
    // params:
    // method = true for Crout & method = false for DoLittle
    // returns true if solvable and false if not
    public boolean solve(boolean method){
        if(method){
            decompCroutForm();
        }else {
            decompDoLittleFormWithPivoting();
        }
        if(!isNonsingular()){
            return false;
        }
        forwardSub();
        backSub();
        return true;
    }


    // returns the solution of SLE
    public double[] solution(){
        return x;
    }

    // decompose the given matrix A into L and U matrices using Crout's method
    public void decompCroutForm(){
        for (int i = 0; i < n; i++) {
            L[i][0] = A[i][0];
        }
        for (int i = 1; i < n; i++) {
            U[0][i] = A[0][i]/L[0][0];
        }
        for (int i = 0; i < n; i++) {
            U[i][i] = 1;
        }
        for (int j = 1; j < n-1; j++) {
            for (int i = j; i < n ; i++) {
                double sum = 0;
                for (int k = 0; k < j; k++) {
                    sum += round(L[i][k] * U[k][j],precision);
                }
                L[i][j] = round(A[i][j] - sum,precision);
            }
            for (int k = j+1; k < n; k++) {
                double sum = 0;
                for (int i = 0; i < j ; i++) {
                    sum += round(L[j][i] * U[i][k],precision);
                }
                U[j][k] = round((A[j][k]-sum) / L[j][j],precision);
            }
        }
        double sum = 0;
        for (int k = 0; k < n ; k++) {
            sum += round(L[n-1][k] * U[k][n-1],precision);
        }
        L[n-1][n-1] = round( A[n-1][n-1] - sum,precision);
    }

    // decompose the given matrix A into L and U matrices using DoLittle's method
    public void decompDoLittleForm(){
        for (int i = 0; i < n; i++)
        {
            pivot(i);
            // finding U matrix
            for (int k = i; k < n; k++)
            {
                // Summation of L[i][j] * U[j][k]
                double sum = 0;
                for (int j = 0; j < i; j++){
                    sum += round(L[i][j] * U[j][k],precision);
                    //sum += L[i][j] * U[j][k];
                }
                // finding U[i][k]
                U[i][k] = round(A[i][k] - sum,precision);
            }
            // finding L matrix
            for (int k = i; k < n; k++)
            {
                {
                    // Summation of L[k][j] * U[j][i]
                    double sum = 0;
                    for (int j = 0; j < i; j++){
                        sum += round(L[k][j] * U[j][i],precision);
                        //sum += L[k][j] * U[j][i];
                    }
                    // finding L[k][i]
                    L[k][i] = round((A[k][i] - sum) / U[i][i],precision);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            L[i][i] = 1;
        }
    }

    public void decompDoLittleFormWithPivoting(){
        for (int i=1; i<n; i++) {
            pivot(i-1);
            for (int k=0; k<n-i; k++) {
                double factor =round( A[i+k][i-1] / A[i-1][i-1],precision);
                L[i+k][i-1] = factor;
                for (int j=0; j<n; j++) {
                    A[i+k][j] -= round(factor * A[i-1][j],precision);
                    if (Math.abs(A[i+k][j]) < 0.001) {
                        A[i+k][j] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            L[i][i] = 1;
        }
        U = A;
    }

    // checks if the system has a unique solution
    public boolean isNonsingular() {
        for (int j = 0; j < n; j++) {
            if (U[j][j] == 0 || L[j][j] == 0)
                return false;
        }
        return true;
    }

    // calculate x matrix which is the solution of the system of linear equations
    private void backSub() {
        double sum;
        for (int i=n-1; i>=0; i--) {
            sum = 0;
            for (int j=0; j<n; j++) {
                if (j == i) continue;
                sum += round(U[i][j] * x[j],precision);
                //sum += U[i][j] * x[j];
            }
            x[i] = round((y[i] - sum)/U[i][i],precision);
        }

    }


    // get intermediate y matrix
    private void forwardSub(){
        double sum ;
        for (int i = 0; i < n; i++) {
            sum = 0;
            for (int j = 0; j < n ; j++) {
                if(i == j) continue;
                sum += round( L[i][j] * y[j],precision);
                //sum += L[i][j] * y[j];
            }
            y[i] =round ((b[i] - sum)/L[i][i],precision);
        }
    }

    // round the double to a given number of significant figures(precision)
    public double round(double number,int precision){
        BigDecimal numberBD = new BigDecimal(number);
        numberBD = numberBD.round(new MathContext(precision));
        number = numberBD.doubleValue();
        return number;
    }

    private void pivot (int i) {
        int max = i;
        for (int j=i+1;j<n;j++) {
            if (Math.abs(A[j][i]) > Math.abs(A[max][i])) {
                max = j;
            }
        }
        if(max != i){
            double temp1 = b[i];
            b[i] = b[max];
            b[max] = temp1;
            double[] temp = A[i];
            A[i] = A[max];
            A[max] = temp;
            int tempP = piv[i];
            piv[i] = piv[max];
            piv[max] = tempP;
            for (int j = 0; j < i; j++) {
                double tempL = L[i][j];
                L[i][j] = L[max][j];
                L[max][j] = tempL;
            }
        }

    }







}
