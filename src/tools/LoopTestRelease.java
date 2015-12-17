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
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.math.plot.Plot2DPanel;



public class LoopTestRelease {



	public  LoopTestRelease(){

	};


	public static void main(String[] args) throws IOException{

		LoopTestRelease x=new LoopTestRelease();

		String root="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/fourcoilsAll/refine0";
		
		
		File rootFodler=new File(root);
		String[] names = rootFodler.list();
		
		
		int nDomains=8;
		
		
	
	
		for(int ifold=1;ifold<10;ifold++){
		
			String sourceFolder=root+"/"+names[ifold]+"/release";


		//	x.runRelease(sourceFolder);
			
					
			String destFolder=root+"/"+names[ifold]+"/MPI para "+nDomains;
			
			x.runMPI(nDomains,sourceFolder,destFolder);
			
		}
		
		//x.test();

	}

	public  void runRelease(String sourceFolder){
		


			//sourceFolder=root+"/"+names[ifold]+"/release";
			
		File source1=new File(sourceFolder+"/input");
		File dest1=new File("input");
		
		File source2=new File(sourceFolder+"/pre_geom2D.neu");
		File dest2=new File("pre_geom2D.neu");
		
		File source3=new File(sourceFolder+"/2D_to_3D");
		File dest3=new File("2D_to_3D");

		
	    try {
	    	
	    	util.copyFile(source1, dest1);
	    	util.copyFile(source2, dest2);
	    	util.copyFile(source3, dest3);
	    	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    
	    
		ProcessBuilder builder = new ProcessBuilder();

	//	String command="EMSolBatch_x64.exe -f input";
		builder.command("EMSolBatch_x64.exe","input");

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
			    util.pr(i);
			} catch( IllegalThreadStateException e){
				e.printStackTrace();
			}
			
			pp.destroy();
	    
/*	    Process proc=null;
		
		try {
			String command="EMSolBatch_x64.exe -f input";
			proc=Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	try {
		proc.waitFor();
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		*/	
			File source4=new File("check");
			File dest4=new File(sourceFolder+"/check");
			
			File source5=new File("output");
			File dest5=new File(sourceFolder+"/output");
			
			File source6=new File("stderr");
			File dest6=new File(sourceFolder+"/stderr");

			

	    	try {
				util.copyFile(source4, dest4);
				util.copyFile(source5, dest5);
		    	util.copyFile(source6, dest6);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		
	    	
		
	}	
	
	public  void runMPI(int nDomains, String sourceFolder, String destFolder){
		
		
		

		
		

		//sourceFolder=root+"/"+names[ifold]+"/release";
		
	File source1=new File(sourceFolder+"/input");
	File dest1=new File("input");
	
	File source2=new File(sourceFolder+"/pre_geom2D.neu");
	File dest2=new File("pre_geom2D.neu");
	
	File source3=new File(sourceFolder+"/2D_to_3D");
	File dest3=new File("2D_to_3D");

	
    try {
    	
    	this.setNO_Mesh(nDomains,source1, dest1);
    	util.copyFile(source2, dest2);
    	util.copyFile(source3, dest3);
    	
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
    try {
		Thread.sleep(500);
	} catch (InterruptedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	

for(int j=1;j<nDomains;j++){
	
	String domainx="../domain"+j;
	
	 dest1=new File(domainx+"/input");
	 
	 File source1modified=new File("input");
	
	 dest2=new File(domainx+"/pre_geom2D.neu");
	
	 dest3=new File(domainx+"/2D_to_3D");
	 
		
	    try {
	    	util.copyFile(source1modified, dest1);
	    	util.copyFile(source2, dest2);
	    	util.copyFile(source3, dest3);
	    	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    

	
}
    


	
	File logFile=new File("exec_log.txt");

	
	

	String command="run-mpi"+nDomains+".bat";
	

String dmainFolder=System.getProperty("user.dir");



//String[] cmdAndArgs = {"cmd.exe", "/C", command};

String[] cmdAndArgs = {"cmd.exe", "/C","mpiexec", "-n",Integer.toString(nDomains),dmainFolder+"/EMSolBatch_MPI.exe","-f", "input"};


ProcessBuilder builder = new ProcessBuilder(cmdAndArgs);

	
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
		    util.pr(i);
		} catch( IllegalThreadStateException e){
			e.printStackTrace();
		}
	
/*		try {
		String command="EMSolBatch_x64.exe -f input";
		Runtime.getRuntime().exec(command);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
		
	
	
		
		File fdestFolder2=new File(destFolder);
		if(!fdestFolder2.exists()) fdestFolder2.mkdirs();
	
		
		
		 source1=new File("input");
		 dest1=new File(destFolder+"/input");
		
		source2=new File("pre_geom2D.neu");
		dest2=new File(destFolder+"/pre_geom2D.neu");
		
		source3=new File("2D_to_3D");
		 dest3=new File(destFolder+"/2D_to_3D");
	
		File source4=new File("check");
		File dest4=new File(destFolder+"/check");
		
		File source5=new File("output");
		File dest5=new File(destFolder+"/output");
		
		File source6=new File("stderr");
		File dest6=new File(destFolder+"/stderr");
		
		File source7=new File("exec_log.txt");
		File dest7=new File(destFolder+"/exec_log.txt");
	

		

    	try {
    		
    		util.copyFile(source1, dest1);
			util.copyFile(source2, dest2);
	    	util.copyFile(source3, dest3);
			util.copyFile(source4, dest4);
			util.copyFile(source5, dest5);
	    	util.copyFile(source6, dest6);
	    	util.copyFile(source7, dest7);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	
    	
		pp.destroy();
	
    	}	
	
	
	
	
	public void test(){
		
		String root="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/fourcoilsALl/refine0";
		
		
		File rootFodler=new File(root);
		
		String[] names = rootFodler.list();

		int ix=0;
		for(String name : names)
		{
			
			util.pr(name);
		/*    if (new File("C:\\Windows\\" + name).isDirectory())
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
	
	
	
	private void setNO_Mesh(int nDomains,File source, File dest){
		
		String regex="[:; . ,\\t]+";
		
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

			if(manip){
				String[] sp=line.split(regex);
				int ib=0; if(sp[0].equals("")) ib=1;
				String nMesh=Integer.toString(nDomains);
				
				sp[ib+2]=nMesh;
				line="";

				for(int j=0;j<sp.length;j++ )

					line=line+sp[j]+"\t";

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

}



