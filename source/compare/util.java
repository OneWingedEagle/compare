package compare;

import static java.lang.Math.*;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;







import org.math.plot.Plot2DPanel;

public class util {

	static String regex="[ : ,=\\t]+";
	
	public util(){}
	
	public static void main2(String[] args) throws Exception{
		
		double[] y=new double[100];
		for(int i=0;i<y.length;i++)
			y[i]=util.triangWave(i*.02);
		
		plot(y);
		
		util.show(y);

	}
	
	
	public static double max(double[] x){
		double max=x[0];
		for(int i=1;i<x.length;i++)
		if(x[i]>max)
			max=x[i];
		return max;
	}
	
	public static int max(int[] x){
		int max=x[0];
		for(int i=1;i<x.length;i++)
		if(x[i]>max)
			max=x[i];
		return max;
	}	
	public static int indmax(double[] x){
		int indmax=0;
		double max=x[0];
		for(int i=1;i<x.length;i++)
		if(x[i]>max)
			indmax=i;
		return indmax;
	}


	
	public static double getAng(Vect v){
		double ang=0;
		if(v.norm()==0) return ang;
		else if(v.el[0]>=0 && v.el[1]>=0) ang=atan(abs(v.el[1]/v.el[0]));
		else if(v.el[0]<=0 && v.el[1]>=0) ang=PI-atan(abs(v.el[1]/v.el[0]));
		else if(v.el[0]>=0 && v.el[1]<=0) ang=2*PI-atan(abs(v.el[1]/v.el[0]));
	
		else ang=atan(abs(v.el[1]/v.el[0]))+Math.PI;
		
		return ang;
	}
	
	public  static File getJFile(int mode){
		 JFileChooser fileChooser = new JFileChooser();
		 File theDirectory = new File(System.getProperty("user.dir"));
		 fileChooser.setCurrentDirectory(theDirectory);
	        int returnValue;
	        if(mode==0)
	        returnValue = fileChooser.showOpenDialog(null);
	        else
		        returnValue = fileChooser.showSaveDialog(null);

	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          File selectedFile = fileChooser.getSelectedFile();
	          System.out.println(selectedFile.getPath());
	          
	          
	  		return selectedFile;

	        }
	      
