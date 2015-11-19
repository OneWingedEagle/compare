package compare;


import java.io.BufferedReader;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


//import math.Vect;


import java.io.FileInputStream;
import java.io.OutputStream;


public class LargeScaleTestOlder {

	String regex="[:; ,\\t]+";


	public static void main(String[] args) throws IOException{

		LargeScaleTestOlder x=new LargeScaleTestOlder();
		
	//	x.compareOutputs();
	x.compareOutputsAndWrite();

	}


	private void compareOutputsAndWrite(){


		int nFiles=0;
		String[] ff=null;
		String[] tip=null;
		String[]  moduleVersion=null;
	try{	
		FileReader fr=new FileReader("path");
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		
		line=br.readLine();
		nFiles=Integer.parseInt(line);
	 ff=new String[nFiles];
	tip=new String[nFiles];
		for(int i=0;i<nFiles;i++){
			line=br.readLine();
		if(line==null) {break;}
		ff[i]=line;

		}

		for(int i=0;i<nFiles;i++){
			line=br.readLine();
		if(line==null) {break;}
		tip[i]=line;

		}
		
		moduleVersion=new String[nFiles];
		for(int i=0;i<nFiles;i++){
			line=br.readLine();
		if(line==null) {break;}
		moduleVersion[i]=line;

		}
	
	}

	catch(IOException e){System.err.println("Failed in reading path.");
	}
	
	
		
		String[] file=new String[nFiles];
		
		int[][] stepNumb=new int[nFiles][10000];
		Vect[] time=	new Vect[nFiles];
		
		String[][][] sourceVoltage=new String[nFiles][][];
		String[][][] sourceCurrent=new String[nFiles][][];
		String[][][] fieldSourceFlux=new String[nFiles][][];
		String[][][] fieldSourceVoltage=new String[nFiles][][];
		String[][][] fieldSourceCurrent=new String[nFiles][][];
		String[][][] flux=new String[nFiles][][];
		String[][][][] energy=new String[nFiles][][][];
		String[][][] heat=new String[nFiles][][];
		
		String[][][] networkElementFlux=new String[nFiles][][];
		String[][][] networkElementVoltage=new String[nFiles][][];
		String[][][] networkElementCurrent=new String[nFiles][][];
		String[][] networkElementID=new String[nFiles][];
		int[] nNetworkElements =new int[nFiles];
		int[] nNetworkElementVoltages =new int[nFiles];
		int[] nNetworkElementFluxes =new int[nFiles];
		
		String[][] sourceCurrentID=new String[nFiles][];
		String[][] sourceVoltageID=new String[nFiles][];
		String[][] fieldSourceCurrentID=new String[nFiles][];
		String[][] fieldSourceVoltageID=new String[nFiles][];
		String[][] fieldSourceFluxID=new String[nFiles][];
		String[][] fluxID=new String[nFiles][];
		String[][] energyID=new String[nFiles][];
		String[][] heatID=new String[nFiles][];
	
		
		String[][][][] table=new String[nFiles][][][];//[nTot][nT][2+(2*nPowerSources+2*nFieldSources+nFluxes+nEnergies+nHeats)];
		String[][][] titles1=new String[nFiles][][];
		String[][][] titles2=new String[nFiles][][];
		int[] nPowerSourceCurrents =new int[nFiles];
		int[] nPowerSourceVoltages =new int[nFiles];
		int[] nFieldSourcesCurrent =new int[nFiles];
		int[] nFieldSourcesVoltage =new int[nFiles];
		int[] nFieldSourcesFlux =new int[nFiles];
		int[] nFluxes =new int[nFiles];
		int[] nEnergies =new int[nFiles];
		int[] nHeats =new int[nFiles];
		int[][] nn=new int[nFiles][2];
		int[]  nRefine=new int[nFiles];

		int[][] nodeEl=new int[nFiles][2];
		

		String[] group=null;
		
		
		FileWriter frsum;
		
		PrintWriter[] compWriter=new  	PrintWriter[nFiles];

		PrintWriter sumWriter=null;
		

		
		try{
			 frsum=new FileWriter("comparison");
			  sumWriter = new PrintWriter(frsum);			
		
		
		for(int nfile=0;nfile<nFiles;nfile++){
			file[nfile]=ff[nfile];
			
		//	util.pr(file[nfile]);
			
			try{
				FileWriter fr=new FileWriter("dataOut"+nfile);
				 compWriter[nfile] = new PrintWriter(fr);	
				 
				 if(nfile>0)
				 nRefine[nfile]=this.getRefine(file[nfile]);

	//	}
		
	/*	file[0]=folder+"/release/3sources_no_phi/output";
		file[1]=folder+"/MPI para 1/3sources_no_phi/output";
		file[2]=folder+"/MPI para 8/3sources_no_phi/output";
		nFiles=3;*/
		/*for(int i=0;i<nFiles;i++)
		file[i]=folder+tip[i];*/


			
	//util.pr(file[0]);
				 
	

		time[nfile]=	loadTimesSteps(stepNumb[nfile], file[nfile]);

		if(time[nfile]==null)
		{
			compWriter[nfile].println();
			compWriter[nfile].println("Failed in reading times in file "+file[nfile]);
		compWriter[nfile].println("Please check the file.");
		
		sumWriter.println();
		sumWriter.println("Failed in reading times in file "+file[nfile]);
		sumWriter.println("Please check the file.");
		frsum.close();
		sumWriter.close();
		
		fr.close();
		compWriter[nfile].close();
		
		System.exit(1);
		
		}
		
			int nT=time[nfile].length;
			

			
			
			int nMax=100;
			
			
			String[] dummy=new String[1];
			
			///==========network elements
			
			boolean network=false;
			if(network){

			for(int i=0;i<nMax;i++){
				String[] v=loadNetworkCurrentString(file[nfile],i+1,dummy);
			
				if(v==null) break;
				nNetworkElements[nfile]++;
			}
			
			networkElementID[nfile]=new String[nNetworkElements[nfile]];
			
			networkElementFlux[nfile]=new String[nNetworkElements[nfile]][nT];
			networkElementVoltage[nfile]=new String[nNetworkElements[nfile]][nT];
			networkElementCurrent[nfile]=new String[nNetworkElements[nfile]][nT];

				for(int i=0;i<nNetworkElements[nfile];i++){
				
		
					networkElementCurrent[nfile][i]=loadNetworkCurrentString(file[nfile],i+1,dummy);
				
					networkElementID[nfile][i]=dummy[0];
					
					networkElementVoltage[nfile][i]=loadNetworkVoltageString(file[nfile],i+1,dummy);
					
					networkElementFlux[nfile][i]=loadNetworkElementFluxString(file[nfile],i+1,dummy);
				

			}
			
			
				for(int i=0;i<nNetworkElements[nfile];i++){
					if(networkElementVoltage[nfile][i]!=null) nNetworkElementVoltages[nfile]++;
					
					if(networkElementFlux[nfile][i]!=null) nNetworkElementFluxes[nfile]++;
					
			}
				
	
	
			}
			//======
			

			
			if(nNetworkElements[nfile]==0){
			
			for(int i=0;i<nMax;i++){
				String[] v=loadSourceCurrentString(file[nfile],i+1,dummy);
			
				if(v==null) break;
				nPowerSourceCurrents[nfile]++;
			}
			
			
			sourceCurrentID[nfile]=new String[nPowerSourceCurrents[nfile]];
			
			sourceCurrent[nfile]=new String[nPowerSourceCurrents[nfile]][nT];

				for(int i=0;i<nPowerSourceCurrents[nfile];i++){
		
					sourceCurrent[nfile][i]=loadSourceCurrentString(file[nfile],i+1,dummy);
				
					sourceCurrentID[nfile][i]=dummy[0];
				

			}
				
		
				for(int i=0;i<nPowerSourceCurrents[nfile];i++){
					String[] v=loadSourceVoltageString(file[nfile],i+1,dummy);
				
					if(v==null) break;
					nPowerSourceVoltages[nfile]++;
				}
				
				sourceVoltageID[nfile]=new String[nPowerSourceVoltages[nfile]];
				
				sourceVoltage[nfile]=new String[nPowerSourceVoltages[nfile]][nT];
				
				for(int i=0;i<nPowerSourceVoltages[nfile];i++){
					sourceVoltage[nfile][i]=loadSourceVoltageString(file[nfile],i+1,dummy);
					
					sourceVoltageID[nfile][i]=dummy[0];

			}
	
			
		//--------------------------------------------
			
		
			
			for(int i=0;i<nMax;i++){
				String[] v=loadFieldSourceCurrentString(file[nfile],i+1,dummy);
				if(v==null) break;
				nFieldSourcesCurrent[nfile]++;
			
			}
			
			fieldSourceCurrentID[nfile]=new String[nFieldSourcesCurrent[nfile]];
			fieldSourceCurrent[nfile]=new String[nFieldSourcesCurrent[nfile]][nT];
			
				for(int i=0;i<nFieldSourcesCurrent[nfile];i++){
				
					fieldSourceCurrent[nfile][i]=loadFieldSourceCurrentString(file[nfile],i+1,dummy);
					fieldSourceCurrentID[nfile][i]=dummy[0];
		
				}
				
	
				
			//--------------------------------------------	
			
		//--------------------------------------------
			
		
				int[] hasVoltageIndex=new int[nFieldSourcesCurrent[nfile]];
		
				int ix=0;
			for(int i=0;i<nFieldSourcesCurrent[nfile];i++){
				String[] v=loadFieldSourceVoltageString(file[nfile],i+1,dummy);
			if(v==null) continue;
			hasVoltageIndex[ix++]=i;
				nFieldSourcesVoltage[nfile]++;
			
			}
			
			fieldSourceVoltageID[nfile]=new String[nFieldSourcesVoltage[nfile]];
			
			fieldSourceVoltage[nfile]=new String[nFieldSourcesVoltage[nfile]][nT];
			
	
			for(int i=0;i<nFieldSourcesVoltage[nfile];i++){
				

				int index=hasVoltageIndex[i];
					fieldSourceVoltage[nfile][i]=loadFieldSourceVoltageString(file[nfile],index+1,dummy);
					
					fieldSourceVoltageID[nfile][i]=dummy[0];
				}
				
			
			ix=0;
			int[] hasFluxIndex=new int[nFieldSourcesCurrent[nfile]];
			for(int i=0;i<nFieldSourcesCurrent[nfile];i++){
					
					String[] v=loadCurFluxString(file[nfile],i+1,dummy);
					if(v==null) continue;
					hasFluxIndex[ix++]=i;
					nFieldSourcesFlux[nfile]++;
	
			}

				
				fieldSourceFlux[nfile]=new String[nFieldSourcesFlux[nfile]][nT];
				
				fieldSourceFluxID[nfile]=new String[nFieldSourcesFlux[nfile]];


				
				
				for(int i=0;i<nFieldSourcesFlux[nfile];i++){
					int index=hasVoltageIndex[i];

					fieldSourceFlux[nfile][i]=loadCurFluxString(file[nfile],index+1,dummy);
					
					fieldSourceFluxID[nfile][i]=dummy[0];
				}
				
				
			}
			
		//	System.out.println(nFieldSourcesVoltage[nfile]);
				
			//--------------------------------------------	
			
				
				for(int i=0;i<nMax;i++){
					String[] v=loadMagFluxString(file[nfile],i+1,dummy);
					if(v==null) break;
						nFluxes[nfile]++;
				//	flux[0][i]=v.deepCopy();
				}
			
				flux[nfile]=new String[nFluxes[nfile]][nT];
				fluxID[nfile]=new String[nFluxes[nfile]];
				
					for(int i=0;i<nFluxes[nfile];i++){
						flux[nfile][i]=loadMagFluxString(file[nfile],i+1,dummy);
						fluxID[nfile][i]=dummy[0];
					}

	
			
		//--------------------------------------------	
				
				for(int i=0;i<nMax;i++){
					String[][] v=loadMagEnergyString(file[nfile],i+1,dummy);
					if(v==null) break;
					nEnergies[nfile]++;
				//	energy[0][i]=v.deepCopy();
				}
				
				energyID[nfile]=new String[nEnergies[nfile]];
				energy[nfile]=new String[nEnergies[nfile]][nT][3];
			
					for(int i=0;i<nEnergies[nfile];i++){

						energy[nfile][i]=loadMagEnergyString(file[nfile],i+1,dummy);
						energyID[nfile][i]=dummy[0];

				}
				
				
				
				for(int i=0;i<nMax;i++){
					String[] v=loadHeatString(file[nfile],i+1,dummy);
		
					if(v==null) break;
					nHeats[nfile]++;
					//heat[0][i]=v.deepCopy();
				}
				heatID[nfile]=new String[nHeats[nfile]];
		
				heat[nfile]=new String[nHeats[nfile]][nT];
					for(int i=0;i<nHeats[nfile];i++){

						heat[nfile][i]=loadHeatString(file[nfile],i+1,dummy);
						heatID[nfile][i]=dummy[0];
					}

				
					int nTot=0;
					if(sourceCurrent[nfile]!=null) nTot++;
					if(sourceVoltage[nfile]!=null) nTot++;
					if(fieldSourceCurrent[nfile]!=null) nTot++;
					if(fieldSourceVoltage[nfile]!=null) nTot++;
					if(fieldSourceFlux[nfile]!=null) nTot++;
					if(flux[nfile]!=null) nTot++;
					if(energy[nfile]!=null) nTot+=3;
					if(heat[nfile]!=null) nTot++;
					
					
					table[nfile]=new String[nTot][][];//;[nT][2+(2*nPowerSources[nfile]+2*nFieldSources[nfile]+nFluxes[nfile]+nEnergies[nfile]+nHeats[nfile])];		
					
				
			/*	if(nPowerSources[nfile]>0)
					nT=sourceCurrent[0][0].length;
				else if(nFluxes[nfile]>0) nT=flux[0][0].length;
				else if(nEnergies[nfile]>0) nT=energy[0][0].length;*/
					
	int iGroup=0;
	
	if(nNetworkElements[nfile]>0){
		table[nfile][iGroup]=new String[nT][2+(nNetworkElements[nfile])+(nNetworkElementVoltages[nfile])+nNetworkElementFluxes[nfile]];	
		for(int t=0;t<nT;t++){
			int ix=0;
			table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
			table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
						for(int i=0;i<nNetworkElements[nfile];i++){
							table[nfile][iGroup][t][ix++]=networkElementCurrent[nfile][i][t];	
					}
					
						for(int i=0;i<nNetworkElementVoltages[nfile];i++){
							table[nfile][iGroup][t][ix++]=networkElementVoltage[nfile][i][t];	
					}

							for(int i=0;i<nFieldSourcesFlux[nfile];i++){
								table[nfile][iGroup][t][ix++]=networkElementFlux[nfile][i][t];	
							
						}
		}
		
		
		
	}
	else{
		
	table[nfile][iGroup]=new String[nT][2+nPowerSourceCurrents[nfile]];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nPowerSourceCurrents[nfile];i++){
						table[nfile][iGroup][t][ix++]=sourceCurrent[nfile][i][t];	
					
				}
	
				
					
		
	}
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+nPowerSourceVoltages[nfile]];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);

					
					for(int i=0;i<nPowerSourceVoltages[nfile];i++){
						table[nfile][iGroup][t][ix++]=sourceVoltage[nfile][i][t];	
					
				}

	}

	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+(nFieldSourcesCurrent[nfile])];
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nFieldSourcesCurrent[nfile];i++){
						table[nfile][iGroup][t][ix++]=fieldSourceCurrent[nfile][i][t];	
				}
					
				
	}
	
	//=======
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+(nFieldSourcesVoltage[nfile])];
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
	
					
					for(int i=0;i<nFieldSourcesVoltage[nfile];i++){
						table[nfile][iGroup][t][ix++]=fieldSourceVoltage[nfile][i][t];	
				}
	

	}
	
	//====
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+(nFieldSourcesFlux[nfile])];
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
	
	

					if(nFieldSourcesFlux[nfile]>0)
						for(int i=0;i<nFieldSourcesFlux[nfile];i++){
							table[nfile][iGroup][t][ix++]=fieldSourceFlux[nfile][i][t];	
						
					}
	}
	
	}
	
	
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+(nFluxes[nfile])];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nFluxes[nfile];i++){
						table[nfile][iGroup][t][ix++]=flux[nfile][i][t];	
				}
	}
	
	//===========
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+nEnergies[nfile]];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nEnergies[nfile];i++){
						table[nfile][iGroup][t][ix++]=energy[nfile][i][t][0];	
				}

	}
	
	//==========
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+nEnergies[nfile]];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nEnergies[nfile];i++){
						table[nfile][iGroup][t][ix++]=energy[nfile][i][t][1];	
				}
				
	}
	
