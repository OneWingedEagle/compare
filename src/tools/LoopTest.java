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

import org.math.plot.Plot2DPanel;

import jxl.write.WriteException;



public class LoopTest {

	DataExtractor dex=new DataExtractor();

	String regex="[:; . ,\\t]+";

	boolean[] success;
	
	boolean inputsReady=false;
	
	boolean twoMeshes=false;

	String[][] inputMesh=new String[2][2];
	static int geom=1;


	

	String caseFolder,execDir;
	
public  LoopTest(){

		inputMesh[1][0]="pre_geom2D.neu";
		inputMesh[0][0]="pre_geom.neu";
		
		inputMesh[1][1]="rotor_mesh2D.neu";
		inputMesh[0][1]="rotor_mesh.neu";


	};
	

	public  LoopTest(String exDir,String rr){
		
		this.execDir=exDir;

		caseFolder=rr;
		
		inputMesh[1][0]="pre_geom2D.neu";
		inputMesh[0][0]="pre_geom.neu";
		
		inputMesh[1][1]="rotor_mesh2D.neu";
		inputMesh[0][1]="rotor_mesh.neu";


	};


	public static void main(String[] args) throws IOException{
		

		
		LoopTest x=new LoopTest();
		
		x.runHere();
		
	//	x.runFromControl();

	}
	
private  void runHere() throws IOException{
		

	
		this.execDir="C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain";
		

		this.caseFolder="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/SLIDE TESTS 2015.12.23/SPM-nonlinear/disp1/refine0";
		this.caseFolder="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/PHICOIL TEST 201609";
		
	//	this.caseFolder="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/REGULATIZATION-ANTISYM";
		
		

			
	//	execDir=System.getProperty("user.dir");
			
			boolean overWrite=true;
			boolean inputReady=false;
			
			int nDomains=0;
			int nBegin=17;
			int nEnd=17;
	
		doLoopTest(nBegin,nEnd,nDomains,overWrite,inputReady);
		
		//x.doLoopTest(13,13,0,true,true);
		
		//x.doLoopCompare(11, 11);
		

	}

	
private  void runFromControl() throws IOException{
		
		execDir=System.getProperty("user.dir");
			
			boolean overWrite=false,meshReady=false, inputReady=false;
			int nDomains=1,nBegin=1,nEnd=1;
			

			try{	
				FileReader fr=new FileReader(execDir+"/control");
				BufferedReader br = new BufferedReader(fr);
				String line;

				line=util.getNextDataLine(br);
				
				caseFolder=new String(line);
			
				line=util.getNextDataLine(br);
				
				int[] numbs=util.getCSInt(line);
				
			

			
			if(numbs.length>0){

					nDomains=numbs[0];
			}
			
			if(numbs.length>1)
				nBegin=numbs[1];
			
			if(numbs.length>2)
				nEnd=numbs[2];
		
				
				line=util.getNextDataLine(br);
				
				 numbs=util.getCSInt(line);
				
			
			if(numbs.length>0){
				if(numbs[0]==1)

					overWrite=true;
			}
			
			if(numbs.length>1){
				if(numbs[1]==1)
					meshReady=true;
				
				}
			
			if(numbs.length>2){
				if(numbs[2]==1)
					inputReady=true;
				}

			
			if(nDomains==0)
				inputReady=true;

				
				br.close();

	fr.close();

		
			}

			catch(IOException e){System.err.println("Failed in reading control file.");		}
			
		
		//	LoopTest x=new LoopTest(execDir,caseFolder);
			
			doLoopTest(nBegin,nEnd,nDomains,overWrite,inputReady);
			

	}


	public  void doLoopTestCRelease(int nBegin, int nEnd,boolean overWrite,boolean inputReady) throws IOException{
		doLoopTest(nBegin, nEnd, 0, overWrite,inputReady);
	}

	public  void doLoopTestSeq(int nBegin, int nEnd,int nDomains,boolean overWrite,boolean inputReady) throws IOException{
		doLoopTest(nBegin, nEnd, -nDomains,overWrite,inputReady);
	}


