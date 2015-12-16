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
	String[][] outputItemEntity;
	int[][] outputItemIndex;

	String[] file,tip,moduleVersion;

	String[] key;
	int[] nLineAfter;
	String[] outputTitles;
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
	
	
	double[][] errorMaxOfGroups;
	double[] errorMax;
	int[][] errorMaxCoord;
	String[][] errorMaxID;
	String[][] errorMaxData;

	boolean[] formatErr;
	
	int[] wantedOutputId;
	String[][] wantedOutputItemId;
	int  nWantedOutputGroups;


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
		
		
		nLineAfter=new int[nOutputGroupsMax];
		nLineAfter[0]=1;
		nLineAfter[1]=1;
		nLineAfter[2]=1;
		nLineAfter[3]=4;
		nLineAfter[4]=1;
		nLineAfter[5]=1;
		

		outputTitles=new String[nOutputGroupsMax];

		outputTitles[0]="Power Sources";
		outputTitles[1]="Field Sources";
		outputTitles[2]="Network elements";
		outputTitles[3]="Magn. fluxes";
		outputTitles[4]="Magn. energies";
		outputTitles[5]="Joule heats";


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


		this.readOuputStrings();


		this.getErrorTable();


	}



	private void writeOutputs() throws  WriteException{

		String path="camparison"+".xls";
		
		setWriteable(path);


		WritableWorkbook workbook;
		try {
			
			workbook = Workbook.createWorkbook(new File(path));
			
			
			WritableSheet summarySheet=workbook.createSheet("summary",0);
			
			
			Label label=null;
			Number number=null;
		
			int rw=1;
			int clm=0;
			
			label=new Label(clm,rw,"Max errors (%):");
			summarySheet.addCell(label);

			rw++;
			for(int nfile=1;nfile<nFiles;nfile++){
	/*			String title="";
				if(nfile==1) title=tip[nfile]+" against "+tip[0];
				else title=tip[nfile]+" against "+tip[1];*/
				if(nfile==1)
				label=new Label(clm,rw,tip[1]+"/"+tip[0]+" :");
				else
					label=new Label(clm,rw,tip[nfile]+"/"+tip[1]+" :");
				
				summarySheet.addCell(label);
				rw++;
				
				number=new Number(clm+1,rw,errorMax[nfile]);
				summarySheet.addCell(number);
				
				int index=avialableOutputGroupIndex[nfile][errorMaxCoord[nfile][0]];
				label=new Label(clm+3,rw,"in: "+outputTitles[index]);
				summarySheet.addCell(label);
				label=new Label(clm+5,rw,"ID No.: "+errorMaxID[nfile][0]);
				summarySheet.addCell(label);
				label=new Label(clm+7,rw,"quantity: "+errorMaxID[nfile][1]);
				summarySheet.addCell(label);
				
				
				label=new Label(clm+10,rw,"step: ");
				summarySheet.addCell(label);
				number=new Number(clm+11,rw,errorMaxCoord[nfile][2]);
				summarySheet.addCell(number);
				
				label=new Label(clm+13,rw," compared data : ");
				summarySheet.addCell(label);
				label=new Label(clm+15,rw,errorMaxData[nfile][0]);
				summarySheet.addCell(label);

				rw++;
				
							
			}
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			label=new Label(clm,rw,"Max errors of output groups (%):");
			summarySheet.addCell(label);

			rw++;
			
			for(int nfile=1;nfile<nFiles;nfile++){
				if(nfile==1)
					label=new Label(clm+3*nfile,rw,tip[1]+"/"+tip[0]+" :");
					else
						label=new Label(clm+3*nfile,rw,tip[nfile]+"/"+tip[1]+" :");
					
					summarySheet.addCell(label);
				
			}
			

			for(int i=0;i<nOutputGroups;i++){

		
				rw++;
				
				label=new Label(clm+1,rw,outputTitles[avialableOutputGroupIndex[0][i]]+" : ");
				
				summarySheet.addCell(label);
				
				for(int nfile=1;nfile<nFiles;nfile++){
					number=new Number(clm+3*nfile,rw,errorMaxOfGroups[nfile][i]);
					summarySheet.addCell(number);
				
			
				}
			//	
				
				
				
			/*	int index=avialableOutputGroupIndex[i][errorMaxCoord[i][0]];
				label=new Label(clm+3,rw,"in: "+outputTitles[index]);
				summarySheet.addCell(label);
				label=new Label(clm+5,rw,"ID No.: "+errorMaxID[i][0]);
				summarySheet.addCell(label);
				label=new Label(clm+7,rw,"quantity: "+errorMaxID[i][1]);
				summarySheet.addCell(label);
				
				
				label=new Label(clm+10,rw,"step: ");
				summarySheet.addCell(label);
				number=new Number(clm+11,rw,errorMaxCoord[i][2]);
				summarySheet.addCell(number);
				
				label=new Label(clm+13,rw," compared data : ");
				summarySheet.addCell(label);
				label=new Label(clm+15,rw,errorMaxData[i][0]);
				summarySheet.addCell(label);
*/
			
				
							
			}
			rw++;
			
			
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;

			label=new Label(clm,rw,"Result location:");
			summarySheet.addCell(label);
			rw++;
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+":");
				summarySheet.addCell(label);
				label=new Label(clm+2,rw,file[nfile]);
				summarySheet.addCell(label);
				
				rw++;
				
			}

			rw++;

			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;

			label=new Label(clm,rw,"Analysis date:");
			summarySheet.addCell(label);

			rw++;
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,"\t\t"+tip[nfile]+":\n");
				summarySheet.addCell(label);
				
				rw++;
				
				label=new Label(clm,rw,"       Start at : "+computationTimeAndDate[nfile][9]);
				summarySheet.addCell(label);
				
				rw++;
				
				label=new Label(clm,rw,"    Completed at: "+computationTimeAndDate[nfile][11]);
				summarySheet.addCell(label);
				
				rw++;
				
			}
			
			rw++;
	

			// == module version
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			label=new Label(clm,rw,"Module version: ");
			summarySheet.addCell(label);

			rw++;
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+" :");
				summarySheet.addCell(label);
				
				label=new Label(clm+1,rw,moduleVersion[nfile]);
				summarySheet.addCell(label);
				
				rw++;
				
							
			}
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			label=new Label(clm,rw,"Refine: ");
			summarySheet.addCell(label);

			rw++;
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+" :");
				summarySheet.addCell(label);
				
				number=new Number(clm+1,rw,nRefine[nfile]);
				summarySheet.addCell(number);
				
				rw++;
				
							
			}
			
			rw++;
			
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
		

			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			label=new Label(clm,rw," Number of ements:");
			summarySheet.addCell(label);
		
			rw++;
			
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+" : "+nodeEl[nfile][1]);
				summarySheet.addCell(label);
				 rw++;
			
						
			}
			
			rw++;
			
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			label=new Label(clm,rw," Number of nodes:");
			summarySheet.addCell(label);
		
			rw++;
			
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+" : "+nodeEl[nfile][0]);
				summarySheet.addCell(label);
				 rw++;
			
						
			}
			
			
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;
			
			
			label=new Label(clm,rw,"Number of iterations:");
			summarySheet.addCell(label);
		
			rw++;
			
			for(int nfile=0;nfile<nFiles;nfile++){

				label=new Label(clm,rw,tip[nfile]+":");
				summarySheet.addCell(label);
				 rw++;
				 rw++;
				label=new Label(clm+1,rw,"Total ICCG:");
				summarySheet.addCell(label);
				
				number=new Number(clm+3,rw,iterationNumber[nfile][0]);
				summarySheet.addCell(number);
				 rw++;
				 
					label=new Label(clm+1,rw,"Total Nonlinear:");
					summarySheet.addCell(label);
					
					number=new Number(clm+3,rw,iterationNumber[nfile][1]);
					summarySheet.addCell(number);
					 rw++;
			
						
			}
			
		
			label=new Label(0,rw,"____________________________________________________________________________");
			summarySheet.addCell(label);
			rw++;

			label=new Label(clm,rw,"Total computation time:");
			summarySheet.addCell(label);
		
			rw++;
			
			for(int nfile=0;nfile<nFiles;nfile++){
				label=new Label(clm,rw,tip[nfile]+":");
				summarySheet.addCell(label);
				
				String[] sp;
				if(computationTimeAndDate[nfile][1]!=null){
				sp=this.splitToStrings(computationTimeAndDate[nfile][1],regex2);

				number=new Number(clm+2,rw,Double.parseDouble(sp[sp.length-3]));
				summarySheet.addCell(number);
			}
				else
				{
					label=new Label(clm+2,rw,"null");
					summarySheet.addCell(label);
				}
			
				 rw++;		
						
			}
			
			rw++;
			
			WritableSheet[] sheet=new WritableSheet[nOutputGroups*10];

			int fRef=0;

			int index=0;
			
			int isheet=0;
			
			for(int i=0;i<nOutputGroups;i++){
				index=avialableOutputGroupIndex[0][i];
		
			
			 int L=0;
			 for(int nfile=0;nfile<nFiles;nfile++)
			 for(int j=1;j<outputItemId[i].length;j++){
				 L+=(outputString[nfile][i][j].length-1);
			 }
			 
		
			 
			 int Lerr=0;
			 for(int nfile=1;nfile<nFiles;nfile++)
			 for(int j=1;j<outputItemId[i].length;j++){
				 Lerr+=(outputString[nfile][i][j].length-1);
			 }
			 
			 
			 
			 String[][] table=new String[nT[0]][L];
			 String[][] titles=new String[3][L];
			 
			 double[][] errTable=new double[nT[0]][Lerr];
			 String[][] errTitles=new String[3][Lerr];
			

			
			 int col;
	
				 col=0;

				 
				
					 for(int j=1;j<outputString[fRef][i].length;j++)
						 for(int p=1;p<outputString[fRef][i][j].length;p++){
							 for(int nfile=0;nfile<nFiles;nfile++)
							 titles[0][col++]=outputItemId[i][j];
						 }
				 
				 col=0;
				 
					 for(int j=1;j<outputString[fRef][i].length;j++)
						 for(int p=1;p<outputString[fRef][i][j].length;p++){
							 for(int nfile=0;nfile<nFiles;nfile++)
							 if(outputString[nfile][i][0][p][0].equals("Amplitude(Current)"))
								titles[1][col++]="Current";
							 else
							    titles[1][col++]=outputString[nfile][i][0][p][0];
							 
						 }
				 col=0;
				
					 for(int j=1;j<outputString[fRef][i].length;j++)
						 for(int p=1;p<outputString[fRef][i][j].length;p++){
							 for(int nfile=0;nfile<nFiles;nfile++)
							 titles[2][col++]=tip[nfile];
						 }

		

			 for(int k=0;k<table.length;k++)
			 {
				 col=0;
				
				 for(int j=1;j<outputString[fRef][i].length;j++)
					 for(int p=1;p<outputString[fRef][i][j].length;p++){
						 for(int nfile=0;nfile<nFiles;nfile++)
				 		 table[k][col++]=outputString[nfile][i][j][p][k];
					 }
			 }
			 

				 col=0;
				 for(int nfile=1;nfile<nFiles;nfile++)
				 for(int j=1;j<outputString[nfile][i].length;j++)
					 for(int p=1;p<outputString[nfile][i][j].length;p++){
				 		 errTitles[0][col++]=outputItemId[i][j];
					 }
				 
				 col=0;
				 
				 for(int j=1;j<outputString[fRef][i].length;j++)
					 for(int p=1;p<outputString[fRef][i][j].length;p++){
						 for(int nfile=1;nfile<nFiles;nfile++)
						 if(outputString[nfile][i][0][p][0].equals("Amplitude(Current)"))
							 errTitles[1][col++]="Current";
						 else
							 errTitles[1][col++]=outputString[nfile][i][0][p][0];
						 
					 }
				 
				 col=0;
			
				 for(int j=1;j<outputString[fRef][i].length;j++)
					 for(int p=1;p<outputString[fRef][i][j].length;p++){
						 for(int nfile=1;nfile<nFiles;nfile++)
						 if(nfile==1)
							 errTitles[2][col++]=tip[nfile]+"/"+tip[0];
						 else
							 errTitles[2][col++]=tip[nfile]+"/"+tip[1];
					 }
	
			 
			 for(int k=0;k<table.length;k++)
			 {
				 col=0;
				
				 for(int j=1;j<outputString[fRef][i].length;j++)
					 for(int p=1;p<outputString[fRef][i][j].length;p++){
						 for(int nfile=1;nfile<nFiles;nfile++)
				 		 errTable[k][col++]=errorTable[nfile][i][j][p][k];
					 }
			 }


			 int nTableParts=1+(int)(table[0].length/150);
			 
			 
			 String[][][] subTitle=new  String[nTableParts][][];
			 String[][][] subTable=new  String[nTableParts][][];
			 double[][][] subErr=new  double[nTableParts][][];
			 String[][][] subErrTitle=new  String[nTableParts][][];
			 
			 
			for(int k=0;k<nTableParts;k++){
				
				subTitle[k]=new String[titles.length][titles[0].length/nTableParts];
				for(int j=0;j<subTitle[k].length;j++)
					for(int p=0;p<subTitle[k][0].length;p++)
						subTitle[k][j][p]=titles[j][p+k*subTitle[k][0].length];
				
				subTable[k]=new String[table.length][table[0].length/nTableParts];
				for(int j=0;j<subTable[k].length;j++)
					for(int p=0;p<subTable[k][0].length;p++)
						subTable[k][j][p]=table[j][p+k*subTable[k][0].length];
				
				subErr[k]=new double[errTable.length][errTable[0].length/nTableParts];
				for(int j=0;j<subErr[k].length;j++)
					for(int p=0;p<subErr[k][0].length;p++)
						subErr[k][j][p]=errTable[j][p+k*subErr[k][0].length];
					
				
				subErrTitle[k]=new String[errTitles.length][errTitles[0].length/nTableParts];
				for(int j=0;j<subErrTitle[k].length;j++)
					for(int p=0;p<subErrTitle[k][0].length;p++)
						subErrTitle[k][j][p]=errTitles[j][p+k*subErrTitle[k][0].length];
			
				if(nTableParts==1)
					sheet[isheet] = workbook.createSheet(outputTitles[index],isheet+1);
				else
					sheet[isheet] = workbook.createSheet(outputTitles[index]+"-"+(k+1),isheet+1);
				
			
			fillSheet(sheet[isheet++],stepNumbs[fRef],time[fRef], subTitle[k],subTable[k],subErr[k],subErrTitle[k]) ;
				 
			//fillSheet(sheet[isheet++],stepNumbs[fRef],time[fRef], titles,table,errTable,errTitles) ;
			}
			

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
	
	private String[] splitToStrings(String line, String regex){

		String[] sp=line.split(regex);
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
			FileReader fr=new FileReader("path");
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

/*
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

					wantedOutputId=getCSInt(lines[i+1]);
					nWantedOutputGroups=wantedOutputId.length;
					ix++;
					break;
				}
			}


			wantedOutputItemId=new String[nWantedOutputGroups][];
			boolean ended=false;
			for(int i=0;i<numbLines;i++){
				if(lines[i]==null) break;
				if(lines[i].equalsIgnoreCase("IDs")) {
					for(int j=0;j<nWantedOutputGroups;j++){
						String line2=lines[i+1+j];
						if(line2==null || line2.equals("")){
							break;
						}
						String[] item=splitToStrings(line2);

						util.pr("-"+item[0]+"-");

						int idi=Integer.parseInt(item[0])-1;
						wantedOutputItemId[idi]=new String[wantedOutputItemId.length-1];

						for(int k=0;k<wantedOutputItemId[idi].length;k++)
						{
							wantedOutputItemId[idi][j]=item[j];
						}


					}
					if(ended) break;
				}
			 }


			///=== all

			/*			outputId=new int[1];
			outputItemId=new String[1][];

			for(int j=0;j<1;j++){
				outputId[j]=j+1;

			}

			outputId[0]=1;
			outputItemId[0]=new String[1];
			outputItemId[0][0]="1";
		nOutputGroups=outputId.length;
			 */


			//=========





			br.close();
		}

		catch(IOException e){System.err.println("Failed in reading path.");
		}
	}

	private void readOuputStrings(){
		isRelease=new boolean[nFiles];
		nRefine=new int[nFiles];

		iterationNumber=new int[nFiles][2];
		computationTimeAndDate=new String[nFiles][11];

		nAvialableOutputGroups=new int[nFiles];
		avialableOutputGroupIndex=new int[nFiles][nKeys];

		nodeEl=new int[nFiles][2];

		for(int nfile=0;nfile<nFiles;nfile++){

			isRelease[nfile]=dex.isRelease(file[nfile]);
			iterationNumber[nfile]=dex.getIterNumb(file[nfile]);
			computationTimeAndDate[nfile]=dex.getComputationTimesAndDate(file[nfile]);

			nodeEl[nfile]=dex.getNodeElNumbs(file[nfile]);

			if(nfile>0)
				nRefine[nfile]=dex.getRefine(file[nfile]);

			for(int i=0;i<nKeys;i++){
				String[][] data=dex.loadDataString(file[nfile],key[i],nLineAfter[i],nVariableIDsMax,nTmax,stderr);

				if(data==null) continue;

				avialableOutputGroupIndex[nfile][nAvialableOutputGroups[nfile]]=i;

				nAvialableOutputGroups[nfile]++;

			}
		}
	
			nOutputGroups=nAvialableOutputGroups[0];
			outputId=new int[nOutputGroups];

			for(int i=0;i<nAvialableOutputGroups[0];i++){
				outputId[i]=avialableOutputGroupIndex[0][i];

			}


			outputItemId=new String[nOutputGroups][];
			outputItemEntity=new String[nOutputGroups][];

		outputItemIndex=new int[nOutputGroups][];


		nT=new int[nFiles];

		stepNumbs=new int[nFiles][nTmax];
		time=	new Vect[nFiles];


		outputString=new String[nFiles][nOutputGroups][][][];

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


			for(int i=0;i<nOutputGroups;i++){

	
				int index=avialableOutputGroupIndex[nfile][i];
						

				String[][] data=dex.loadDataString(file[nfile],key[index],nLineAfter[index],nVariableIDsMax,nTmax,stderr);
				
				if(data==null) continue;
			

		/*		if(i==0){
					for(int k=0;k<1;k++)
					util.pr(data[k][0]);
				}
				*/

					outputItemId[i]=new String[data.length];

			
				outputItemIndex[i]=new int[data.length];

				outputString[nfile][i]=new String[outputItemId[i].length][][];

				String[] dataSplitted=new String[1];
				for(int j=0;j<data.length;j++){
					dataSplitted=this.splitToStrings(data[j][0],"  +");
					
					outputString[nfile][i][j]=new String[dataSplitted.length][nT[nfile]];

				
				
					outputItemId[i][j]=dataSplitted[0];
					

					dataSplitted=this.splitToStrings(data[0][0],"  +");
					
					outputItemEntity[i]=new String[dataSplitted.length-1];
					for(int k=0;k<outputItemEntity[i].length;k++){
						outputItemEntity[i][k]=dataSplitted[k+1];
					}

						outputItemIndex[i][j]=j;



				}
				
		

				
				for(int j=0;j<outputItemId[i].length;j++){
			

					for(int k=0;k<nT[nfile];k++){

						dataSplitted=this.splitToStrings(data[outputItemIndex[i][j]][k],"  +");
			
						for(int p=0;p<dataSplitted.length;p++){
							outputString[nfile][i][j][p][k]=dataSplitted[p];
		
							
						}
						
						

					}


				}


			}
		}


	}
	




	private void getErrorTable(){

		int fRef=0;

		errorTable=new double[nFiles][nOutputGroups][][][];
		for(int nfile=0;nfile<nFiles;nfile++){

			for(int i=0;i<nOutputGroups;i++){
				errorTable[nfile][i]=new double[outputString[nfile][i].length][][];
				for(int j=0;j<outputString[nfile][i].length;j++){
					errorTable[nfile][i][j]=new double[outputString[nfile][i][j].length][nT[fRef]];
				}
			}
		}


		int fRef1=0,fRef2=1;

		//double[] errorSum=new double[nFiles];
		
		errorMaxOfGroups=new double[nFiles][nOutputGroups];

	errorMax=new double[nFiles];
		 errorMaxCoord=new int[nFiles][3];

		 errorMaxID=new String[nFiles][2];
		 
		 errorMaxData=new String[nFiles][2];
		 
		formatErr=new boolean[nFiles];

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
									else if(Math.abs(data-data0)<1e-10){

										
											err=0;
										
									}

								
									//	err=Math.floor(err*1e6)/1e6;

									errorTable[nfile][i][j][p][k]=err;

									if(err>errorMax[nfile]){
										errorMax[nfile]=err;
										errorMaxCoord[nfile][0]=i;
										errorMaxCoord[nfile][2]=stepNumbs[fRef][k];;

										
										errorMaxID[nfile][0]=outputItemId[i][j];
										
										errorMaxID[nfile][1]=outputString[fRef][i][0][1][0];  
										

										errorMaxData[nfile][0]=outputString[fRef][i][j][p][k]+" <> "+outputString[nfile][i][j][p][k];



									}
									
									if(err>errorMaxOfGroups[nfile][i]){
										errorMaxOfGroups[nfile][i]=err;

										
									}

								//	errorSum[nfile]+=err;

								//	errorSum[nfile]=Math.floor(errorSum[nfile]*10000)/10000;

								}

							}

						}

				}


		}



	}

		
		public void fillSheet(WritableSheet sheet,int[] stepNumbs,Vect time, String[][] titles,String[][] table,double[][] error,String[][] errTitles) 
				throws RowsExceededException, WriteException, IOException{
		
		int clm=0;
		
		int rw=1;

		
		
		Number number=null;
		Label label;
	
		
		label=new Label(clm,rw,"step");
		sheet.addCell(label);
		
		label=new Label(clm+1,rw,"time");
		sheet.addCell(label);
		

		int nRow=table.length;
		
		int nCol=0;
		for(int i=0;i<table[0].length;i++){
			if(table[0][i]!=null && !table[0][i].equals("null"))
				nCol++;

		}
	

		for(int q=0;q<titles[0].length;q++){
			label=new Label(clm+2+q,rw,"ID-"+titles[0][q]);
			sheet.addCell(label);

		}
		
		rw++;
		
		label=new Label(clm,rw," ");
		sheet.addCell(label);
		
		label=new Label(clm+1,rw,"sec.");
		sheet.addCell(label);
		
	
		for(int q=0;q<titles[0].length;q++){
			label=new Label(q+clm+2,rw,titles[1][q]);
			sheet.addCell(label);
			}
		
		rw++;
		
		for(int q=0;q<titles[0].length;q++){
			label=new Label(q+clm+2,rw,titles[2][q]);
			sheet.addCell(label);
			}


		
		rw++;
		
		for(int p=0;p<nRow;p++){
			number= new Number(clm,p+rw,stepNumbs[p]);
			sheet.addCell(number);
			number= new Number(clm+1,p+rw,time.el[p]);
			sheet.addCell(number);
	
			
		}
		
		clm+=2;


	

		
		for(int p=0;p<nRow;p++){
	
			for(int q=0;q<nCol;q++){

				
				try{

					number= new Number(q+clm,p+rw,Double.parseDouble(table[p][q]));
					sheet.addCell(number);

				}
				catch(NumberFormatException e){
					label=new Label(clm+q,p+rw,table[p][q]);
					sheet.addCell(label);
				}
				
			}

		}
		
		clm+=nCol;
		
	
		
		rw=1;

		for(int q=0;q<errTitles[0].length;q++){
			label=new Label(clm+q,rw,"% err:ID-"+errTitles[0][q]);
			sheet.addCell(label);

		}
		
		rw++;
		
		
		for(int q=0;q<errTitles[0].length;q++){
			label=new Label(q+clm,rw,errTitles[1][q]);
			sheet.addCell(label);
			}
		
		rw++;
		
		for(int q=0;q<errTitles[0].length;q++){
			label=new Label(q+clm,rw,errTitles[2][q]);
			sheet.addCell(label);
			}
		
		rw++;
		
		for(int p=0;p<error.length;p++){
			
			for(int q=0;q<error[0].length;q++){

				if(error[p][q]==-1){

						label= new Label(q+clm,rw,"NA");
						sheet.addCell(label);
			
				}
				else{
				number= new Number(q+clm,rw,error[p][q]);
					sheet.addCell(number);
				}

				
			}
			
			rw++;

		}
		
	
		rw++;

		
		double[] errMax=new double[error[0].length];
		int[] errStep=new int[error[0].length];
	
			for(int q=0;q<error[0].length;q++){

				for(int p=0;p<error.length;p++){
					if(error[p][q]==-1){
						errMax[q]=-1;
						errStep[q]=q;
					}
					else{
				if(error[p][q]>errMax[q]) {
					errMax[q]=error[p][q];
					errStep[q]=q;
				}
					}
				
				
			}

		}
		
		



		for(int q=0;q<error[0].length;q++){

			label=new Label(clm+q,rw,"Max(%):");
			sheet.addCell(label);
			
			if(errMax[q]==-1){
				label= new Label(q+clm,rw+1,"NA");
				sheet.addCell(label);
				
			}
			else{
			number= new Number(q+clm,rw+1,errMax[q]);
			sheet.addCell(number);
			}

			
		}



	}

	
	private void setWriteable(String path){
		
		 final File file=new File(path);
		    try {
				Runtime.getRuntime().exec("cmd /c taskkill /f /im excel.exe");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    new Thread(new Runnable() {

		        @Override
		        public void run() {

		            try {
		                 // you need to wait 1-2 sec to close file before delete
		                   file.canWrite();  

		               } catch (Exception e) {

		                e.printStackTrace();
		            } 
		        }
		    }).start();
		    
		    
		   
		    
		    try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}


}
