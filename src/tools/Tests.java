package tools;
import java.io.BufferedReader;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
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
*/


public class Tests {


	public static void main(String[] args) {
		
		Tests fst=new Tests();
		
		fst.readInput();
		
	}


	public  Tests(){}
	
	private void readInput(){

		String pp="C:/Users/hassan/Documents/Large Scale Test/FieldSources/domain";


		try{
			int N=16;
			int[] numS=new int[N];
		
			String num="";
			for(int k=0;k<N;k++){
				if(k>0) num=Integer.toString(k);
				String path=pp+num+"/stderr";
				//util.pr(k+" --> "+path);
				FileReader fr=new FileReader(path);
				BufferedReader br = new BufferedReader(fr);
				String line="";
			
	
				while(line!=null ){
					line=br.readLine();
					if(line!=null){
						numS[k]++;
					util.pr(k+" --> "+line);
					}
				}


			fr.close();
			br.close();
			}
			
			util.hshow(numS);
		}

		catch(IOException e){System.err.println("Failed in reading .");
		}
		
	}
	
	private String getNextDataLine(BufferedReader br) throws IOException{


		String line="*";

		while(line!=null && (line.startsWith("*") || line.startsWith("/"))) { line=br.readLine();}

		return line;
	}





}

