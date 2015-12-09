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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



public class LargeScaleTest {

	String regex="[:; . ,\\t]+";
	String regex2="[:;, \\t]+";

	final int nTmax=10000;
	final int nOutputGroupsMax=100;
	final int nVariableIDsMax=100;
	int[] nAvialableOutputGroups;
	int[][] avialableOutputGroupIndex;
	
	int nFiles,nOutputGroups;
	int[] outputId;
	String[][] outputItemId;
	int[][] outputItemIndex;

	String[] file,tip,moduleVersion;

	String[] key;
	boolean[] isRelease;
	int[] nRefine;

	String[][][][][] outputString;
	
	double[][][][][] errorTable;
	
	int[][] stepNumbs;
	int[] nT;
	Vect[] time;
	DataExtractor dex=new DataExtractor();

	int[][] nodeEl;

	String[][] analysisDate;
	int[][] iterationNumber;
	String[][] computationTimeAndDate;


	int nKeys;
	
	PrintWriter stderr;

	public static void main(String[] args) throws WriteException{

		LargeScaleTest x=new LargeScaleTest();

		x.loadOutputs();
		x.writeOutputs();

	}


	public  LargeScaleTest(){

		key=new String[nOutputGroupsMax];
		key[0]="***  Power Sources";
		key[1]="***  Sources ";
		key[2]="***  Network elements";
		key[3]="*            Magnetic fluxes of flux loops";
		key[4]="*** Total magnetic";
		key[5]="*** Total Joule heat";
	

		nKeys=0;

		while(nKeys<nOutputGroupsMax && key[nKeys]!=null){nKeys++;}

		
		try{	
			stderr = new PrintWriter(new FileWriter("stderr"));
			
	}

	catch(IOException e){System.err.println("Failed in reading path.");
	}

	}