//================
	
	iGroup++;
	table[nfile][iGroup]=new String[nT][2+nEnergies[nfile]];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
	
					for(int i=0;i<nEnergies[nfile];i++){
						table[nfile][iGroup][t][ix++]=energy[nfile][i][t][2];	
				}
					
		
	}
	

	iGroup++;
	
	
	table[nfile][iGroup]=new String[nT][2+(nHeats[nfile])];	
	for(int t=0;t<nT;t++){
		int ix=0;
		table[nfile][iGroup][t][ix++]=Integer.toString(stepNumb[nfile][t]);
		table[nfile][iGroup][t][ix++]=Double.toString(time[nfile].el[t]);
					for(int i=0;i<nHeats[nfile];i++){
						table[nfile][iGroup][t][ix++]=heat[nfile][i][t];	
				}
	}

	iGroup++;


	 nTot=iGroup;

	// System.out.println(nTot);
			
		titles1[nfile]=new String[nTot][];
		titles2[nfile]=new String[nTot][];
			

	
			//===================
			
			
			 compWriter[nfile].format("%20s Result locations:\t",file[nfile]);
			

			 compWriter[nfile].println();
		

		
			String[][] tt=new String[nFiles][11];
			 tt[nfile]=getComputationTimesAndDate( file[nfile]);
		
			
			 compWriter[nfile].print("Analysis date:\t");

			 // compWriter[nfile].print(tip[nfile]+"\t"+Double.parseDouble(tt[nfile][3]));
				//compWriter[nfile].println(tt[nfile][1]);
				//String[] sp=tt[nfile][9].split(regex);

				 compWriter[nfile].println(tt[nfile][9]);
	
			 compWriter[nfile].println();
		
				nodeEl[nfile]=this.getNodeElNumbs(file[nfile]);
				if(nfile==1){
				if(nodeEl[nfile][1]==nodeEl[0][1]) nRefine[0]=nRefine[nfile];
				}
				
				 // == module version
				 
		

				 compWriter[nfile].format("%15s %15s\n","Module version: ",moduleVersion[nfile]);
				 compWriter[nfile].println();
			
			 compWriter[nfile].println();
			
			 //==============
				
				
				 // == refine
				 
				 if(nfile>0){
			
			compWriter[nfile].format("%15s %15s\n","Module version: ",nRefine[nfile]);

			compWriter[nfile].println();
			
			 compWriter[nfile].println();
				 }
			 //==============
				
				 
		
			 compWriter[nfile].println("Node and element numbers:");

					// compWriter[nfile].println(tip[nfile]+":");
					 compWriter[nfile].println("Total number of nodes: "+nodeEl[nfile][0]);
					 compWriter[nfile].println("Total number of elements: "+nodeEl[nfile][1]);
	
				 compWriter[nfile].println();
			
			 compWriter[nfile].println();
			 

		
			 nn[nfile]=this.getIterNumb(file[nfile]);
	
			compWriter[nfile].println("Number of iterations:");

				 compWriter[nfile].println("Total ICCG: "+nn[nfile][0]);
				 compWriter[nfile].println("Total Nonlinear: "+nn[nfile][1]);
		
			 compWriter[nfile].println();
			
			 compWriter[nfile].print("Total time:\t"); 

			 String[] sp=tt[nfile][1].split(regex);

				 compWriter[nfile].println(Double.parseDouble(sp[2]));
		
			 compWriter[nfile].println();
			
			//=================
			
			 compWriter[nfile].println("Comparison of results:");
			 compWriter[nfile].println();
			 


			 //int fRef=0;

			 int ix=0;
			 iGroup=0;
	
			
			if(nNetworkElements[nfile]>0){
				titles1[nfile][iGroup]=new String[100];///lllll
				
				titles1[nfile][iGroup][ix++]="Step.No";	
				titles1[nfile][iGroup][ix++]="Time(sec.)";	

				iGroup++;
				 ix=0;
				 titles1[nfile][iGroup][ix++]="Step.No";	
					titles1[nfile][iGroup][ix++]="Time(sec.)";	
		
				for (int i=0;i<nNetworkElements[nfile];i++){
				
					
					titles1[nfile][iGroup][ix++]="Elem. "+(networkElementID[nfile][i])+"";	
		
				}
				
				for (int i=0;i<nNetworkElementVoltages[nfile];i++){
					titles1[nfile][iGroup][ix++]="Elem. "+(networkElementID[nfile][i])+"";	
		
				}
	
				for (int i=0;i<nNetworkElementFluxes[nfile];i++){
					titles1[nfile][iGroup][ix++]="Elem. "+(networkElementID[nfile][i])+"";	
		
				}
				
				
			}
			else{
				
				ix=0;
				titles1[nfile][iGroup]=new String[2+nPowerSourceCurrents[nfile]];
				titles1[nfile][iGroup][ix++]="Step.No";	
				titles1[nfile][iGroup][ix++]="Time(sec.)";	
				
			for (int i=0;i<nPowerSourceCurrents[nfile];i++){

				titles1[nfile][iGroup][ix++]=""+(sourceCurrentID[nfile][i])+"";	
	
			}
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nPowerSourceVoltages[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
			
			for (int i=0;i<nPowerSourceVoltages[nfile];i++){
				
				titles1[nfile][iGroup][ix++]=""+(sourceVoltageID[nfile][i])+"";	
	
			}
			
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nFieldSourcesCurrent[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
	
			for (int i=0;i<nFieldSourcesCurrent[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(fieldSourceCurrentID[nfile][i])+"";	
	
			}
			
			iGroup++;
			
			ix=0;
			titles1[nfile][iGroup]=new String[2+nFieldSourcesVoltage[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
			
			for (int i=0;i<nFieldSourcesVoltage[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(fieldSourceVoltageID[nfile][i])+"";	
	
			}
			
			iGroup++;
			
			ix=0;
			titles1[nfile][iGroup]=new String[2+nFieldSourcesFlux[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
	
			for (int i=0;i<nFieldSourcesFlux[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(fieldSourceFluxID[nfile][i])+"";	
	
			}
			
			}
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nFluxes[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
	
			for (int i=0;i<nFluxes[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(fluxID[nfile][i])+"";	
	
			}
			
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nEnergies[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
	
	
			for (int i=0;i<nEnergies[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(energyID[nfile][i])+"";	
	
			}
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nEnergies[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
			for (int i=0;i<nEnergies[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(energyID[nfile][i])+"";	
	
			}
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nEnergies[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
			for (int i=0;i<nEnergies[nfile];i++){
				titles1[nfile][iGroup][ix++]=""+(energyID[nfile][i])+"";	
	
			}
			
			
			iGroup++;
			ix=0;
			titles1[nfile][iGroup]=new String[2+nEnergies[nfile]];
			titles1[nfile][iGroup][ix++]="Step.No";	
			titles1[nfile][iGroup][ix++]="Time(sec.)";	
	
			for (int i=0;i<nHeats[nfile];i++){
				titles1[nfile][iGroup][ix++]="Heat"+(heatID[nfile][i])+"";	
	
			}
			

			
	 group=new String[nTot];

	 		group[0]="   Power Sources Currents";
	 		group[1]="   Power Sources Volatges";
	 		group[2]="   Field Sources Currents";
			group[3]="   Field Sources Volatges";
			group[4]="   Field Sources Fluxes";
			group[5]="      Fluxes";
			group[6]=" Magnetic Energies";
			group[7]=" Magnetic Co-energy";
			group[8]=" Magnetic Ave.energy";
			group[9]="      Heat         ";
			
			
			String[] unit=new String[nTot];
			
			unit[0]=" (A)  ";
			unit[1]=" (V)  ";
			unit[2]=" (A)  ";
			unit[3]=" (V)  ";
			unit[4]=" (Wb) ";
			unit[5]=" (Wb) ";
			unit[6]=" (J)  ";
			unit[7]=" (J)  ";
			unit[8]=" (J)  ";
			unit[9]=" (J)  ";
			
			for(int i=0;i<nTot;i++){
				if(table[nfile][i][0].length<3) continue;
				compWriter[nfile].println();
				for(int p=0;p<table[nfile][i][0].length;p++)
					for(int h=0;h<5;h++)
				compWriter[nfile].print("*");
							compWriter[nfile].print(group[i]+unit[i]);
				for(int p=0;p<table[nfile][i][0].length;p++)
					for(int h=0;h<5;h++)
				compWriter[nfile].print("*");
				compWriter[nfile].println();
				compWriter[nfile].println();
			
				for(int j=0;j<table[nfile][i][0].length;j++){
					compWriter[nfile].format("%18s",titles1[nfile][i][j]);	
					}
				compWriter[nfile].println();
				for(int p=0;p<table[nfile][i][0].length;p++)
					for(int h=0;h<15;h++)
				compWriter[nfile].print("-");
				compWriter[nfile].println();
			for(int j=0;j<table[nfile][i].length;j++){
			
		
				for(int k=0;k<table[nfile][i][0].length;k++){

					compWriter[nfile].format("%18s",table[nfile][i][j][k]);
				}
				compWriter[nfile].println();
			}
			//compWriter[nfile].println();
			for(int p=0;p<table[nfile][i][0].length;p++)
				for(int h=0;h<15;h++)
			compWriter[nfile].print("-");
			compWriter[nfile].println();
			}
			
			
	//	tables.show();
		
			fr.close();	
	
	}
		catch(IOException e){  compWriter[nfile].println(e.getMessage());}
		
		
		compWriter[nfile].close();
	}
		
		boolean  machingFiles=true;
		for(int nfile=1;nfile<nFiles;nfile++){
			//for(int i=0;i<nTot;i++){
				if(time[nfile].length!=time[0].length){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of time steps.");
					machingFiles=false;
					}

				if(machingFiles && time[nfile].sub(time[0]).norm()>0){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in times steps.");
					machingFiles=false;
					}
				if(nPowerSourceCurrents[nfile]!=nPowerSourceCurrents[0]){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of power sources.");
					machingFiles=false;
					}
				
				if(nFieldSourcesCurrent[nfile]!=nFieldSourcesCurrent[0]){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of field sources.");
					machingFiles=false;
					}
				if(nFluxes[nfile]!=nFluxes[0]){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of magnetic fluxes.");
					machingFiles=false;
					}	
				
				if(nEnergies[nfile]!=nEnergies[0]){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of magnetic energies.");
					machingFiles=false;
					}
				
				if(nHeats[nfile]!=nHeats[0]){
					sumWriter.print("Error: ");
					sumWriter.println("File #"+nfile+" does not match with File #0 in number of Heats.");
					machingFiles=false;
					}
		
								
			//}
			
		}

		
		if(!machingFiles){
		frsum.close();
		sumWriter.close();
		System.exit(1);
		}
		
	
		

		
		
		 sumWriter.println("Result locations:");
			for(int nfile=0;nfile<nFiles;nfile++){
				 sumWriter.println(tip[nfile]+":\t"+file[nfile]);
			}
			 sumWriter.println();
		

		
			String[][] tt=new String[nFiles][11];
			for(int nfile=0;nfile<nFiles;nfile++){
			 tt[nfile]=getComputationTimesAndDate( file[nfile]);
			}
			
			 sumWriter.println("Analysis date:");
			for(int nfile=0;nfile<nFiles;nfile++){
				// sumWriter.print(tip[nfile]+"\t"+Double.parseDouble(tt[nfile][3]));
				//sumWriter.println(tt[nfile][1]);
				//String[] sp=tt[nfile][9].split(regex);

				 sumWriter.println(tip[nfile]+":\t"+tt[nfile][9]);
			}
			sumWriter.println();
			
			 // == module version
			 
			sumWriter.println("Module version: ");
			for(int nfile=0;nfile<nFiles;nfile++){
				sumWriter.format("%15s: %15s\n",tip[nfile],moduleVersion[nfile]);
			}
			sumWriter.println();
		

		
		 //==============
			
			// == refine
			sumWriter.println("Refine: ");
			for(int nfile=0;nfile<nFiles;nfile++){
				sumWriter.format("%15s: %15s\n",tip[nfile],nRefine[nfile]);
			}
			sumWriter.println();

		

		 //==============
			
			 sumWriter.println();
		
			for(int nfile=0;nfile<nFiles;nfile++){
				nodeEl[nfile]=this.getNodeElNumbs(file[nfile]);
			}
			 sumWriter.println("Node and element numbers:");
				for(int nfile=0;nfile<nFiles;nfile++){

					 sumWriter.println(tip[nfile]+":");
					 sumWriter.println("Total number of nodes: "+nodeEl[nfile][0]);
					 sumWriter.println("Total number of elements: "+nodeEl[nfile][1]);
				}
				 sumWriter.println();
			
			 sumWriter.println();
			
			for(int nfile=0;nfile<nFiles;nfile++){
			 nn[nfile]=this.getIterNumb(file[nfile]);
			}
			sumWriter.println("Number of iterations:");
			for(int nfile=0;nfile<nFiles;nfile++){

				 sumWriter.println(tip[nfile]+":");
				 sumWriter.println("Total ICCG: "+nn[nfile][0]);
				 sumWriter.println("Total Nonlinear: "+nn[nfile][1]);
			}
			 sumWriter.println();
			
			 sumWriter.println("Total time:"); 
			for(int nfile=0;nfile<nFiles;nfile++){
				// sumWriter.print(tip[nfile]+"\t"+Double.parseDouble(tt[nfile][3]));
				//sumWriter.println(tt[nfile][1]);
				String[] sp=tt[nfile][1].split(regex);

				 sumWriter.println(tip[nfile]+":\t"+Double.parseDouble(sp[2]));
			}
			 sumWriter.println();
			
			//=================
			 
	
			
			 sumWriter.println("Comparison of results:");
			 sumWriter.println();

				int fRef=0;
				
				int nT=time[fRef].length;
			 
				int nTot=group.length;
				

				double[][][][] errorTable=new double[nFiles][nTot][][];
				
				for(int nfile=1;nfile<nFiles;nfile++)
				for(int i=0;i<nTot;i++){
					if(table[fRef][i][0].length<3) continue;


					errorTable[nfile][i]=new double[nT][table[nfile][i][0].length];
				}
				
				double[] errorSum=new double[nFiles];
				
				double[] errorMax=new double[nFiles];
				int[][] errorMaxCoord=new int[nFiles][3];
				
				boolean[] formatErr=new boolean[nFiles];

				////---------------
				for(int i=0;i<nTot;i++){
					if(table[fRef][i][0].length<3) continue;


					
					for(int j=0;j<table[fRef][i].length;j++){

					
						
					double data=0,data0=0;
			

					for(int k=2;k<table[fRef][i][0].length;k++){
						
					for(int nfile=1;nfile<nFiles;nfile++){

						boolean numb0=true;
						
						int nr=k;

						 try{
							 if(nfile==1)
							 data0=Double.parseDouble(table[fRef][i][j][nr]);
							 else
							data0=Double.parseDouble(table[fRef+1][i][j][nr]);
							 
						}
						catch(NumberFormatException e){numb0=false;}
					
								boolean numb=true;
								try{

									 data=Double.parseDouble(table[nfile][i][j][k]);
								}
								catch(NumberFormatException e){numb=false;}
								
								if(!numb0 || !numb){
									errorTable[nfile][i][j][nr]=-1;
												
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
						
								
							
								err=Math.floor(err*10000)/10000;
								
								errorTable[nfile][i][j][nr]=err;
					
								if(err>errorMax[nfile]){
									errorMax[nfile]=err;
									errorMaxCoord[nfile][0]=i;
									errorMaxCoord[nfile][1]=stepNumb[fRef][j];
									errorMaxCoord[nfile][2]=k;
									
								}
								
								errorSum[nfile]+=err;
								
								errorSum[nfile]=Math.floor(errorSum[nfile]*10000)/10000;
								
								}

							}
					
					}

				}
			
				
				}
				
				
				 
				 sumWriter.println("Max errors:");
					for(int nfile=1;nfile<nFiles;nfile++){
						String title="";
						if(nfile==1) title=tip[nfile]+" against "+tip[0];
						else title=tip[nfile]+" against "+tip[1];
						if(!formatErr[nfile]){
				 sumWriter.format("%36s:%18s %1s",title,errorMax[nfile],"%");
				 sumWriter.format(" at: %18s%12s%3s%20s%15s\n",group[errorMaxCoord[nfile][0]]," ,Step No: ",errorMaxCoord[nfile][1]," ,variable ID: ",titles1[nfile][errorMaxCoord[nfile][0]][errorMaxCoord[nfile][2]]);
						}
						else
				 sumWriter.format("%18s:%18s\n","NA"," Warning: There are number format errors in results.");
					}
					 sumWriter.println();	
					 
					 sumWriter.println("Accumulated errors:");
						for(int nfile=1;nfile<nFiles;nfile++){
							
							String title="";
							if(nfile==1) title=tip[nfile]+" against "+tip[0];
							else title=tip[nfile]+" against "+tip[1];
							
							if(!formatErr[nfile])
					 sumWriter.format("%36s:%18s %1s\n",title,errorSum[nfile],"%");
							else
					 sumWriter.format("%18s:%18s\n","NA"," Warning: There are number format errors in results.");
						}
						 sumWriter.println();	
				

		
		for(int i=0;i<nTot;i++){
			if(table[fRef][i][0].length<3) continue;
			sumWriter.println();
			for(int p=0;p<table[fRef][i][0].length;p++)
				for(int h=0;h<1;h++)
			sumWriter.print("*");
						sumWriter.print(group[i]);
			for(int p=0;p<table[fRef][i][0].length;p++)
				for(int h=0;h<5*nFiles;h++)
			sumWriter.print("*");
			sumWriter.println();
			sumWriter.println();
			sumWriter.format("%18s",titles1[fRef][i][0]);
			sumWriter.format("%18s",titles1[fRef][i][1]);
			for(int j=2;j<table[fRef][i][0].length;j++){
				for(int nfile=0;nfile<nFiles;nfile++){
						sumWriter.format("%18s",titles1[nfile][i][j]);	
				}
	
				}
			
			for(int j=2;j<table[fRef][i][0].length;j++){

				for(int nfile=1;nfile<nFiles;nfile++){
					sumWriter.format("%18s","Err. of "+titles1[nfile][i][j]+" (%)");	
			}
				}
			
			sumWriter.println();
			sumWriter.format("%18s","");
			sumWriter.format("%18s","");	
			for(int j=2;j<table[fRef][i][0].length;j++){
				for(int nfile=0;nfile<nFiles;nfile++){
						sumWriter.format("%18s",tip[nfile]);	
				}

				}
			
			for(int j=2;j<table[fRef][i][0].length;j++){

				for(int nfile=1;nfile<nFiles;nfile++){
					if(nfile==1)
						sumWriter.format("%18s",tip[nfile]+"/"+tip[0]);	
					else
						sumWriter.format("%18s",tip[nfile]+"/"+tip[1]);	
			}
				}
			
			sumWriter.println();
			for(int p=0;p<table[fRef][i][0].length;p++)
				for(int h=0;h<15;h++)
			sumWriter.print("-");
			sumWriter.println();
			
			
			for(int j=0;j<table[fRef][i].length;j++){
				sumWriter.format("%18s",stepNumb[fRef][j]);
				sumWriter.format("%18s",time[fRef].el[j]);
				
		
			for(int k=2;k<table[fRef][i][0].length;k++){
				for(int nfile=0;nfile<nFiles;nfile++){
				sumWriter.format("%18s",table[nfile][i][j][k]);
				}
			}
	

			for(int k=2;k<table[fRef][i][0].length;k++){
			for(int nfile=1;nfile<nFiles;nfile++){
			
			double err=errorTable[nfile][i][j][k];
			if(err==-1){
				sumWriter.format("%18s","NA");
			}
			else{
						if(err<100)
							sumWriter.format("%18s",err);
						else
						sumWriter.format("%18s",">100");

			}			
				
					}
			
			}
			
			sumWriter.println();
			
		
			

		}
	
		
		}
			
		for(int h=0;h<50;h++)
		sumWriter.print("=");
		sumWriter.println();
		
		frsum.close();
		sumWriter.close();
			}
		
		catch(IOException e){  sumWriter.print(e.getMessage());}
		
		
		
}
	
		
	/*	try{
			PrintWriter compWriter = new PrintWriter(new BufferedWriter(new FileWriter("summary");	
			
		}*/

	
	public Vect loadSourceCurrent(String file, int sourceID){

	
		
		Vect powerSourceCurrent=new Vect(10000);

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Power Sources")){}
			if(line==null) {break;}

			line=br.readLine();
			for(int i=0;i<sourceID;i++){
			line=br.readLine();
			if(line.startsWith("*"))
			 break;
			}
			//util.pr(line);
		//	line=br.readLine();
			sp=line.split(regex);
			if(sp.length<=2) break;
			//util.pr(sp[2]);
			powerSourceCurrent.el[ix]=Double.parseDouble(sp[2]);

			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	Vect current=new Vect(ix);

	for(int i=0;i<ix;i++){
		current.el[i]=powerSourceCurrent.el[i];

	}

	//util.plot(current);
	//util.plot(NRerr);

	return current;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	
	
public String[] loadSourceCurrentString(String file, int sourceIndex,String[] id){

	
		
		String[] powerSourceCurrent=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Power Sources")){}
			if(line==null) {break;}

			line=br.readLine();
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("*"))
			 break;
			}
			//util.pr(line);
		//	line=br.readLine();
			sp=line.split(regex);
			if(sp.length<=2) break;
			//util.pr(sp[2]);
			id[0]=sp[1];
	
			powerSourceCurrent[ix]=sp[2];

			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] current=new String[ix];

	for(int i=0;i<ix;i++){
		current[i]=powerSourceCurrent[i];

	}

	//util.plot(current);
	//util.plot(NRerr);

	return current;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	
	

public String[] loadCurFluxString(String file, int sourceIndex,String[] id){

	String[] fieldSourceFlux1=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Sources ")){}
			if(line==null) {break;}
			line=br.readLine();
			sp=line.split(regex);
			if(sp.length<=4) break;
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}
			if(line.startsWith("**")) break;
			sp=line.split(regex);

			if(sp.length<=4) break;
			id[0]=sp[1];
			fieldSourceFlux1[ix]=sp[4];

			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] fieldSourceFlux=new String[ix];

	for(int i=0;i<ix;i++){
		fieldSourceFlux[i]=fieldSourceFlux1[i];

	}
	
	//util.plot(voltage);
	//util.plot(NRerr);

	return fieldSourceFlux;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	


	
public String[] loadFieldSourceCurrentString(String file,int sourceIndex,String[] id){

		
	String[] powerSourceCurrent=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Sources ")){}
			if(line==null) {break;}
			line=br.readLine();
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}
			if(line.startsWith("**")) break;
			sp=line.split(regex);
			if(sp.length<=2) break;
			id[0]=sp[1];
			powerSourceCurrent[ix]=sp[2];
			
			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] voltage=new String[ix];

	for(int i=0;i<ix;i++){
		voltage[i]=powerSourceCurrent[i];

	}
	
	//util.plot(voltage);
	//util.plot(NRerr);

	return voltage;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	
	

	
	public String[] loadSourceVoltageString(String file,int sourceIndex,String[] id){

		
		String[] powerSourceCurrent=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Power Sources")){}
			if(line==null) {break;}
			line=br.readLine();
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}
			if(line.startsWith("**")) break;
			sp=line.split(regex);
			if(sp.length<=2) break;
			id[0]=sp[1];
			powerSourceCurrent[ix]=sp[3];
			
			
			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] voltage=new String[ix];

	for(int i=0;i<ix;i++){
		voltage[i]=powerSourceCurrent[i];

	}
	
	//util.plot(voltage);
	//util.plot(NRerr);

	return voltage;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

	}

	
public String[] loadFieldSourceVoltageString(String file,int sourceIndex, String[] id){

		
	 String[] powerSourceCurrent=new  String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Sources ")){}
			if(line==null) {break;}
			line=br.readLine();
			
		
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}

			if(line.startsWith("**")) break;
			sp=line.split(regex);
			if(sp.length<=3) break;
			id[0]=sp[1];
			powerSourceCurrent[ix]=sp[3];
			
			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	 String[] voltage=new  String[ix];

	for(int i=0;i<ix;i++){
		voltage[i]=powerSourceCurrent[i];

	}
	
	//util.plot(voltage);
	//util.plot(NRerr);

	return voltage;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	

	
public String[] loadMagFluxString(String file,int loopID,String[] id){

		
		
	String[] magFluxTemp=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("*            Magnetic fluxes of flux loops")){}
	
			if(line==null) {break;}
			line=br.readLine();
			line=br.readLine();
			line=br.readLine();
			line=br.readLine();

			for(int i=0;i<loopID;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}
			if(line.startsWith("**")) break;
			sp=line.split(regex);
	
			if(sp.length<=2) break;
			id[0]=sp[1];
			magFluxTemp[ix]=sp[2];
	
			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] magFlux=new String[ix];

	for(int i=0;i<ix;i++){
		magFlux[i]=magFluxTemp[i];

	}

	//util.plot(voltage);
	//util.plot(NRerr);

	return magFlux;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	


	

public String[][] loadMagEnergyString(String file,int matIndex, String[] id){

	
	
	String[][] magEnergyTemp=new String[10000][3];

	try{
	
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
	
		while((line=br.readLine())!=null && !line.startsWith("*** Total magnetic")){}

		if(line==null) {break;}
		line=br.readLine();
		for(int i=0;i<matIndex;i++){
		line=br.readLine();
		if(line.startsWith("**")) break;
		}
		if(line.startsWith("**")) break;
		sp=line.split(regex);
	
		if(sp.length<=4) break;
	
		id[0]=sp[1];
	
		magEnergyTemp[ix][0]=sp[2];
		magEnergyTemp[ix][1]=sp[3];
		magEnergyTemp[ix][2]=sp[4];
		ix++;

		
		}
		
	
		
br.close();
fr.close();

if(ix==0) return null;

String[][] magFnergy=new String[ix][3];

for(int i=0;i<ix;i++){
	magFnergy[i]=magEnergyTemp[i];

}

//util.plot(voltage);
//util.plot(NRerr);

return magFnergy;
	}

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
}	




public String[] loadHeatString(String file,int matIndex, String[] id){

	
	
	String[] heatTemp=new String[10000];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("*** Total Joule heat")){}
		if(line==null) {break;}
		line=br.readLine();
		for(int i=0;i<matIndex;i++){
		line=br.readLine();
		if(line.startsWith("**")) break;
		}
		if(line.startsWith("**")) break;
		sp=line.split(regex);
	//util.pr(sp.length);
		
		if(sp.length<=2) break;
		id[0]=sp[1];
		heatTemp[ix]=sp[2];

		ix++;

		
		}
		
	
		
br.close();
fr.close();

if(ix==0) return null;

String[] magFnergy=new String[ix];

for(int i=0;i<ix;i++){
	magFnergy[i]=heatTemp[i];

}

//util.plot(voltage);
//util.plot(NRerr);

return magFnergy;
	}
	
	

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
}	


public Vect loadTimesSteps(int[] stepNumb,String file){

		
	Vect powerSourceCurrent=new Vect(stepNumb.length);

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("*                            Step No.   ")){}
		if(line==null) {break;}

		//line=br.readLine();

		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);
		
		stepNumb[ix]=Integer.parseInt(sp[3]);

		powerSourceCurrent.el[ix]=Double.parseDouble(sp[5]);

		ix++;

		
		}
		