	public  void doLoopTest(int nBegin, int nEnd,int nDomains, boolean overWrite,boolean inputReady) throws IOException{

		//Runtime.getRuntime().exec("cmd /c start");


		boolean seq=(nDomains<0);
		if(seq)
		nDomains=-nDomains;


		File caseFolderFodler=new File(caseFolder);
		String[] names = caseFolderFodler.list();
		
	//	Arrays.sort(names);
		  Arrays.sort(names, new WindowsExplorerStringComparator());
		//util.show(names);

		int nMaxFold=500;


		success=new boolean[nMaxFold];

		String[] runSucess=new String[nMaxFold];


		String sc="";


		int ifold=0;


		for(int ifold1=nBegin;ifold1<=nEnd;ifold1++){
			
			//if((ifold1-1)%4>1) continue;
			
			ifold=ifold1-1;

			String sourceFolder=caseFolder+"/"+names[ifold]+"/release";

			String destFolder=null;

			if(nDomains==0)
				destFolder=caseFolder+"/"+names[ifold]+"/release";
			else if(!seq)
				destFolder=caseFolder+"/"+names[ifold]+"/MPI para "+nDomains;
			else 
				destFolder=caseFolder+"/"+names[ifold]+"/seq."+(nDomains);



			File ff=new File(destFolder+"/output");

			success[ifold]=false;

			if(ff.exists()){

				String[] timeDate=dex.getComputationTimesAndDate(destFolder+"/output");
				if(timeDate[11]!=null)
					success[ifold]=true;
			}
			

		if(overWrite || !success[ifold])	
			{	
			 System.out.print("  Running for "+names[ifold]+" ....");
			 
				if(nDomains==0)
					success[ifold]=this.runCRelease(sourceFolder);
				else 
					success[ifold]=this.runLS(nDomains,sourceFolder,destFolder,inputReady,seq);

				 System.out.print("\r");

			}



			if(success[ifold]) sc="Completed";
			else sc="Failed";

			if(nDomains==0)
				runSucess[ifold]=names[ifold]+"/release : "+sc;
			else if(!seq)
				runSucess[ifold]=names[ifold]+"/MPI para "+nDomains+" : "+sc;
			else
				runSucess[ifold]=names[ifold]+"/seq."+nDomains+" : "+sc;


			util.pr(runSucess[ifold]);


		}

		if(nDomains==0)
			util.write(execDir+"\\states_CRelease", runSucess);
		else if(!seq)
			util.write(execDir+"\\states_MPI_para"+nDomains, runSucess);
		else
			util.write(execDir+"\\states_Sequential"+nDomains, runSucess);

		//x.test();

	}


	
	
	
	public  boolean runLS(int nDomains, String sourceFolder, String destFolder, boolean inputReady,boolean seq){

		boolean success=false;

		String sourceFolderX=new String(sourceFolder);
		
		if(inputReady){
			sourceFolderX=new String(destFolder);
			
		}
		

		File source1=new File(sourceFolderX+"/input");


		
		if(!source1.exists()) {
			System.err.println(" no input file in "+sourceFolderX);

			return success;
		}
		
		File dest1=new File(execDir+"/input");
		
		
		int geomx=new RunMPI().getGeomCode(source1);
		
		if(geomx>0) geom=1;
		else geom=0;


		File source2=new File(sourceFolderX+"/"+inputMesh[geom][0]);
		File dest2=new File(execDir+"/"+inputMesh[geom][0]);

		File source3=new File(sourceFolderX+"/2D_to_3D");
		File dest3=new File(execDir+"/2D_to_3D");


		File dest4=new File(destFolder+"/check");


		File dest5=new File(destFolder+"/output");


	
		File dest7=new File(destFolder+"/exec_log.txt");


		File source8=new File(sourceFolderX+"/"+inputMesh[geom][1]);
		File dest8=new File(execDir+"/"+inputMesh[geom][1]);
		
		
		if(dest1.exists()) dest1.delete();
		if(dest2.exists()) dest2.delete();

		if(dest3.exists()) dest3.delete();


		if(source8.exists())
			if(dest8.exists()) dest8.delete();
		
		

		try{
		util.copyFile(source1, dest1);
		
		util.copyFile(source2, dest2);
		
		//util.hardLink(source2, dest2);
		
		if(source3.exists())
		util.copyFile(source3, dest3);

		if(source8.exists())
			util.copyFile(source8, dest8);
		
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}


		
		
		

		try {
		
			RunMPI rmpi=new RunMPI(execDir, caseFolder);
			rmpi.twoMeshes=this.twoMeshes;
			
			success=rmpi.runMPI(nDomains,inputReady,seq );

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		File fdestFolder2=new File(destFolder);
		if(!fdestFolder2.exists()) fdestFolder2.mkdirs();



		try {
			
			if(!inputReady){

				source1=new File(execDir+"/input");
				dest1=new File(destFolder+"/input");
				

				util.copyFile(source1, dest1);
	
				source2=new File(execDir+"/"+inputMesh[geom][0]);
				dest2=new File(destFolder+"/"+inputMesh[geom][0]);

				source3=new File(execDir+"/2D_to_3D");
				dest3=new File(destFolder+"/2D_to_3D");
				
				if(source8.exists())
					dest8=new File(destFolder+"/"+inputMesh[geom][1]);
				
		
				
				//util.pr(dest1.getPath());
				util.copyFile(source2, dest2);
				
				if(source3.exists())
				util.copyFile(source3, dest3);
				
				if(source8.exists())
					util.copyFile(source8, dest8);
				}
		

			File source4=new File(execDir+"/check");
			 dest4=new File(destFolder+"/check");

			File source5=new File(execDir+"/output");
			 dest5=new File(destFolder+"/output");
			 
				File source6=new File(execDir+"/stderr");
				File dest6=new File(sourceFolder+"/stderr");

			File source7=new File(execDir+"/exec_log.txt");
			 dest7=new File(destFolder+"/exec_log.txt");

			
			util.copyFile(source4, dest4);
			util.copyFile(source5, dest5);
			
			util.copyFile(source6, dest6);
			util.copyFile(source7, dest7);
					

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





		return success;

	}	





	public  boolean runCRelease(String sourceFolder){


		boolean success=false;

		//sourceFolder=caseFolder+"/"+names[ifold]+"/release";

		File source1=new File(sourceFolder+"/input");
		File dest1=new File(execDir+"/input");
		
		

		if(!source1.exists()) {
			System.err.println(" no input file in "+sourceFolder);

			return success;
		}
		
		int geomx=new RunMPI().getGeomCode(dest1);
		
		if(geomx>0) geom=1;
		else geom=0;
		
		

		File source2=new File(sourceFolder+"/"+inputMesh[geom][0]);
		File dest2=new File(execDir+"/"+inputMesh[geom][0]);

		File source3=new File(sourceFolder+"/2D_to_3D");
		File dest3=new File(execDir+"/2D_to_3D");

		File source8=new File(sourceFolder+"/"+inputMesh[geom][1]);
		File dest8=new File(execDir+"/"+inputMesh[geom][1]);



		try {

			util.copyFile(source1, dest1);
			util.copyFile(source2, dest2);
			if(source3.exists())
			util.copyFile(source3, dest3);

			if(source8.exists())
				util.copyFile(source8, dest8);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}




		ProcessBuilder builder = new ProcessBuilder();

		//builder.command("EMSolBatchCversion_x64.exe","input");
		builder.command(execDir+"/EMSol.exe","-b","-m","-f","input");

		builder.directory(new File(execDir));

		


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

		pp.destroy();

		File source4=new File(execDir+"/check");
		File dest4=new File(sourceFolder+"/check");

		File source5=new File(execDir+"/output");
		File dest5=new File(sourceFolder+"/output");

		File source6=new File(execDir+"/stderr");
		File dest6=new File(sourceFolder+"/stderr");



		try {
			util.copyFile(source4, dest4);
			util.copyFile(source5, dest5);
			util.copyFile(source6, dest6);




			String[] timeDate=dex.getComputationTimesAndDate(sourceFolder+"/output");
		

			if(timeDate[11]!=null){
				success=true;
			}
			else{
				success=false;

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return success;


	}	


	
	public  void distributeData() throws IOException{





		File caseFolderFodler=new File(caseFolder);
		String[] names = caseFolderFodler.list();


		//String[] states=util.read(execDir+"\states");

		double[][] HB=util.loadArrays(24, 2, "BH");
		double[][][] BHs=new double[1][][];
		BHs[0]=HB;
		//util.show(HB);

		int action=3;

		int LL=96;

		boolean distrbuteModel=false;

		LL=0;

		for(int ifold=65;ifold<=-128;ifold++){

			String sourceFolder=caseFolder+"/"+names[ifold]+"/release";
			String destFolder=caseFolder+"/"+names[ifold+LL]+"/release";

			File fsource=new File(sourceFolder+"/input");
			File ftemp=new File(execDir+"/inputTemp1");

			File fdest=new File(destFolder+"/input");


			if(action==0){


				util.copyFile(fsource, fdest);

			}


			if(action==1){

				this.setToNolinPart1(fsource, ftemp, 1, 1);

				fsource=new File(ftemp.getPath());
				ftemp=new File(execDir+"/inputTemp2");

				this.setToNolinPart2(fsource, ftemp, 1, 1);




				fsource=new File(ftemp.getPath());

				ftemp=new File(execDir+"/inputTemp3");

				this.setTimeEvolution(fsource, ftemp, 1, 10);


				fsource=new File(ftemp.getPath());


				this.addBHdata(fsource,fdest,BHs);


			}

			else if(action==2){


				this.setPeriodicPart1(2,0,90.0,0,fsource, ftemp);

				fsource=new File(ftemp.getPath());


				this.setPeriodicPart2(fsource, fdest);

			}
			if(action==3){



				File ff=new File( sourceFolder=caseFolder+"/"+names[ifold]+"/release/output");
				if(ff.exists()) ff.delete();

				ff=new File( sourceFolder=caseFolder+"/"+names[ifold]+"/MPI para 1/output");
				if(ff.exists()) ff.delete();

				ff=new File( sourceFolder=caseFolder+"/"+names[ifold]+"/MPI para 8/output");
				if(ff.exists()) ff.delete();


				String[] elcur={"ELMCUR     4      1   0",
						"* MAT_ID * IN_SURFACE * OUT_SURFACE * CURENT * SIGMA",
				"24         3        5             150        50000000"};


				ftemp=new File(execDir+"/inputTemp1");
				fsource=new File( sourceFolder=caseFolder+"/"+names[ifold]+"/MPI para 1/input");
				this.replacePHICOILwithELMCUR(fsource,fdest,elcur);

				//util.copyFile(ftemp, fdest);

			}




			if(distrbuteModel){

				fsource=new File(sourceFolder+"/"+inputMesh[geom][0]);
				fdest=new File(destFolder+"/"+inputMesh[geom][0]);
				util.copyFile(fsource, fdest);

				fsource=new File(sourceFolder+"/2D_to_3D");
				fdest=new File(destFolder+"/2D_to_3D");

				util.copyFile(fsource, fdest);
			}


		}

	}




	public void test(){



		File caseFolderFodler=new File(caseFolder);

		String[] names = caseFolderFodler.list();

		int ix=0;
		for(String name : names)
		{

			util.pr(name);
			/*    if (new File(execDir+"/domain/C:\\Windows\\" + name).isDirectory())
		    {
		        System.out.println(name);
		    }
			 */
		}

	}

	//************************************************



	public   String getFile(int mode,String message){
		String filePath="";
		FileDialog fd;
		Frame f=new Frame();
		if(mode==0)
			fd= new FileDialog(f,message,FileDialog.LOAD);
		else
			fd= new FileDialog(f,message,FileDialog.SAVE);
		fd.setVisible(true);
		fd.toFront();
		String Folder=fd.getDirectory();
		String File = fd.getFile();
		if(Folder!=null && File!=null)
		{

			filePath=Folder+"\\"+File;

		}
		f.dispose();
		fd.dispose();

		return filePath;
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


				if(line.contains("CYCLIC")){
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



	private void replacePHICOILwithELMCUR(File source, File dest,String[] dataLine){

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

				line=util.dropLeadingSpaces(line);

				if(manip && !line1.startsWith("*")){


					for(int k=0;k<dataLine.length;k++){
						pw.println(dataLine[k]);
						line=br.readLine();
					}


					manip=false;

				}



				if(line.startsWith("* PHICOIL *")){
					pw.println("* ELMCUR * SERIES_NO * NO_MAT_IDS * OPTION *");
					manip=true;
				}
				else
					pw.println(line);


			}

			fr2.close();

			pw.close();

			br.close();
			fr.close();


		}

		catch(IOException e2){e2.printStackTrace();}
	}




	public  void deleteData() throws IOException{





		File caseFolderFodler=new File(caseFolder);
		String[] names = caseFolderFodler.list();
		Arrays.sort(names);



		for(int ifold=1;ifold<=128;ifold++){

			String sourceFolder=caseFolder+"/"+names[ifold]+"/release";
			String destFolder=caseFolder+"/"+names[ifold]+"/release";

			//File fff=new File(sourceFolder=caseFolder+"/"+names[ifold]+"/release/output");

			int[] nn=dex.getIterNumb(sourceFolder=caseFolder+"/"+names[ifold]+"/release/output");

			int[] nn2=dex.getIterNumb(sourceFolder=caseFolder+"/"+names[ifold]+"/MPI para 1/output");
			util.pr(ifold+"   "+nn[1]+"   "+nn2[1]);
			//	if(nn[1]==0)
			//	util.deleteDir(fff);

			/* fff=new File(caseFolder+"/"+names[ifold]+"/MPI para 1");
			util.deleteDir(fff);

			 fff=new File(caseFolder+"/"+names[ifold]+"/MPI para 8");
				util.deleteDir(fff);*/

			//util.pr(fff.getPath());

			//util.pr(sourceFolder);


		}

	}





}