	private void loadOutputs(){

		nFiles=0;
		
		this.readInput();

		
		isRelease=new boolean[nFiles];
		nRefine=new int[nFiles];
	//analysisDate=new String[nFiles][11];
		iterationNumber=new int[nFiles][2];
		computationTimeAndDate=new String[nFiles][11];
		
		nAvialableOutputGroups=new int[nFiles];
		avialableOutputGroupIndex=new int[nFiles][nKeys];
		
		nodeEl=new int[nFiles][2];

		for(int nfile=0;nfile<nFiles;nfile++){
			
			isRelease[nfile]=dex.isRelease(file[nfile]);
			iterationNumber[nfile]=dex.getIterNumb(file[nfile]);
			computationTimeAndDate[nfile]=dex.getComputationTimesAndDate(file[nfile]);

			
			if(nfile>0)
				nRefine[nfile]=dex.getRefine(file[nfile]);
			
		for(int i=0;i<nKeys;i++){
			String[][] data=dex.loadDataString(file[nfile],key[i],nVariableIDsMax,nTmax,stderr);
	
			if(data==null) continue;
			
			avialableOutputGroupIndex[nfile][nAvialableOutputGroups[nfile]]=i;
			
			nAvialableOutputGroups[nfile]++;

		}
	}

	if(nOutputGroups==0){
		nOutputGroups=nAvialableOutputGroups[0];
		outputId=new int[nOutputGroups];

		for(int i=0;i<nAvialableOutputGroups[0];i++){
			outputId[i]=avialableOutputGroupIndex[0][i];

		}
		
	}
	
	if(outputItemId==null){
		outputItemId=new String[nOutputGroups][];
		
	}
	outputItemIndex=new int[nOutputGroups][];
	
	

	
	nT=new int[nFiles];



	stepNumbs=new int[nFiles][nTmax];
	time=	new Vect[nFiles];

	
	outputString=new String[nFiles][nOutputGroups][][][];
	//outputIDString=new String[nFiles][nOutputGroups][];

		for(int nfile=0;nfile<nFiles;nfile++){


			time[nfile]=	dex.loadTimesSteps(stepNumbs[nfile], file[nfile],stderr);
			

			if(time[nfile]==null)
			{
				stderr.println();
				stderr.println("Failed in reading timesteps from file "+file[nfile]);
				stderr.println("Please check the file.");
				stderr.close();

				System.exit(1);


			}

			nT[nfile]=time[nfile].length;

			
			for(int i=0;i<outputId.length;i++){

			
				String[][] data=dex.loadDataString(file[nfile],key[outputId[i]-1],nVariableIDsMax,nTmax,stderr);
			
					
				if(outputItemId[i]==null){
					outputItemId[i]=new String[data.length];
					
					
				}
				
				outputItemIndex[i]=new int[data.length];
				
				outputString[nfile][i]=new String[outputItemId[i].length+1][][];
			//	outputIDString[nfile][i]=new String[outputItemId[i].length];
		
				String[] dataSplitted=new String[1];
				for(int j=0;j<data.length;j++){
					dataSplitted=this.splitToStrings(data[j][0]);
		
					outputString[nfile][i][j]=new String[dataSplitted.length-1][nT[nfile]];
			
					if(outputItemId[i][0]!=null){
					for(int k=0;k<outputItemId[i].length;k++){
						if(dataSplitted[0].equals(outputItemId[i][k])){
							outputItemIndex[i][k]=j;
							break;
						}
					}
					}
					else 
						outputItemIndex[i][j]=j;
					


				}

				for(int j=0;j<outputItemId[i].length;j++){
				
					for(int k=0;k<nT[nfile];k++){

				dataSplitted=this.splitToStrings(data[outputItemIndex[i][j]][k]);
		
				for(int p=0;p<dataSplitted.length-1;p++){
					outputString[nfile][i][j][p][k]=dataSplitted[p+1];
				}
			
				
				}
	

			}


		}
		}
		
		
		this.getErrorTable();
		
		
		
		for(int nfile=1;nfile<nFiles;nfile++){

			for(int i=0;i<outputId.length;i++){

				for(int j=0;j<outputItemId[i].length;j++){

					for(int k=0;k<nT[nfile]/20;k++){
						System.out.print(stepNumbs[nfile][k]+" >> \t" );
						for(int p=0;p<errorTable[nfile][i][j].length;p++)
						System.out.print(outputString[0][i][j][p][k]+"\t"+outputString[0][i][j][p][k]+"\t"+errorTable[nfile][i][j][p][k]+"\t");
						
						System.out.println();
						

				}
				
				
				System.out.println("-------------------");
				
				
				}
				System.out.println("=======================");
			}
			}

	}



private void writeOutputs() throws  WriteException{

	
/*	String[][][][] table=new String[nFiles][nOutputGroups][][];


	PrintWriter[] outputWriter=new  	PrintWriter[nFiles];
	FileWriter fr=null;
	try{


		for(int nfile=0;nfile<nFiles;nfile++){
			file[nfile]=file[nfile];
			

				 fr=new FileWriter("dataOut"+nfile);
				outputWriter[nfile] = new PrintWriter(fr);	
		}
		
		
		
		fr.close();
		for(int nfile=0;nfile<nFiles;nfile++)
		outputWriter[nfile].close();


	}

	catch(IOException e){stderr.println(e.getMessage());
	}*/
	String path="dataOutputs"+".xls";

	WritableWorkbook workbook;
	try {
		workbook = Workbook.createWorkbook(new File(path));
		

		WritableSheet sheet = workbook.createSheet("comparison",0);
		
			this.fillSheet(sheet,stepNumbs[0],time[0],outputString[0][0][0]);


	
for(int nfile=0;nfile<nFiles;nfile++){
	
	String title=tip[nfile];
	int kx=0;
	for(int j=0;j<nfile;j++)
		if(tip[j].equals(title)){
			kx++;
			title=title+kx;
		}
		
		 sheet = workbook.createSheet(title,nfile+1);
		this.fillSheet(sheet,stepNumbs[nfile],time[nfile],outputString[nfile][0][0]);
	
}


workbook.write();


workbook.close();
	


	} catch (IOException e) {
	
		e.printStackTrace();
	} 

		

			
	}


private int[] getCSInt(String line){
	String[] sp=line.split(regex);	
	int L=sp.length;
	int[] v=new int[L];
	for( int p=0;p<L;p++)
		v[p]=Integer.parseInt(sp[p]);

	return v;
}

private String[] splitToStrings(String line){
	
	String[] sp=line.split(regex2);
	int ib=0;
	if(sp[0].equals("")) ib=1;
	
	int L=sp.length-ib;
	String[] splitted=new String[L];
	for(int i=0;i<L;i++)
		splitted[i]=sp[i+ib];
	
	return splitted;
}

private String getNextDataLine(BufferedReader br) throws IOException{
	

		String line="*";
		
		while(line!=null && (line.startsWith("*") || line.startsWith("/"))) { line=br.readLine();}
		
		return line;
}

private void readInput(){
	

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
			
			
			String[] lines=new String[100];
			
			int ix=0;
			
			String line1="";
			while(line1!=null){
				line1=getNextDataLine(br);
				
				lines[ix]=line1;

				ix++;
			}
			
			int numbLines=ix;
			for(int i=0;i<numbLines;i++)
				if(lines[i].equalsIgnoreCase("version")) {
					moduleVersion=new String[nFiles];
					for(int j=0;j<nFiles;j++){
					moduleVersion[j]=lines[i+j+1];
					
					}
					
					break;
			}
			

			for(int i=0;i<numbLines;i++){
				if(lines[i]==null) break;
				if(lines[i].equalsIgnoreCase("outputs")) {
				
					outputId=getCSInt(lines[i+1]);
					nOutputGroups=outputId.length;
					ix++;
					break;
				}
			}
			
/*			if(outputId==null){
				outputItemId=new String[nKeys][];
				outputId=new int[nKeys];
				for(int j=0;j<nKeys;j++){
					outputId[j]=j+1;
						
				}
				
			nOutputGroups=outputId.length;
			}*/
			
			//outputItemId=new String[nOutputGroups][];
			boolean ended=false;
			for(int i=0;i<numbLines;i++){/*	
				if(lines[i]==null) break;
				if(lines[i].equalsIgnoreCase("IDs")) {
					for(int j=0;j<nOutputGroups;j++){
						String line2=lines[i+1+j];
						if(line2==null || line2.equals("")){
							break;
						}
						String[] item=splitToStrings(line2);
						
						util.pr("-"+item[0]+"-");
		
						int idi=Integer.parseInt(item[0])-1;
						outputItemId[idi]=new String[outputItemId.length-1];
						
						for(int k=0;k<outputItemId[idi].length;k++)
						{
							outputItemId[idi][j]=item[j];
						}

						
					}
					if(ended) break;
				}
				*/}
				
			
			///=== all
			
			outputItemId=new String[1][];
			outputId=new int[1];
			for(int j=0;j<1;j++){
				outputId[j]=j+1;
					
			}
			
		nOutputGroups=outputId.length;
			
			
			
			//=========

		
			
		

	br.close();
}

catch(IOException e){System.err.println("Failed in reading path.");
}
}