br.close();
fr.close();

if(ix==0) return null;

Vect current=new Vect(ix);

for(int i=0;i<ix;i++){
	current.el[i]=powerSourceCurrent.el[i];
}

return current;
	}

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
	
}

public Vect loadTimesSteps(String file){

	
	
	Vect powerSourceCurrent=new Vect(10000);

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("----- Time step")){}
		if(line==null) {break;}

		//line=br.readLine();

		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);

		powerSourceCurrent.el[ix]=Double.parseDouble(sp[5]);

		ix++;

		
		}
		
br.close();
fr.close();

if(ix==0) return null;

Vect current=new Vect(ix);

for(int i=0;i<ix;i++){
	current.el[i]=powerSourceCurrent.el[i];

}

return current;
	}

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
	
}

public int[] getIterNumb(String file){

	
	
	int[] iterNumbs=new int[2];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;

		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("**** Total number of ICCG")){}
		if(line==null) {break;}

		//line=br.readLine();

		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);

		iterNumbs[0]=Integer.parseInt(sp[6]);
		
		while((line=br.readLine())!=null && !line.startsWith("**** Total number of nonlinear")){}
		if(line==null) {break;}

		sp=line.split(regex);

		iterNumbs[1]=Integer.parseInt(sp[6]);
		//line=br.readLine();
		//sp=line.split(regex);
		
		}
		
