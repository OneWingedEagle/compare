package compare;


import java.io.BufferedReader;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.PrintWriter;


//import math.Vect;


import java.io.FileInputStream;

import java.io.OutputStream;


public class LargeScaleTest {

	String regex="[:; . ,\\t]+";
	String regex2="[:;, \\t]+";

	final int nTmax=10000;
	final int nOutputGroupsMax=100;
	final int nVariableIDsMax=100;
	int[] nAvialableOutputGroups;
	int[][] avialableOutputGroupIndex;
	
	int nOutputGroups;
	//int[][] outputIdIndex;

	//String[][] outItemIdsAll;
	int[] outputId;
	String[][] outputItemId;
	int[][] outputItemIndex;

	String[] file,tip,moduleVersion;

	String[] key;


	String[][][][][] outputString;
	//String[][][] outputIDString;
	int[][] stepNumbs;
	int[] nT;
	Vect[] time;
	DataExtractor dex=new DataExtractor();

	int nKeys;
	
	PrintWriter stderr;

	public static void main(String[] args) throws IOException{

		LargeScaleTest x=new LargeScaleTest();

		x.compareOutputs();

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


	private void compareOutputs(){

		
		

		int nFiles=0;

		try{	
			FileReader fr=new FileReader("path");
			BufferedReader br = new BufferedReader(fr);
			String line;

			line=br.readLine();
			while(line.startsWith("*")) {line=br.readLine();}

			nFiles=Integer.parseInt(line);

			file=new String[nFiles];
			tip=new String[nFiles];

			while(line!=null){

				for(int nfile=0;nfile<nFiles;nfile++){
					line=br.readLine();
					while(line.startsWith("*")) {line=br.readLine();}

					if(line!=null) 
						file[nfile]=line;

				}

				for(int i=0;i<nFiles;i++){
					line=br.readLine();
					while(line.startsWith("*")) {line=br.readLine();}

					if(line!=null)
						tip[i]=line;

				}

				line=br.readLine();
				
				if(line==null) break; 
					
				while(line.startsWith("*")) {line=br.readLine();}

				if(line.equalsIgnoreCase("version")) {

					moduleVersion=new String[nFiles];
					for(int i=0;i<nFiles;i++){
						line=br.readLine();
						while(line.startsWith("*")) {line=br.readLine();}

						moduleVersion[i]=line;
					}
				}

				while(line.startsWith("*")) {line=br.readLine();}
				//int[] outputIndex=null;
				if(line.equalsIgnoreCase("output")) {

						outputId=getCSInt(line);
						nOutputGroups=outputId.length;

				}
				if(outputId!=null){
					outputItemId=new String[nOutputGroups][];
					for(int j=0;j<outputId.length;j++){
				line=br.readLine();
				if(line==null) break;
				while(line.startsWith("*")) {line=br.readLine();}
				outputItemId[j]=splitToStrings(line);
					}
				
				}

				
			}	

		br.close();
	}

	catch(IOException e){System.err.println("Failed in reading path.");
	}
		
			
		
		nAvialableOutputGroups=new int[nFiles];
		avialableOutputGroupIndex=new int[nFiles][nKeys];

		for(int nfile=0;nfile<nFiles;nfile++)
		for(int i=0;i<nKeys;i++){
			String[][] data=dex.loadDataString(file[nfile],key[i],nVariableIDsMax,nTmax,stderr);
	
			if(data==null) continue;
			
			avialableOutputGroupIndex[nfile][nAvialableOutputGroups[nfile]]=i;
			
			nAvialableOutputGroups[nfile]++;

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
		outputItemIndex=new int[nOutputGroups][];
	}
	
	
	

	
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

				String[][] data=dex.loadDataString(file[nfile],key[outputId[i]],nVariableIDsMax,nTmax,stderr);
				
				if(outputItemId[i]==null){
					outputItemId[i]=new String[data.length];
					outputItemIndex[i]=new int[data.length];
				}
				
				
				outputString[nfile][i]=new String[outputItemId[i].length][][];
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
		

		
		for(int nfile=0;nfile<nFiles;nfile++){

			for(int i=0;i<outputId.length;i++){

				for(int j=1;j<outputItemId[i].length;j++){

					for(int k=0;k<nT[nfile];k++){
						System.out.print(stepNumbs[nfile][k]+" >> \t" );
						for(int p=0;p<outputString[nfile][i][j].length;p++)
						System.out.print(/*outputItemId[i][j]+"    -    "+	*/outputString[nfile][i][j][p][k]+"\t");
						
						System.out.println();
						

				}
				
				
				System.out.println("-------------------");
				
				
				}
				System.out.println("=======================");
			}
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


}
