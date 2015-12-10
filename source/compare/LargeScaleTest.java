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



		for(int nfile=1;nfile<-nFiles;nfile++){

			for(int i=0;i<outputId.length;i++){

				for(int j=1;j<outputItemId[i].length;j++){

					for(int k=0;k<nT[nfile]/20;k++){
						System.out.print(stepNumbs[nfile][k]+" >> \t" );
						for(int p=0;p<outputString[nfile][i][j].length;p++)
								System.out.print(outputString[nfile][i][j][p][k]+"\t");
							//System.out.print(outputString[nfile][i][j][p][k]+"\t"+outputString[0][i][j][p][k]+"\t"+errorTable[nfile][i][j][p][k]+"\t");

						System.out.println();


					}


					System.out.println("-------------------");


				}
				System.out.println("=======================");
			}
		}

	}



	private void writeOutputs() throws  WriteException{

		String path="camparison"+".xls";
		
		setWriteable(path);


		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(new File(path));
			WritableSheet[] sheet=new WritableSheet[nOutputGroups];


			int index=0;
			int nfile=0;
			
			for(int i=0;i<nOutputGroups;i++){
				index=avialableOutputGroupIndex[0][i];
			 sheet[i] = workbook.createSheet(outputTitles[index],i);
			 
			
			 int L=0;
			 for(int j=1;j<outputItemId[i].length;j++){
				 L+=(outputString[nfile][i][j].length-1);
			 }
			 
			 String[][] table=new String[nT[0]][L];
			 String[][] titles=new String[2][L];

			
			 int col;
	
				 col=0;
				 for(int j=1;j<outputItemId[i].length;j++)
					 for(int p=1;p<outputString[nfile][i][0].length;p++){
					 titles[0][col++]=outputItemId[i][j];
					 }
				 
				 col=0;
				 for(int j=1;j<outputItemId[i].length;j++)
					 for(int p=1;p<outputString[nfile][i][0].length;p++){
					 titles[1][col++]=outputString[nfile][i][0][p][0];
					 }
		
		

			 for(int k=0;k<table.length;k++)
			 {
				 col=0;
				 for(int j=1;j<outputString[nfile][i].length;j++)
					 for(int p=1;p<outputString[nfile][i][j].length;p++){
				 		 table[k][col++]=outputString[nfile][i][j][p][k];
					 }
			 }

			 
			 for(int k=0;k<table.length;k++)
			 {
			 for(int j=0;j<table[0].length;j++)
					 util.hshow(table[k]);
			 }
			 util.pr("--------------");
			 
	/*		 for(int k=0;k<titles.length;k++)
			 {
			 for(int j=0;j<titles[0].length;j++)
					 util.hshow(titles[j]);
			 }*/
			

	/*		 for(int k=0;k<titles.length;k++)
			 {
				 col=0;
				 for(int j=1;j<outputString[nfile][i].length;j++)
					 for(int p=1;p<outputString[nfile][i][j].length;p++){
						 titles[k][col++]=outputString[nfile][i][j][p][k];
					 }
			 }*/

			//this.fillSheet(sheet[i],i,String[]);
			fillSheet(sheet[i],stepNumbs[nfile],time[nfile], titles,table,new double[nT[nfile]][L]) ;
			

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


			if(nfile>0)
				nRefine[nfile]=dex.getRefine(file[nfile]);

			for(int i=0;i<nKeys;i++){
				String[][] data=dex.loadDataString(file[nfile],key[i],nVariableIDsMax,nTmax,stderr);

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


				String[][] data=dex.loadDataString(file[nfile],key[avialableOutputGroupIndex[nfile][i]],nVariableIDsMax,nTmax,stderr);
				
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

/*	public void fillSheet(WritableSheet sheet,int[] stetNumb,Vect time, String[] title1, String[] title2,String[][] table,double[][] table2) 
			throws RowsExceededException, WriteException, IOException{*/
		
		public void fillSheet(WritableSheet sheet,int[] stepNumbs,Vect time, String[][] titles,String[][] table,double[][] error) 
				throws RowsExceededException, WriteException, IOException{
/*			public void fillSheet(WritableSheet sheet,int is) 
					throws RowsExceededException, WriteException, IOException{*/
				
		


	

		
		int clm=0;
		
		int rw=1;
		
		clm=0;
		
		Number number=null;

		Label label;
		
		label=new Label(clm,rw,"step");
		sheet.addCell(label);
		
		label=new Label(clm+1,rw,"time");
		sheet.addCell(label);
		

		int nRow=table.length;
		
		int nCol=0;
		for(int i=0;i<table[0].length;i++){
			if(table[i][0]!=null && !table[i][0].equals("null"))
				nCol++;
		}
	

		for(int q=0;q<titles[0].length;q++){
			label=new Label(clm+2+q*(nCol-1),rw,"mat."+titles[0][q]);
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
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}


}