br.close();
fr.close();




	}

	catch(IOException e){System.err.println("Error in loading output file.");
	}

	return iterNumbs;
}

public int[] getNodeElNumbs(String file){

	
	
	int[] neNumbs=new int[2];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;

		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("                         total number of nodes")){}
		if(line==null) {break;}

		//line=br.readLine();

		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);

		neNumbs[0]=Integer.parseInt(sp[6]);
		
		while((line=br.readLine())!=null && !line.startsWith("                         total number of volume elements")){}
		if(line==null) {break;}

		sp=line.split(regex);

		neNumbs[1]=Integer.parseInt(sp[7]);
		//line=br.readLine();
		//sp=line.split(regex);
		
		}
		
br.close();
fr.close();




	}

	catch(IOException e){System.err.println("Error in loading output file.");
	}

	return neNumbs;
}	

public String[] getComputationTimesAndDate(String file){

	
	
	String[] tt=new String[11];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;

		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("*                     CPU Times")){}
		if(line==null) {break;}

		tt[0]=line;
		for(int i=1;i<7;i++){
		line=br.readLine();
		tt[i]=line;
		}
		
		for(int i=0;i<3;i++){
			line=br.readLine();
				}

		for(int i=8;i<11;i++){
			line=br.readLine();
			tt[i]=line;
			line=br.readLine();
				}

		}
		
