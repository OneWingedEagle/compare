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



public class FieldCircuitTest {


	public static void main(String[] args) {

		
		
		String[] subFolder=new String[3];
		
		String[] data=new String[3];
		data[0]="input";
		data[1]="2D_To_3D";
		data[2]="pre_geom2D.neu";
		
		String[] file=new String[3];
		
		subFolder[0]="release";
		subFolder[1]="MPI para 1";
		subFolder[2]="MPI para 8";
		
		
		String batch="run-mpi1.bat";

		//C:\Users\hassan\Documents\Large Scale Test\FieldSources\domain
	//	cd /d ""C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain"

		//folders[0]="//192.168.12.103/Users/hassan/Documents/Large Scale Test/FieldSources/results/version20151214-verification/non_periodic/static/independent/Igiven/coil/refine 0";
	//	folders[1]="//192.168.12.103/Users/hassan/Documents/Large Scale Test/FieldSources/results/version20151214-verification/non_periodic/static/independent/Igiven/coil/refine 1";
		
      //  String[] cmd = { "mpiexec -n 1 C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain/EMSolBatch_MPI.exe -f C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain/input" };
		
		String rootDirectory="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests/four_coils";
		
		String[][] harc=new String[8][10];
		harc[0][0]="no_periodic";
		harc[0][1]="periodic";
		
		harc[1][0]="linear";
		//harc[1][1]="nonlinear";
		
		harc[2][0]="static";
		harc[2][1]="transient";
		//harc[2][2]="AC";
		
		
		harc[3][0]="independent";
		harc[3][1]="series";
		harc[3][2]="parallel";
		harc[3][3]="series_parallel";
		
		harc[4][0]="Igiven";
		harc[4][1]="Vgiven";
	//	harc[4][2]="I_and_Vgiven";
		
		harc[5][0]="refine0";
	//	harc[5][1]="refine1";
		//harc[5][2]="refine2";
		
		harc[6][0]="REGULAZATION0";
	//	harc[6][1]="REGULAZATION1";
		
		harc[7][0]="release";
		harc[7][1]="MPI para 1";
		harc[7][2]="MPI para 8";
		
      
		File ff=new File(rootDirectory);
		
		
		
	int[] length=new int[harc.length];
		
		for(int i=0;i<harc.length;i++)
			for(int j=0;j<harc[0].length;j++){
				if(harc[i][j]!=null)
					length[i]++;
			}


		int[] counter=new int[harc.length];

		
		int total=1;
		for(int j=0;j<length.length;j++){
			total*=length[j];
		}
			
			util.pr(total);
	
			String[] folderPath=new String[total];
			
			String[] tip=new String[total];
			String[] tipName=new String[total];
			
	
			//-------------------

			int ix=0;
			for(int i=0;i<total;i++){
		
				tip[ix]=rootDirectory;
				tipName[ix]="";


				for(int j=harc.length-2;j>=0;j--){
					if(counter[j+1]==length[j+1]){
						counter[j+1]=0;
						counter[j]++;
					}
					
				}
					

				for(int j=0;j<harc.length;j++){
				
					tip[ix]=tip[ix]+"/"+harc[j][counter[j]];
					if(j<harc.length-1){
					if(j==0)
						tipName[ix]=tipName[ix]+harc[j][counter[j]];
					else
					tipName[ix]=tipName[ix]+"_"+harc[j][counter[j]];
					}
					}
				


				
				//util.pr(tip[ix]);
			//	if(!ff.exists()) ff.mkdirs();
				
				counter[harc.length-1]++;

				ix++;
				
			}
			

			
			String rootDirectory1="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/fourcoilsALl";
			
			for(int i=0;i<total;i++){
				util.pr(tipName[i]);
				
				File ffx=new File(rootDirectory1+"/"+tipName[i]);
				
						if(!ffx.exists()) ffx.mkdirs();
						
			}
				

			
			
			
//==================================
	
		//util.deleteDir(ff);
		//util.removedir(ff);
		// ff.delete();
		
		//if(!ff.exists()) ff.mkdirs();
		
		PrintWriter pw=null;
		
		BufferedReader br=null;

		
		FileReader fr=null;
		
		
		if(1>2)
		try{	
			fr = new FileReader(new File(rootDirectory+"/input"));
			br = new BufferedReader(fr);


			ix=0;
		for(int i=0;i<2+0*total;i++){
	
			tip[ix]=rootDirectory;
			tipName[ix]="";

			for(int j=harc.length-2;j>=0;j--){
				if(counter[j+1]==length[j+1]){
					counter[j+1]=0;
					counter[j]++;
				}
				
			}
			
			for(int j=0;j<harc.length;j++){
			
				tip[ix]=tip[ix]+"/"+harc[j][counter[j]];
				
				tipName[ix]=tipName[ix]+"_"+harc[j][counter[j]];
				}
			
			 ff=new File(tip[ix]);
			 
			 
				
				try{	
					pw = new PrintWriter(new FileWriter(ff+"/input"));
					
					String line="";
					while(line!= null){
						line=br.readLine();
						pw.println(line);
						
					}
					
					pw.close();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				catch(IOException e){e.printStackTrace();
				}


			
			//util.pr(tip[ix]);
		//	if(!ff.exists()) ff.mkdirs();
			
			counter[harc.length-1]++;

			ix++;
			
		}
		
		
		
		br.close();
		
		fr.close();

		
		
				
			}


		catch(IOException e){e.printStackTrace();;
		}


		
	   // new File("C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests\\a\\b\\c\\d\\e").mkdirs();
	  
/*		 String[] cmd = {"run-mpi1.bat" };
        Process p=null;
		try {
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
		int nFold=0;
		
		for( int i=0;i<folderPath.length;i++)
			if(folderPath[i]!=null) nFold++;
		
	
	//	String batch1="cd /d Y:/";




		for( int i=0;i<-nFold;i++){
			 
			for(int j=0;j<subFolder.length;j++){
				String folder=folderPath[i]+"/"+subFolder[j];
				for(int k=0;k<data.length;k++){
	
				 file[k]=folder+"/"+data[k];
		
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


	public  FieldCircuitTest(){}
	
	private void readInput(){

		/*

		try{	
			FileReader fr=new FileReader("input");
			BufferedReader br = new BufferedReader(fr);
			String line;

			line=getNextDataLine(br);

			nFiles=Integer.parseInt(line);

			file=new String[nFiles];
			tip=new String[nFiles];


			for(int nfile=0;nfile<nFiles;nfile++){
				line=getNextDataLine(br);

				if(line!=null) 
					file[nfile]=line;

			}

			for(int i=0;i<nFiles;i++){
				line=getNextDataLine(br);

				if(line!=null)
					tip[i]=line;

			}
			
			
			moduleVersion=new String[nFiles];
			
			for(int i=0;i<nFiles;i++){
				line=getNextDataLine(br);
				if(line!=null)
					moduleVersion[i]=line;

			}




			br.close();
		}

		catch(IOException e){System.err.println("Failed in reading path.");
		}
		*/
	}
	
	private String getNextDataLine(BufferedReader br) throws IOException{


		String line="*";

		while(line!=null && (line.startsWith("*") || line.startsWith("/"))) { line=br.readLine();}

		return line;
	}





}
