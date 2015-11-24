package compare;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class DataExtractor {
	String regex="[:; ,\\t]+";

	
	

	
public String[][] loadDataString(String file, String key,int nDataMax,int nTmax,PrintWriter stderr){

	
		
		String[][] data1=new String[nDataMax][nTmax];

		try{
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String s;
			String[] sp;
			int ix=0;
			int sourceIndex=0;
			while((line=br.readLine())!=null){
			while((line=br.readLine())!=null && !line.startsWith(key)){}
		
			if(line==null) {break;}
			line=br.readLine();
			//line=br.readLine();
			
			sourceIndex=0;

			data1[sourceIndex++][ix]=line;
			line=br.readLine();

			data1[sourceIndex++][ix]=line;
			line=br.readLine();

			while(line!=null && !line.startsWith("*")){
				sp=line.split(regex);
				if(sp.length<2) break;
				data1[sourceIndex][ix]=line;
			sourceIndex++;
		
			line=br.readLine();
			
			}
			
			ix++;
			}
			
	br.close();
	fr.close();

	if(ix==0) return null;

	String[][] data=new String[sourceIndex][ix];

	for(int i=0;i<sourceIndex;i++)
		for(int j=0;j<ix;j++){
		data[i][j]=data1[i][j];


	}


	return data;
		}

		catch(IOException e){stderr.println(e.getMessage());
		return null;
		}

		
	}	

public Vect loadTimesSteps(int[] stepNumb,String file,PrintWriter stderr){


	Vect time1=new Vect(stepNumb.length);

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

			sp=line.split(regex);

			stepNumb[ix]=Integer.parseInt(sp[3]);


			time1.el[ix]=Double.parseDouble(sp[5]);

			ix++;


		}

		br.close();
		fr.close();

		if(ix==0) return null;

		Vect time=new Vect(ix);

		for(int i=0;i<ix;i++){
			time.el[i]=time1.el[i];
		}

		return time;
	}

	catch(IOException e){stderr.println(e.getMessage());
	return null;
	}



}



public int[] getIterNumb(String file,PrintWriter stderr){



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

	catch(IOException e){stderr.println(e.getMessage());
	}

	return iterNumbs;
}

public int[] getNodeElNumbs(String file,PrintWriter stderr){



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

	catch(IOException e){stderr.println(e.getMessage());
	}

	return neNumbs;
}	

public String[] getComputationTimesAndDate(String file,PrintWriter stderr){



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

	catch(IOException e){stderr.println(e.getMessage());
	}
	return tt;


}


}
