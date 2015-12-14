package compare;


import java.io.BufferedReader;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


//import math.Vect;


import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.concurrent.Executor;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



public class LoopTest {


	public static void main(String[] args) throws WriteException{
		
		String[] folders=new String[10];
		String[] subFolder=new String[3];
		
		String[] data=new String[3];
		data[0]="input";
		data[1]="2D_To_3D";
		data[2]="pre_geom2D.neu";
		
		String[] file=new String[3];
		
		subFolder[0]="release";
		subFolder[1]="MPI para 1";
		subFolder[2]="MPI para 8";
		
		
		String batch="mpiexec -n 1 //192.168.12.103/Users/hassan/Documents/Large Scale Test/FieldSources/domain/ EMSolBatch_MPI.exe -f input";

		
	//	cd /d ""C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain"

		folders[0]="//192.168.12.103/Users/hassan/Documents/Large Scale Test/FieldSources/results/version20151214-verification/non_periodic/static/independent/Igiven/coil/refine 0";
	//	folders[1]="//192.168.12.103/Users/hassan/Documents/Large Scale Test/FieldSources/results/version20151214-verification/non_periodic/static/independent/Igiven/coil/refine 1";
		
		int nFold=0;
		
		for( int i=0;i<folders.length;i++)
			if(folders[i]!=null) nFold++;
		
	
	//	String batch1="cd /d Y:/";
	//	Runtime.getRuntime().exec("sh -c batch1");
		
		  try {
			//	Runtime r=Runtime.getRuntime();
/*			    r.exec("cmd.exe /c start ", null,  
			            new File("Y:\\domain"));          // working directory 
*/			
				Runtime.getRuntime().exec("cmd.exe cd /d Y:\\domain");

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
		for( int i=0;i<-nFold;i++){
			for(int j=0;j<subFolder.length;j++){
				String folder=folders[i]+"/"+subFolder[j];
				for(int k=0;k<data.length;k++){
	

				 file[k]=folder+"/"+data[k];
				 util.pr(batch);
				 
				    try {
						Runtime.getRuntime().exec(batch);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		}
		
		

	}


	public  LoopTest(){}





}