br.close();
fr.close();



	}

	catch(IOException e){System.err.println("Error in loading output file.");

	}
	return tt;

	
}	

private int getRefine(String file){
	int refine=0;

	
	int L=file.length();
	
	int ix=0;
	while(file.charAt(L-1-ix)!='\\'){
		ix++;
		if(ix>L-2) break;
	}
	

	String checkfile="";
	for(int i=0;i<L-ix;i++)
		checkfile=checkfile+file.charAt(i);
	checkfile=checkfile+"check";
	
	try{
		FileReader fr=new FileReader(checkfile);
		BufferedReader br = new BufferedReader(fr);
		String line;


		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("* INPUT_MESH_FILE *  UNIT  * NO_MESH *")){}
		if(line==null) {break;}
		line=br.readLine();
		String[] sp2=line.split(regex);
		
		int ib=0;
		if(sp2[0].equals("")) ib=1;
		
		if(sp2.length-ib>5) refine=Integer.parseInt(sp2[ib+5]);
		

		}
		
br.close();
fr.close();


	}
	catch(IOException e){};

	return refine;
	
}

public String[] loadNetworkCurrentString(String file, int sourceIndex,String[] id){

	
	
	String[] output1=new String[10000];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("***  Network elements")){}
		if(line==null) {break;}

		line=br.readLine();
		for(int i=0;i<sourceIndex;i++){
		line=br.readLine();
		if(line.startsWith("*"))
		 break;
		}
		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);
		if(sp.length<=2) break;
		//util.pr(sp[2]);
		id[0]=sp[1];

		output1[ix]=sp[2];

		ix++;

		
		}
		
