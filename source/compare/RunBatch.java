package compare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;

public class RunBatch {
	
	
	public static void main(String[] args) {

	
	
	ProcessBuilder builder = new ProcessBuilder();

		
	String path=util.getFile();

	if(path==null || path.equals("") )return;

		builder.command(path);

		
		File file=new File("exec_log.txt");

		
		builder.redirectOutput(Redirect.to(file));
		
        
		Process pp = null;
		try {
		    pp = builder.start();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
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
			
			
		
	}
		


		 pp.destroy();
	}

}
