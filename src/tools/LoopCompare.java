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



public class LoopCompare {

	String regex="[:; . ,\\t]+";
	String caseFolder,execDir;
	
	String[] dataFolders,tip,moduleVersion;
	int nBegin,nEnd,nFiles;
	
	DataExtractor dex=new DataExtractor();
	

	public  LoopCompare(){

	};


	public static void main(String[] args) throws IOException{
		
		
		boolean controlFile=true;
	
		
		if(controlFile){
			
			
		
			
			
			LoopCompare x=new LoopCompare();
			
			x.execDir=System.getProperty("user.dir");
			
		//	x.execDir="C:\\Users\\hassan\\Documents\\Large Scale Test\\FieldSources\\domain";

			
			x.readInput();
			

			
			x.doLoopCompare();
		}

		else{
		 

		LoopCompare x=new LoopCompare();

		//x.deleteData();
		
		x.caseFolder="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/fourcoilsAll/refine0";
		
		
		x.doLoopComparebb(1,1);
		}



	}


	
	public  void doLoopCompare() throws IOException{

		File rootFolder=new File(caseFolder);

		
		String[] folderNames = rootFolder.list();
		Arrays.sort(folderNames);

		double[] errs=null;

		LargeScaleTest ls=new LargeScaleTest();


		String[] lines=new String[1000];

		int kx=0;

		for(int ifold1=nBegin;ifold1<=nEnd;ifold1++){


			int ifold=ifold1-1;

			String caseFolder=rootFolder.getPath()+"/"+folderNames[ifold];
			
			File caseFodlerFile=new File(caseFolder);
			
		//	String[] subFolders = caseFodlerFile.list();
			//Arrays.sort(subFolders);
			


			
			boolean[] completed=new boolean[nFiles];

			

			String[] files1=new String[nFiles];
			String[] tips1=new String[nFiles];
			String[] versions1=new String[nFiles];

		
			for(int k=0;k<nFiles;k++){
				versions1[k]=this.moduleVersion[k];
				files1[k]=caseFodlerFile.getAbsolutePath()+"/"+dataFolders[k]+"/output";
				
				String[] sp=dataFolders[k].split(regex);
				int ib=0;
				if(sp[0].equals("")) ib=1;
				String tip="";
				for(int i=ib;i<sp.length;i++)
					tip=tip+sp[i];
				
				tips1[k]=tip;
				
			}



			String[][] messages=new String[nFiles][2];
			

			
			int nx=0;
			for(int i=0;i<nFiles;i++){
				File ff=new File(files1[i]);

				completed[i]=false;

				if(ff.exists()){

					String[] timeDate=dex.getComputationTimesAndDate(files1[i]);
					if(timeDate[11]!=null){
						completed[i]=true;
						nx++;
					}
				}
			}

			String[] files2=new String[nx];
			String[] tips2=new String[nx];
			String[] versions2=new String[nx];

			int ix=0;
			for(int i=0;i<nFiles;i++)
				if(completed[i]) {
					files2[ix]=files1[i];
					
					tips2[ix]=tips1[i];
					versions2[ix]=versions1[i];

					ix++;

				}


			try {

			

				//errs=ls.compare(files1, tips1, versions1,comparecaseFolder);
			//	util.show(files2);
				errs=ls.compare(files2, tips2, versions2,caseFolder);

			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			messages[0][0]=String.format("%75s", folderNames[ifold]+": ");
			//getClass().fs1.format(names[ifold]);
			lines[kx]=messages[0][0];

			for(int ie=1;ie<errs.length;ie++){

				if(ie==1)
					messages[ie][0]=String.format("%30s",tips2[ie]+"/"+tips2[0]+":  ");
				else
					messages[ie][0]=String.format("%30s",tips2[ie]+"/"+tips2[1]+":  ");

				messages[ie][1]=String.format("%f",errs[ie]);


				lines[kx]=lines[kx]+messages[ie][0]+messages[ie][1];


			}

			util.pr(lines[kx]);

			kx++;

		}


		util.write(execDir+"/compare_log.txt", lines);


	}

	
	private void readInput(){



		try{	
			FileReader fr=new FileReader(execDir+"\\loopPath");
			BufferedReader br = new BufferedReader(fr);
			String line;

			line=util.getNextDataLine(br);

			caseFolder=new String(line);
			
			line=util.getNextDataLine(br);
			nFiles=Integer.parseInt(line);
			
			dataFolders=new String[nFiles];

			tip=new String[nFiles];
			
			for(int k=0;k<nFiles;k++){
				line=util.getNextDataLine(br);

				dataFolders[k]=new String(line);
			}

			line=util.getNextDataLine(br);
			int[] numbs=util.getCSInt(line);
			
			nBegin=numbs[0];
			nEnd=numbs[1];

			
			this.moduleVersion=new String[nFiles];
			
			line=util.getNextDataLine(br);
			moduleVersion[0]=new String(line);
			
			line=util.getNextDataLine(br);

			for(int k=1;k<nFiles;k++){
				moduleVersion[k]=new String(line);
			}



			br.close();

			fr.close();

	
		}

		catch(IOException e){System.err.println("Failed in reading loopPath.");
		}
	}



	public  void doLoopComparebb(int nBegin, int nEnd) throws IOException{




		File caseFolderFodler=new File(caseFolder);
		String[] names = caseFolderFodler.list();


		double[] errs=null;

		LargeScaleTest ls=new LargeScaleTest();


		int nFiles=5;

		String[] files1=new String[nFiles];
		String[] tips1=new String[nFiles];
		String[] versions1=new String[nFiles];

		versions1[0]="r.11.6";
		for(int k=1;k<nFiles;k++)
			versions1[k]="r.4.2.2 (20151225)";

		tips1[0]="release";
		tips1[1]="para_2";
		tips1[2]="para_4";
		tips1[3]="para_8";
		tips1[4]="para_16";


		String[][] messages=new String[nFiles][2];

		String[] lines=new String[1000];

		int kx=0;

		for(int ifold1=nBegin;ifold1<=nEnd;ifold1++){


			int ifold=ifold1-1;

			boolean[] completed=new boolean[nFiles];

			String comparecaseFolder=caseFolder+"/"+names[ifold];
			
			//comparecaseFolder="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/SLIDE TESTS 2015.12.23/SPM-nonlinear/disp1/3D_FGCE_periodic_symm0_nonlinear_transient_indep_Igiven_refine0_REG1";

			
			files1[0]=comparecaseFolder+"/release/output";
			files1[1]=comparecaseFolder+"/MPI para 2/output";
			
			
			files1[2]=comparecaseFolder+"/MPI para 4/output";
			files1[3]=comparecaseFolder+"/MPI para 8/output";

			files1[4]=comparecaseFolder+"/MPI para 16/output";
			
			int nx=0;
			for(int i=0;i<nFiles;i++){
				File ff=new File(files1[i]);

				completed[i]=false;

				if(ff.exists()){

					String[] timeDate=dex.getComputationTimesAndDate(files1[i]);
					if(timeDate[11]!=null){
						completed[i]=true;
						nx++;
					}
				}
			}

			String[] files2=new String[nx];
			String[] tips2=new String[nx];
			String[] versions2=new String[nx];

			int ix=0;
			for(int i=0;i<nFiles;i++)
				if(completed[i]) {
					files2[ix]=files1[i];
					tips2[ix]=tips1[i];
					versions2[ix]=versions1[i];

					ix++;

				}


			try {

			

				//errs=ls.compare(files1, tips1, versions1,comparecaseFolder);
				errs=ls.compare(files2, tips2, versions2,comparecaseFolder);
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			messages[0][0]=String.format("%75s", names[ifold]+": ");
			//getClass().fs1.format(names[ifold]);
			lines[kx]=messages[0][0];

			for(int ie=1;ie<errs.length;ie++){

				if(ie==1)
					messages[ie][0]=String.format("%30s",tips2[ie]+"/"+tips2[0]+":  ");
				else
					messages[ie][0]=String.format("%30s",tips2[ie]+"/"+tips2[1]+":  ");

				messages[ie][1]=String.format("%f",errs[ie]);


				lines[kx]=lines[kx]+messages[ie][0]+messages[ie][1];


			}

			util.pr(lines[kx]);

			kx++;

		}


		util.write(execDir+"/compare_log.txt", lines);


	}

	






}



