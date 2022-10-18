package com.mixtoler.numerical.linearsolvers;

public class Jacobi {
 double a[][];
 double b[];
 double X[];
 double I;
 double Es;
 int SF;

public  double[] JacobiCalc(double A[][],double B[], double x[], double in, double ES, int sf ){
    a=A;
    b=B;
    X=x;
    I=in;
    Es=ES;
    SF=sf;
    double Row;
    double Er[]=new double [X.length];
    double X1[]=new double [X.length];
    boolean NotEnd=true;  //Check Er still Bigger than Es.
    interchange ();
    for(int J=0; J<I &&NotEnd ; J++){//While Es<Er and #of iteration less than max. 
        X1=X.clone();
    for(int i=0; i<X.length; i++){
        NotEnd=false;
        Row=0;
        for(int r=0; r<X.length; r++){
        if (r != i){Row =  round ((  round (a[i][r]) * round (X[r]))+ round (Row)  )   ;} }
        
            X[i]=  round ( round ((b[i]) - round (Row))  / round (( a[i][i] ))  )    ;       //Get new X
            Er[i]=  round( Math.abs( ( X[i]- X1[i] )/ X[i] )) ;  // Get Er
         }
    
    for(int E=0; E<X.length; E++){     
        if(Er[E]>Es ){NotEnd=true;}} //Check Er still Bigger than Es 

    }
    
    return X;
    }

 //      Methode to round numbers        //
     double  round (double  x) {
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

    // this function checks if there is a 0 on the diagonal and modifies it
    public  void interchange (){
	          int n=b.length;
		for (int i=0; i<n; i++) {
			if (a[i][i] == 0) {
                            pivot(i);
				if (a[i][i] == 0) {
					throw new RuntimeException();
				}
                                else{i=0;}
                        }
                }
	}

// Methode Change the row with higher pivot row.
private  void pivot (int i) {
    int n= b.length;
// search for the largest number in the column and get its index		
             int max = i;
		for (int j=i+1;j<n;j++) {
			if (Math.abs(a[j][i]) > Math.abs(a[max][i])) {
				max = j;
			}
		}
		//replace the entire row by the max column row
		double temp1 = b[i];
		b[i] = b[max];
		b[max] = temp1;
		double[] temp = a[i];
		a[i] = a[max];
		a[max] = temp;
	}
    

    
}