private void getErrorTable(){
	
	int fRef=0;
	
	errorTable=new double[nFiles][nOutputGroups][][][];
	for(int nfile=0;nfile<nFiles;nfile++){

		for(int i=0;i<nOutputGroups;i++){
			errorTable[nfile][i]=new double[outputString[nfile][i].length][][];
			for(int j=0;j<outputString[nfile][i].length;j++)
				errorTable[nfile][i][j]=new double[outputString[nfile][i][j].length][nT[fRef]];
		}
	}
	

	int fRef1=0,fRef2=1;

	double[] errorSum=new double[nFiles];

	double[] errorMax=new double[nFiles];
	int[][] errorMaxCoord=new int[nFiles][3];

	boolean[] formatErr=new boolean[nFiles];

	////---------------
	for(int i=0;i<nOutputGroups;i++){
		//if(outputString[fRef][i][0].length<3) continue;


		for(int j=0;j<outputString[fRef][i].length;j++)
			
			for(int p=0;p<outputString[fRef][i][j].length;p++){


			double data=0,data0=0;

			boolean empty=(outputString[fRef][i][j][p][0]==null); 
			
			if(empty){
				for(int k=0;k<outputString[fRef][i][j][p].length;k++)
					for(int nfile=1;nfile<nFiles;nfile++)
					errorTable[nfile][i][j][p][k]=-1;
				
			}else
			for(int k=0;k<outputString[fRef][i][j][p].length;k++){

				for(int nfile=1;nfile<nFiles;nfile++){
					
					if(nfile==1) fRef=fRef1;
					else  fRef=fRef2;

								
			
					boolean numb0=util.isNumeric(outputString[fRef][i][j][p][k]);  
				

					if(numb0)
						data0=Double.parseDouble(outputString[fRef][i][j][p][k]);  
			
				

					boolean numb=util.isNumeric(outputString[nfile][i][j][p][k]);  
					
					
					if(numb)
						data=Double.parseDouble(outputString[nfile][i][j][p][k]);  
					

					if(!numb0 || !numb){
						
						errorTable[nfile][i][j][p][k]=-1;

						if(!formatErr[nfile])
							formatErr[nfile]=true;
					}
					else{

						double err=0;
						if(Math.abs(data0)>1e-10){
							err=Math.abs(data-data0)/Math.abs(data0)*100;
						}
						else{

							if(Math.abs(data-data0)<1e-10)
								err=0;
						}



						err=Math.floor(err*1e6)/1e6;

						errorTable[nfile][i][j][p][k]=err;
					
							if(err>errorMax[nfile]){
								errorMax[nfile]=err;
								errorMaxCoord[nfile][0]=i;
								errorMaxCoord[nfile][1]=stepNumbs[fRef][j];
								errorMaxCoord[nfile][2]=k;

							}

						errorSum[nfile]+=err;

						errorSum[nfile]=Math.floor(errorSum[nfile]*10000)/10000;

					}

				}

			}

		}


	}

	

}

public void fillSheet(WritableSheet sheet,int[] stetNumb,Vect time, String[][] table) throws RowsExceededException, WriteException, IOException{
	


	int nCol=0;
	for(int i=0;i<table.length;i++){
		if(table[i][0]!=null && !table[i][0].equals("null"))
			nCol++;
	}

	int clm=0,rw=2;
	Number number=null;
	int nRow=table[0].length;

	for(int p=0;p<nRow;p++){
		number= new Number(clm,p+rw,stetNumb[p]);
		sheet.addCell(number);
		number= new Number(clm+1,p+rw,time.el[p]);
		sheet.addCell(number);
	}
	
	clm+=2;

	//Number[][] number=new Number[nRow][nCol];
	for(int p=0;p<nRow;p++)
		for(int q=0;q<nCol;q++){
			
			//number[p][q]= new Number(p,q,p+q);
			try{
				 number= new Number(clm+q,p+rw,Double.parseDouble(table[q][p]));
			}
			catch(NumberFormatException e){
				
			}
		sheet.addCell(number);
	}
	
	}



}
