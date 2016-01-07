package tools;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.List;
import java.awt.Toolkit;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class RunMPI {

	DataExtractor dex=new DataExtractor();

	String regex="[:; . ,\\t]+";


	int refine=0;
	String[][] inputMesh=new String[2][2];
	static int geom=1;



	String caseFolder,execDir;

	public  RunMPI(String exDir,String rr){

		this.execDir=exDir;

		caseFolder=rr;


		inputMesh[1][0]="pre_geom2D.neu";
		inputMesh[0][0]="pre_geom.neu";

		inputMesh[1][1]="rotor_mesh2D.neu";
		inputMesh[0][1]="rotor_mesh.neu";


	};


	public static void main(String[] args) throws IOException{





		/*		String rr="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/fourcoilsAll/refine0";


		rr="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/SLIDE TESTS 2015.12.23/SPM-nonlinear/disp1/refine0";
		 */
		//String	rr="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/SLIDE TESTS 2015.12.23/SPM_tetra/refine0";


		// execDir=System.getProperty("user.dir");
		//	String execDir="C:/Users/hassan/Documents/Large Scale Test/FieldSources";
		String execDir="C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain";

		execDir=System.getProperty("user.dir");

		RunMPI x=new RunMPI(execDir,"");

		//x.deleteData();

		int nDomains=1;
		if(args.length>0)
			nDomains=Integer.parseInt(args[0]);


		x.runMPI(nDomains,false);
		//x.doLoopCompare(5,5);

	}



	public  boolean runMPI(int nDomains, boolean inputReady) throws IOException{

		boolean success=false;


		File input=new File(execDir+File.separator+"input");

		int geomx=this.getGeomCode(input);
		
		if(geomx>0) geom=1;
		else geom=0;
		
		
		if(!inputReady){
			boolean periodic=this.isPeriodic(input);

			
			if(periodic){
			
				File tmp=new File(execDir+File.separator+"input1");
			this.removeSepAng(input, tmp);
			util.copyFile(tmp, input);
			}
			
	
				

		boolean slide=this.isSlide(input);

		if(!slide){
			String replacement1="         5            0          "+nDomains+"        0        1      "+refine;
			
			File input2=new File(execDir+File.separator+"input2");
			this.setNO_Mesh(input, input2,replacement1);
			util.copyFile(input2, input);
		}

		else{

			String replacement1=null;
			if(nDomains>2)
				replacement1="         5            0          "+2+"        0        1      "+refine;
			else
				replacement1="         5            0          "+2+"        0        0      "+refine;

			File input2=new File(execDir+File.separator+"input2");
			this.setNO_Mesh(input, input2,replacement1);

			util.copyFile(input2, input);


			String replacement2="         1         1         0         0         0         0         "+nDomains+"         0";


			this.setNO_Mesh(input, input2,replacement2);

			util.copyFile(input2, input);

		}


		}



		File file1=null;

		File file2=null;
		File file3=null;

		File file4=null;


		File rotFile=new File(inputMesh[geom][1]);
		
		




		for(int j=1;j<nDomains;j++){

			String domainx=new File(execDir).getParent()+"/domain"+j;

			File df=new File(domainx);
			if(!df.exists()) df.mkdirs();

			file1=new File(domainx+"/input");


			file2=new File(domainx+"/"+inputMesh[geom][0]);

			file3=new File(domainx+"/2D_to_3D");

			if(rotFile.exists())
				file4=new File(domainx+"/"+inputMesh[geom][1]);




			try {

				util.copyFile(new File(execDir+File.separator+"input"), file1);

				util.copyFile(new File(inputMesh[geom][0]), file2);


				if(geomx==4){
					
					File source3=new File(execDir+File.separator+"2D_to_3D");
					util.copyFile(source3, file3);
				}


				if(rotFile.exists())
					util.copyFile(rotFile, file4);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}



		}



		File logFile=new File(execDir+File.separator+"exec_log.txt");



		String[] cmdAndArgs = {"cmd.exe","/C","mpiexec.exe", "-n",Integer.toString(nDomains),execDir+"/EMSolBatch_MPI.exe","-f","input"};




		ProcessBuilder builder = new ProcessBuilder(cmdAndArgs);


		/*	ProcessBuilder builder = new ProcessBuilder();



		//builder.command("EMSolWin.exe",execDir+"/domain/input");
		builder.command(execDir+"/domain/EMSolWin.exe","-b","-m","-f","input");
		 */

		builder.directory(new File(execDir));



		builder.redirectOutput(Redirect.to(logFile));



		Process pp = null;
		try {
			pp = builder.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Process has started here
		try {
			pp.waitFor();


		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try {
			// i = 143
			int i = pp.exitValue();

		} catch( IllegalThreadStateException e){
			e.printStackTrace();
		}






		//	String[] timeDate=dex.getComputationTimesAndDate(execDir+"/domain/output");
		String[] timeDate=dex.getComputationTimesAndDate(execDir+File.separator+"output");

		if(timeDate[11]!=null){

			success=true;
		}
		else{
			success=false;

		}
/*		
		if(success)
			util.pr("Completed.");
		else
			util.pr("Failed.");*/



		pp.destroy();


		return success;

	}	





	private void setNO_Mesh(File source, File dest,String replacement){

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();
				if(line==null) break;

				if(manip && !line.startsWith("*")){
					line=replacement;
					/*			String[] sp=line.split(regex);
					int ib=0; if(sp[0].equals("")) ib=1;
					String nMesh=Integer.toString(nDomains);

					sp[ib+2]=nMesh;
					line="";

					for(int j=0;j<sp.length;j++ )

						line=line+sp[j]+"\t";*/

					manip=false;

				}

				pw.println(line);


				if(line.startsWith("* INPUT_MESH_FILE")){
					manip=true;
				}

			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}

	private void setNO_Paras(File source, File dest,String replacement){

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();
				if(line==null) break;

				if(manip && !line.startsWith("*")){
					line=replacement;
					/*			String[] sp=line.split(regex);
					int ib=0; if(sp[0].equals("")) ib=1;
					String nMesh=Integer.toString(nDomains);

					sp[ib+2]=nMesh;
					line="";

					for(int j=0;j<sp.length;j++ )

						line=line+sp[j]+"\t";*/

					manip=false;

				}

				pw.println(line);


				if(line.startsWith("* NODE_ORDER * EDGE_ORDER *")){
					manip=true;
				}

			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}


	private void setToNolinPart2(File source,File dest,int matID,int BHID){


		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";

			int ix=0;


			boolean manip=false;

			while(line!=null){

				line=br.readLine();
				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){

					String[] matIDx=line.split(regex);
					int ib=0; if(matIDx[0].equals("")) ib=1;

					if(matIDx[ib].equals(Integer.toString(matID))){
						matIDx[ib+2]=Integer.toString(BHID);


						line="     ";

						for(int j=ib;j<matIDx.length;j++ )
							line=line+matIDx[j]+"         ";

					}

					manip=false;

				}

				pw.println(line);


				if(line.startsWith("* MAT_ID  *  POTENTIAL *")){
					manip=true;
				}




			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}

	private void setToNolinPart1(File source,File dest,int matID,int BHID){


		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";
			int ix=0;


			boolean manip=false;

			while(line!=null){

				line=br.readLine();
				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){

					String[] ss=line.split(regex);
					int ib=0; if(ss[0].equals("")) ib=1;

					ss[ib]="0";
					ss[ib+5]="1";


					line="     ";

					for(int j=ib;j<ss.length;j++ )
						line=line+ss[j]+"         ";



					manip=false;

				}

				pw.println(line);


				if(line.startsWith("* M-STATIC * STEP ")|| line.startsWith("* STATIC * STEP ")){
					manip=true;
				}



			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}

	private void setTimeEvolution(File source,File dest,int tID,double fact){


		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";

			int ix=0;


			boolean manip=false;

			while(line!=null){

				line=br.readLine();
				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){

					String[] tIDx=line.split(regex);
					int ib=0; if(tIDx[0].equals("")) ib=1;

					pw.println(line);




					if(tIDx[ib].equals(Integer.toString(tID))){
						line=br.readLine();
						line1=util.dropLeadingSpaces(line);

						while(line1.startsWith("*")){

							pw.println(line);
							line=br.readLine();

						}


						String[] sp2=line.split(regex);
						ib=0; if(sp2[0].equals("")) ib=1;

						for(int j=ib;j<sp2.length;j++)
							sp2[j]=Double.toString(Double.parseDouble(sp2[j])*fact);


						line="    ";

						for(int j=ib;j<sp2.length;j++ )
							line=line+sp2[j]+"         ";

					}


					manip=false;


				}

				pw.println(line);


				if(line.startsWith("* TIME_ID *	OPTION	*")){
					manip=true;
				}



			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}


	private void addBHdata(File source,File dest,double[][][] BH){


		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";
			int ix=0;


			boolean manip=false;

			while(line!=null){

				line=br.readLine();
				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){


					line=Integer.toString(BH.length);
					pw.println(line);


					for(int i=0;i<BH.length;i++){

						line="* BH_CURVE_ID * NO_DATA *";
						pw.println(line);
						line=(i+1)+"\t"+BH[i].length;
						pw.println(line);


						for(int j=0;j<BH[i].length;j++ ){
							line=BH[i][j][0]+"\t"+BH[i][j][1];
							pw.println(line);
						}


					}

					manip=false;
					break;

				}




				if(line.startsWith("* NO_BH_CURVES *")){
					manip=true;
				}

				pw.println(line);


			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}

	private void setPeriodicPart1(int cyc,int sym, double deg,double sepAng,File source, File dest){

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();

				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){
					String[] sp=line.split(regex);
					int ib=0; if(sp[0].equals("")) ib=1;

					sp[ib]=Integer.toString(cyc);

					sp[ib+1]=Integer.toString(sym);;

					line="";

					for(int j=0;j<sp.length;j++ )
						line=line+sp[j]+"\t";

					pw.println(line);

					line="* ANGLE *";
					pw.println(line);

					line=Double.toString(deg);{

						if(sepAng>0){
							pw.println(line);
							line=Double.toString(sepAng);

						}

					}

					manip=false;

				}



				pw.println(line);


				if(line.startsWith("*  CYCLIC  *  SYMMETRY")){
					manip=true;
				}

			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}

	private void removeSepAng(File source, File dest){

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";

			int ix=0;

			boolean manip=false;
			while(line!=null ){
				line=br.readLine();

				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip ){

					if(!line1.startsWith("*"))
						line=br.readLine();



					manip=false;

				}



				pw.println(line);


				if(line.startsWith("* SEPERATE_ANGLE(deg)")){

					manip=true;
				}

			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}


	private void setPeriodicPart2(File source, File dest){

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);

			FileWriter fr2=new FileWriter(dest);
			PrintWriter pw = new PrintWriter(fr2);

			String line="";
			String line1="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();

				if(line==null) break;

				line1=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){

					String[] sp=line.split(regex);
					int ib=0;
					if(sp[0].equals("")) ib=1;

					int nB=Integer.parseInt(sp[ib]);
					pw.println("0");

					ib=0;
					while(ib<nB)
					{

						line=br.readLine();
						line1=util.dropLeadingSpaces(line);

						if(!line1.startsWith("*")){
							line="*"+line;
							ib++;
							if(ib<nB)
								pw.println(line);

						}

					}



					manip=false;

				}

				pw.println(line);


				if(line.startsWith("* NO_BN0_PLANES *")){
					manip=true;
				}

			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}






	private int getGeomCode(File source){
		int geom=1;
		

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);



			String line="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();
			
				if(line==null) break;

				if(manip && !line.startsWith("*")){

					double[] numbs=util.getCSV(line);
					geom=(int)(numbs[0]);
					break;



				}



				if(line.startsWith("* GEOMETRY")){
					manip=true;
				}

			}



			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}


		return geom;

	}


	private boolean isSlide(File source){

		boolean slide=false;

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);



			String line="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();
				if(line==null) break;

				if(manip && !line.startsWith("*")){
					
					String line2=util.dropLeadingSpaces(line);

					int[] numbs=util.getCSInt(line2);
					if(numbs[4]>0)
						slide=true;
					break;



				}



				if(line.contains("TRANSIENT * DISPLACEMENT *")){
					manip=true;
				}

			}



			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}


		return slide;

	}
	
	private boolean isPeriodic(File source){

		boolean periodic=false;

		try{
			FileReader fr=new FileReader(source);
			BufferedReader br = new BufferedReader(fr);



			String line="";

			int ix=0;

			boolean manip=false;
			while(line!=null){
				line=br.readLine();
				if(line==null) break;

				if(manip && !line.startsWith("*")){
					
					String line2=util.dropLeadingSpaces(line);

					int[] numbs=util.getCSInt(line2);
					if(numbs[0]>0)
						periodic=true;
					break;



				}



				if(line.contains("*  CYCLIC  *")){
					manip=true;
				}

			}



			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}


		return periodic;

	}


}



