package compare;


import java.io.BufferedReader;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;

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



public class MPIexec {


	public static void main(String[] args) {


		String[] folders=new String[10];

		int ii=0;
		folders[ii++]="C:/Users/hassan/Desktop/notExec/field_source/COIL_No.1/para4";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/field_source/COIL_No.2/para4";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/field_source/PHICOIL_No.1/para4";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/field_source/PHICOIL_No.2/para4";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/field_source/PHICOIL_No.3/para4";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/eddy_current/No.1/para16";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/eddy_current/No.2/para16";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/eddy_current/No.3/para16";
		folders[ii++]="C:/Users/hassan/Desktop/notExec/eddy_current/No.4/para16";
		

		
		String[] batch=new String[ii];
		
		int jj=0;
		batch[jj++]="/run-mpi4.bat";
		batch[jj++]="/run-mpi4.bat";
		batch[jj++]="/run-mpi4.bat";
		batch[jj++]="/run-mpi4.bat";
		batch[jj++]="/run-mpi4.bat";
		batch[jj++]="/run-mpi16.bat";
		batch[jj++]="/run-mpi16.bat";
		batch[jj++]="/run-mpi16.bat";
		batch[jj++]="/run-mpi16.bat";
		
	



		String domainPath="C:/xxx/domain";
		//domainPath=System.getProperty("user.dir");//"C:/Users/hassan/JavaWorks/Compare/domain";



		//util.deleteDir(ff);
		//util.removedir(ff);
		// ff.delete();

		//if(!ff.exists()) ff.mkdirs();

		PrintWriter pw=null;

		BufferedReader br=null;


		FileReader fr=null;

		String[] dataFile=new String[3];
		dataFile[0]="/input";
		dataFile[1]="/2D_to_3D";
		dataFile[2]="/pre_geom2D.neu";
		
		String[] outFile=new String[3];
		outFile[0]="/check";
		outFile[1]="/output";
		outFile[2]="/stderr";
	
		
		ProcessBuilder builder = new ProcessBuilder();
	//	builder.command(domainPath+"/run-mpi1.bat");
		
	//	builder.command("mpiexec -n 1 C:/xxx/domain/EMSolBatch_MPI.exe -f C:/xxx/domain/input");
		

		builder.command(domainPath+"/run-mpi1.bat");
		//util.pr("mpiexec -n 1 EMSolBatch_MPI.exe -f input");

		builder.redirectError(Redirect.to(new File(domainPath+"/stderr")));
		builder.redirectOutput(Redirect.to(new File(domainPath+"/stdout")));
		Process pp = null;
		try {
		    pp = builder.start();
		  // pp=Runtime.getRuntime().exec("mpiexec -path C:/xxx/domaine -n 1 EMSolBatch_MPI.exe -f input");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		// Process has started here
		//pp.destroy();
		try {
			pp.waitFor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		    // i = 143
		    int i = pp.exitValue();
		    util.pr(i);
		} catch( IllegalThreadStateException e){
			e.printStackTrace();
		}
		
		


		for(int p=0;p<-1+0*ii;p++){


	
			for(int mm=0;mm<dataFile.length;mm++)
			{
				for(int hh=0;hh<4;hh++)
				try{	

			
					fr = new FileReader(new File(folders[p]+dataFile[mm]));
					br = new BufferedReader(fr);

				
					try{
						if(hh>0 && mm>1) continue;
						
						String folderx=domainPath;
						if(hh>0) folderx=domainPath+hh;
						
					
						pw = new PrintWriter(new FileWriter(folderx+dataFile[mm]));

						String line="";
						while(line!= null){
							line=br.readLine();
						
							pw.println(line);

						}

						pw.close();
					}
					catch(IOException e){e.printStackTrace();

					}

					 try {
					Thread.sleep(10);
					 }catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					br.close();
					fr.close();
					

				}
				

				catch(IOException e){e.printStackTrace();
				}
				
			}
				
				 Process proc=null;
					try {
						
					//	String ss="mpiexec -n "+1+" "+domainPath+"/EMSolBatch_MPI.exe -f "+ domainPath+"/input";
						String ss=domainPath+batch[p];
						
						//ss="mpiexec -n 1 C:/xxx/domain/EMSolBatch_MPI.exe -f C:/xxx/domain/input";

						//   proc=Runtime.getRuntime().exec("C:/xxx/domain/EMSI_x64.exe");
						proc = Runtime.getRuntime().exec(ss);
					//	proc = Runtime.getRuntime().exec(domainPath+"/run-mpi1");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        try {
						proc.waitFor();
						
						 if (proc.exitValue() == 0) {
						        System.out.println("Command exit successfully");
						    } else {
						        System.out.println("Command failed");
						    }
						
						for(int mm=0;mm<outFile.length;mm++)
						{
							try{	

								fr = new FileReader(new File(domainPath+outFile[mm]));
								br = new BufferedReader(fr);

								try{	
									pw = new PrintWriter(new FileWriter(folders[p]+outFile[mm]));

									String line="";
									while(line!= null){
										line=br.readLine();
									
										pw.println(line);

									}

									pw.close();
								}
								catch(IOException e){e.printStackTrace();

								}

								 try {
								Thread.sleep(10);
								 }catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								br.close();
								fr.close();
								

							}
							

							catch(IOException e){e.printStackTrace();
							}
							
						}

			
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			}
			
			
		}



	public  MPIexec(){}

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