br.close();
fr.close();

if(ix==0) return null;

String[] output=new String[ix];

for(int i=0;i<ix;i++){
	output[i]=output1[i];

}

//util.plot(current);
//util.plot(NRerr);

return output;
	}

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
}	

public String[] loadNetworkVoltageString(String file, int sourceIndex,String[] id){

	
	
	String[] output1=new String[10000];

	try{
		FileReader fr=new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		String s;
		String[] sp;
		int ix=0;
		while((line=br.readLine())!=null){
		while((line=br.readLine())!=null && !line.startsWith("***  Network elements")){}
		if(line==null) {break;}

		line=br.readLine();
		for(int i=0;i<sourceIndex;i++){
		line=br.readLine();
		if(line.startsWith("*"))
		 break;
		}
		//util.pr(line);
	//	line=br.readLine();
		sp=line.split(regex);
		if(sp.length<=3) break;
		//util.pr(sp[2]);
		id[0]=sp[1];

		output1[ix]=sp[3];

		ix++;

		
		}
		
br.close();
fr.close();

if(ix==0) return null;

String[] output=new String[ix];

for(int i=0;i<ix;i++){
	output[i]=output1[i];

}

//util.plot(current);
//util.plot(NRerr);

return output;
	}

	catch(IOException e){System.err.println("Error in loading output file.");
	return null;
	}

	
}	

public String[] loadNetworkElementFluxString(String file, int sourceIndex,String[] id){

	String[] fieldSourceFlux1=new String[10000];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith("***  Network elements")){}
			if(line==null) {break;}
			line=br.readLine();
			
			
			sp=line.split(regex);
			if(sp.length<=4) break;
			for(int i=0;i<sourceIndex;i++){
			line=br.readLine();
			if(line.startsWith("**")) break;
			}
			if(line.startsWith("**")) break;
			sp=line.split(regex);
			if(sp.length<=4) break;
			id[0]=sp[1];
			fieldSourceFlux1[ix]=sp[4];

			ix++;

			
			}
			
	br.close();
	fr.close();
	
	if(ix==0) return null;
	
	String[] fieldSourceFlux=new String[ix];

	for(int i=0;i<ix;i++){
		fieldSourceFlux[i]=fieldSourceFlux1[i];

	}
	
	//util.plot(voltage);
	//util.plot(NRerr);

	return fieldSourceFlux;
		}

		catch(IOException e){System.err.println("Error in loading output file.");
		return null;
		}

		
	}	


}