		return null;
	}
	

	
	public  static String getFile(int mode){
		String filePath="";
		FileDialog fd;
		Frame f=new Frame();
		if(mode==0)
		fd= new FileDialog(f,"Select  file",FileDialog.LOAD);
		else
		fd= new FileDialog(f,"Select  file",FileDialog.SAVE);
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
	
	public  static String getFile(){
		
		return getFile(0);
	}
	
	
	public static void shuffle(int[] ar){
		 Random rnd = new Random();
		    for (int i = ar.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      int a = ar[index];
		      ar[index] = ar[i];
		      ar[i] = a;
		    }
		  }
		
	public static int[] sortind(int[] a){
		int[] ind=new int[a.length];
		int[][] v=new int[a.length][2];
		 for(int i=0;i<a.length;i++){
			 v[i][0]=a[i];
			 v[i][1]=i;
		 }
		 int[] temp=new int[2];
		 for(int i=0;i<a.length-1;i++){
			 for(int j=0;j<a.length-i-1;j++)
			 if(v[j+1][0]<v[j][0]){
					 temp=v[j];    
					v[j]=v[j+1];
					 v[j+1]=temp;

				  }
		
		 }
		 for(int i=0;i<a.length;i++)
			 ind[i]=v[i][1];
		return ind;
}

	public static double[][][] grid(double[] x, double[] y){
		double[][][] grid= new double[2][y.length][x.length];
		for(int i=0;i<y.length;i++)
			for(int j=0;j<x.length;j++){
				grid[0][i][j]=x[j];
				grid[1][i][j]=y[i];}
		return grid;
			}
	
	public static void show(double[][] A){
		for(int i=0;i<A.length;i++){
			for(int j=0;j<A[0].length;j++)
				System.out.format("%12.4f",A[i][j]);
			System.out.println();
	}
		System.out.println();
	}
	public static void show(double[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%12.4f\n",v[i]);
			System.out.println();
	}
	
	public static void hshow(double[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%12.4f",v[i]);
			System.out.println();
	}
	
	public static double[] times(double[] v,double a){
		double[] y=new double[v.length];
		for(int i=0;i<v.length;i++)
				y[i]=a*v[i];
		return y;
	}
	
	public static double[][] times(double[][] M,double a){
		double[][] y=new double[M.length][M[0].length];
		for(int i=0;i<M.length;i++)
			for(int j=0;j<M[0].length;j++)
				y[i][j]=M[i][j]*a;
		return y;
	}
	
	
	public static double saw(double t)
	{
		
		double s=sin(t);
		return s;
	/*	double c=cos(t);
		
		double y=0;
		
		if(s>0 && c>0)
			y_
		int k=(int)(t);
		double rem=t-k;

	
			double y=0;
			if(rem<=.25)
			y=4*rem;
			else if(rem<=.75)
				y=2-4*rem;
			else 
				y=-4+4*rem;
				
		
		
		return y;*/
	}
	

	
	public static double triangWave(double t)
	{
		
	
		double k=floor(t);
		double rem=t-k;

	
			double y=0;
			if(rem<=.25)
			y=4*rem;
			else if(rem<=.75)
				y=2-4*rem;
			else 
				y=-4+4*rem;
				
		
		
		return y;
	}
	
	public static void show(int[][] A){
		for(int i=0;i<A.length;i++){
			for(int j=0;j<A[0].length;j++)
				System.out.format("%d\t",A[i][j]);
			System.out.println();
	}
		System.out.println();
	}
	public static void show(int[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%d\n",v[i]);
			System.out.println();
	}
	public static void hshow(int[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%d\t",v[i]);
			System.out.println();
	}
	
	public static void hshow(String[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%s\t",v[i]);
			System.out.println();
	}
	
	public static void show(byte[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%d\t",v[i]);
			System.out.println();
	}
	
	public static void show(boolean[] v){
		for(int i=0;i<v.length;i++)
				System.out.format("%s\t",v[i]);
			System.out.println();
	}
	
	public static void show(boolean[][] A){
		for(int i=0;i<A.length;i++){
			for(int j=0;j<A[0].length;j++)
				System.out.format("%s\t",A[i][j]);
			System.out.println();
	}
	}
	
	
	public static void show(String[] s){
		for(int i=0;i<s.length;i++){
				System.out.format("%s\n",s[i]);
	}
	}
	
	public static void pr(double a){
	
				System.out.println(a);
	
	}
	
	public static void pr(String a){
		
		System.out.println(a);

}
	public static void pr(int a){
		
		System.out.println(a);

}
	public static void pr(boolean b){
		
		System.out.println(b);

}
	
	
	public static void ph(double a){
		
		System.out.print(a);

}
	public static void ph(int a){
		
		System.out.print(a);

}
	
	public static void ph(String a){
		
		System.out.print(a);

}

	
	public static void plot(Vect y){
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++)
			x[i]=i;
		plot(x,y.el);
	}
	
	public static void plot(double[] y){
		double[] x=new double[y.length];
		for(int i=0;i<x.length;i++)
			x[i]=i;
		plot(x,y);
	}
	
	public static void plot(Vect x, Vect y){
		plot(x.el,y.el);
	}
	
	public static void plot(double[] x, double[] y){
		
		plot("y=f(x)",Color.black,x,y);
	}
	


	
public static void plot(double[][] XY){
		
		plot("y=f(x)",Color.black,XY);
	}

	public static void plot(String name, Color c,double[] x, double[] y){

		 double[][] A=new double[x.length][2];
		 for(int i=0;i<x.length;i++){
			 A[i][0]=x[i];
			 A[i][1]=y[i];
			 
		 }
		
		 plot(name,c,A);
		 
		
	}
	
	
	 
		public static void deleteDir(File dir) {
			if (dir.isDirectory()) {

				String[] children = dir.list();
				for (int i=0; i<children.length; i++) {
					deleteDir(new File(dir, children[i]));

				}
			}
			else
				dir.delete();



		}
	
		
	public static void plot(String name, Color c,double[][] XY){
		
		 Plot2DPanel plot = new Plot2DPanel();
		
		  plot.setFont( new Font("Times New Roman", 1, 13));
		 plot.addLinePlot(name, c, XY);
		//plot.setFont( new Font("Times New Roman", 1, 120));
	//	 util.pr(plot.getFont().toString());
		  JFrame frame = new JFrame("a plot panel");
		   frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  frame.setSize(500,400);
		  frame.setContentPane(plot);
		  frame.setVisible(true);
	
		
	}
	
	public static void plotBunch(double[][] data){
	//	DecimalFormat df=new DecimalFormat("00.0");

		Plot2DPanel plot = new Plot2DPanel();
		double[] x=new double[data.length];
		double[] y=new double[data.length];
		
		for(int i=0;i<data.length;i++)
			x[i]=data[i][0];

		for(int j=0;j<data[0].length-1;j++){
			for(int i=0;i<x.length;i++)
			y[i]=data[i][j+1];
			plot.addLinePlot(" curve  "+j, x, y);
		
				}
		
		plot.setAxisLabel(0,"x");
		plot.setAxisLabel(1,"y");
		plot.addLegend("EAST");
		
		  JFrame frame = new JFrame("plot panel");
		   frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  frame.setSize(500,400);
		  frame.setContentPane(plot);
		  frame.setVisible(true);

	}
	
	public static void plotBunch(double[][]... data){
		
		int n=data.length;
		String[] name=new String[n];
		for(int j=0;j<n;j++)
			name[j]="curve "+j;
		
		plotBunch(name,data);
	}
	
	public static void plotBunch(String[] name, double[][]... data){
	//	DecimalFormat df=new DecimalFormat("00.0");

		Plot2DPanel plot = new Plot2DPanel();
		double[] x,y;
		
		for(int j=0;j<data.length;j++){
		
			x=new double[data[j].length];
			y=new double[data[j].length];
			for(int i=0;i<x.length;i++){
				x[i]=data[j][i][0];
				y[i]=data[j][i][1];
			}
		
		
			plot.addLinePlot(name[j], x, y);
		
				}
		
		plot.setAxisLabel(0,"x");
		plot.setAxisLabel(1,"y");
		plot.addLegend("EAST");
		
		  JFrame frame = new JFrame("plot panel");
		   frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		  frame.setSize(500,400);
		  frame.setContentPane(plot);
		  frame.setVisible(true);

	}

	
	
	public static Vect Atiken(Vect v2,Vect v1, Vect v){
		Vect Av=new Vect(v.length);
		for(int i=0;i<Av.length;i++)
			Av.el[i]=(v2.el[i]*v.el[i]-v1.el[i]*v1.el[i])/(v2.el[i]-2*v1.el[i]+v.el[i]);
		
		return Av;
	}
	public  static double  Atiken(double x2,double x1, double x){
		double Ax;
		
			Ax=(x2*x-x1*x1)/(x2-2*x1+x);
		
		return Ax;
	}

	
	public static void quickSort(double[] x){
		 Sort.quick(x);
		
	}
	
	
	public static void quickSort(double[] x, int[] ind){
		 Sort.quick(x,ind);
		
	}
	public static int search(int[] A,int ic,int a){
		int m=-1;
		for(int i=0;i<ic+1;i++){
			if(A[i]==a){
				m=i;
				break;
			}
		}
		return m;
	}

	public static int search(int[] A,int a){
		int m=-1;
		for(int i=0;i<A.length;i++){
			if(A[i]==a){
				m=i;
				break;
			}
		}
		return m;
	}
	
	 static public double J1(double x) {

		    double ax;
		    double y;
		    double ans1, ans2;

		    if ( (ax = Math.abs(x)) < 8.0) {
		      y = x * x;
		      ans1 = x * (72362614232.0 + y * ( -7895059235.0 + y * (242396853.1
		          + y * ( -2972611.439 + y * (15704.48260 + y * ( -30.16036606))))));
		      ans2 = 144725228442.0 + y * (2300535178.0 + y * (18583304.74
		          + y * (99447.43394 + y * (376.9991397 + y * 1.0))));
		      return ans1 / ans2;
		    } else {
		      double z = 8.0 / ax;
		      double xx = ax - 2.356194491;
		      y = z * z;

		      ans1 = 1.0 + y * (0.183105e-2 + y * ( -0.3516396496e-4
		                                           +
		                                           y * (0.2457520174e-5 + y * ( -0.240337019e-6))));
		      ans2 = 0.04687499995 + y * ( -0.2002690873e-3
		                                  + y * (0.8449199096e-5 + y * ( -0.88228987e-6
		          + y * 0.105787412e-6)));
		      double ans = Math.sqrt(0.636619772 / ax) *
		          (Math.cos(xx) * ans1 - z * Math.sin(xx) * ans2);
		      if (x < 0.0) ans = -ans;
		      return ans;
		    }
		  }
	 
	 public static int fact(int k){
			int f=1;
			
			for(int i=1;i<=k;i++)
				f*=i;
			
			return f;
		}
	 
	 public static String first(String line){
			
			String[] sp=line.split(regex);
			int b=0;
			while(b<sp.length-1 &&sp[b].equals("")){b++;}
			
			return sp[b];
		}
		


	   public static File copyFileFromWeb(String address, String filePath){
	     byte[] buffer = new byte[1024];
	     int bytesRead;
	  try{
	     URL url = new URL(address);
	     util.pr(url.toString());
	     BufferedInputStream inputStream = null;
	     BufferedOutputStream outputStream = null;
	     URLConnection connection = url.openConnection();
	     // If you need to use a proxy for your connection, the URL class has another openConnection method.
	     // For example, to connect to my local SOCKS proxy I can use:
	     // url.openConnection(new Proxy(Proxy.Type.SOCKS, newInetSocketAddress("localhost", 5555)));
	     inputStream = new BufferedInputStream(connection.getInputStream());
	     File f = new File(filePath);
	     util.pr(f.toString());
	     outputStream = new BufferedOutputStream(new FileOutputStream(f));
	     while ((bytesRead = inputStream.read(buffer)) != -1) {
	       outputStream.write(buffer, 0, bytesRead);
	     }
	     inputStream.close();
	     outputStream.close();
	     return f;
	  }catch(Exception e){
		  e.printStackTrace();
		  return null;
	  }
	  
	   }
	 


		   public static boolean isNumeric(String str)  
		   {  
			
		     try  
		     {  

		       double d = Double.parseDouble(str);  
		     }  
		     catch(NumberFormatException nfe)  
		     {  
		       return false;  
		     }  
		     return true;  
		   }

	
	
	
}
