package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;


public class InputMaker {

	
	String[] comment=new String[1000];
	
	String[][][] inputData=new String[1000][50][2];

	
	
	public int SOLVE_EQUATION= 1;
	public int POST_PROCESSING= 1;
	public int STATIC= 1;
	public int STEP= 0;
	public int AC= 0;
	public int TRANSIENT= 0;
	public int MOTION= 2;
	public int NON_LINEAR= 1;
	public int TEMP_DEPEND= 0;
	public int STEADY_CURRENT= 0;
	public int POTENTIAL= 0;
	public int NODAL_FORCE_OPTION= 0;
	public int PHI_OPTION= 0;
	public int FIXED_COODINATE= 0;
	public int TREE_GAUGE= -1;
	public int REGULARIZATION= 0;
	public int RENUMBERING= -1;
	public int SCALING= 0;
	public int LINE_SERACH= 1;
	public int NODE_ORDER= 1;
	public int EDGE_ORDER= 1;
	public int METRIC_MOD= 0;
	public int QUAD_TRI= 0;
	public int CALC_IND= 0;
	public int THIN_ELEM= 0;
	public int PARALLEL_NO= 2;
	public int PARALLEL_OPTION= 0;
	public int DIV_FACTOR= 20;
	public int DIV_ITERATIONS= 100;
	public int SOLVER= 0;
	public int RELAXATION= 1;
	public int NO_ITERATIONS= 20;
	public int INIT_OPTION= 0;
	public int POINTS_IN_1D= 3;
	public int POINTS_IN_IN_TRIANGLE= 7;
	public int MAT_POINTS= 1;
	public int INITIAL_STEP= -1;
	public int DATA_TYPE= 0;
	public int INITIAL_PHASE= 0;
	public int MULTI= 0;
	public int NO_STEPS= 1;
	public int INITIAL_STEP2= 1;
	public int LAST_STEP= 200;
	public int STEP_IintERVAL= 1;
	public int TIME_DIFF= 3;
	public int INPUT_MESH_FILE= 5;
	public int UNIT= 0;
	public int NO_MESH= 2;
	public int MESHLESS= 0;
	
	public double ICCG_CONV= 1e-006;
	
	public double ACCEL_FACTOR= 1.02;
	public double THETA= 0.6666667;
	public double THETA_NETWOEK= 0.5;
	public double THETA_MOTION= 0.5;
	public double NON_LINERAR_CONV= 0.0001;
	public double ICCG_CONV_RATIO= 0.01;
	public double CHECK_B= 0.01;
	public double INITIAL_TIME= -1.;
	public double DELTA_TIME= 6.0;
	public double DELTA_Z= 0.037500;

	public double ANGLE= 90.;
	public double SIGMA= 0.0;
	public double MU= 1.0;
	
	public double MX_R= 0.6363961;
	public double MY_T= 0.6363961;
	public double MZ= 0.;
	
	public double RLS_NO=11.0;
	
	
	public InputMaker(){
		
		int ix=-1;
		
		comment[++ix]="* RLS_NO *";
		inputData[ix][1][0]="11.0";
		
		comment[ix++]="* PRE_PROCESSING * MAKE_SYSTEM_MATRICES *";
		inputData[ix][1][0]="1";
		inputData[ix][2][0]="1";
		
		comment[ix++]="* SOLVE_EQUATION * POST_PROCESSING *";
		inputData[ix][1][0]="1";
		inputData[ix][2][0]="1";
		
		comment[ix++]="* STATIC * STEP * AC * TRANSIENT * MOTION * NON_LINEAR * TEMP_DEPEND * STEADY_CURRENT *";
		inputData[ix][1][0]="1";
		inputData[ix][2][0]="0";
		inputData[ix][3][0]="0";
		inputData[ix][4][0]="0";
		inputData[ix][5][0]="2";
		inputData[ix][6][0]="1";
		inputData[ix][7][0]="0";
		inputData[ix][8][0]="0";
		
		comment[ix++]="* POTENTIAL * NODAL_FORCE_OPTION * PHI_OPTION * FIXED_COODINATE *";
		

		inputData[ix][1][0]="0";
		inputData[ix][2][0]="0";
		inputData[ix][3][0]="0";
		inputData[ix][4][0]="0";

		
		
		comment[ix++]="* TREE_GAUGE * REGULARIZATION * RENUMBERING * SCALING * LINE_SERACH * MATRIX_ASYMMETRICITY *";
		comment[ix++]="* NODE_ORDER * EDGE_ORDER *  METRIC_MOD * QUAD_TRI * CALC_IND *  THIN_ELEM * PARALLEL_NO * PARALLEL_OPTION";
		comment[ix++]="* ICCG_CONV * ACCEL_FACTOR * DIV_FACTOR * DIV_ITERATIONS * SOLVER * THETA * THETA_NETWOEK * THETA_MOTION * MAX_ITERATIONS *";
		comment[ix++]="* NON_LINERAR_CONV * RELAXATION * NO_ITERATIONS * INIT_OPTION * ICCG_CONV_RATIO * CHECK_B * DELTA_A * NON_LINEAR_METHOD *";
		comment[ix++]="* POINTS_IN_1D * POINT_IN_TRIANGLE * MAT_POINTS * ";
		comment[ix++]="* INITIAL_STEP * DATA_TYPE * INITIAL_PHASE * MULTI * ";
		comment[ix++]="* NO_STEPS * INITIAL_TIME *  DELTA_TIME * NO_DATA * CYCLE * N_CORRECT * N_BACK * TP_EEC_OPTION *";
		comment[ix++]="* INITIAL_STEP * LAST_STEP * STEP_INTERVAL * TIME_DIFF * RESTART_FILE_OPTION * CONT *";
		comment[ix++]="* INPUT_MESH_FILE *  UNIT  * NO_MESH *  MESHLESS *";
		comment[ix++]="* POST_DATA_FILE * ELEM_OUT * NODE_OUT * NUMBER_OUTPUT_MATS * AVERAGE * WIDE * SUF_OPTION * POST_COORDINATE * COIL_OPTION * POST_FFT *";
		comment[ix++]="* MESH * CURRENT * MAGNETIC * FORCE_J_B * FORCE_NODAL * DISP * ELEM * HEAT * MAGNETIZATION * IRON_LOSS *   E   * COIL_FORCE *";
		comment[ix++]="*   MESH   *   A   *   V   *   B   *   B_INTEG *    J_Q  *";
		comment[ix++]="*  Q_AVERAGE * HEAT  *  MAG_FLUX  * CUR_FLUX *";
		comment[ix++]="* FORCE_J_B * FORCE_NODAL * MAGNETIC_ENERGY * IRON_LOSS *";
		comment[ix++]="* GEOMETRY * DELTA_Z(m) or DETLA_THETA* NO_LAYERS * ADD_SYMMETRY * PITCH *";
		comment[ix++]="*COORDINATE * NO_COORDINATES *";
		comment[ix++]="* FAR_BOUNDARY_CONDITION ";
		comment[ix++]="* DISTANCE_JUDGE(m) *";
		comment[ix++]="* NO_BN0_PLANES *";
		comment[ix++]="* NO_HT0_PLANES *";
		comment[ix++]="* NO_A0_LINE *";
		comment[ix++]="* CYCLIC  *  SYMMETRICITY * DIVISION * SURFACE_MAT_ID * FITTNESS * LINE_INPUT * N_CYCLIC *";
		comment[ix++]="*   ANGLE *";
		comment[ix++]="* NO_MAT_IDS *  EXTEND_TOTAL *  NO_SMAT_IDS ";
		comment[ix++]="* MAT_ID  *  POTENTIAL * B_H_CURVE_ID * SIGMA * MU  * PACKING *  ANISOTROPY * IRON_LOSS * SIGMA_DEPEND_ID *";
		

		ix=0;
	
		comment[++ix]="* RLS_NO *";
		inputData[ix][1][0]="11.0";
		inputData[ix][1][1]="RLS_NO";




		
	}
	
	public static void main(String[] args){

		InputMaker im=new InputMaker();

		String file="C:/Users/hassan/Documents/Large Scale Test/FieldSources/results/latest_tests2015.12.16/slide_test/simple-test_mag/sequential2mesh/check";
		im.readInput(file);

		

		

	}
	
	
	
	public void makeInput(String folder){
		
		int ix=0;

/*		
		util.pr(comment[ix++]);
		util.pr(RLS_NO);
		for(int)
		util.pr(PRE_PROCESSING * MAKE_SYSTEM_MATRICES );
		util.pr(RLS_NO);*/
	
		
		
		
	}
	
	
	public void readInput(String file){
		
		int ix=0;
		
	

		try{	
			FileReader fr=new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line1,line2;

			line1="";
			
	
			while(line1!=null){
			
				line1=util.getNextCommentLine(br);
				//util.pr(line1);
				if(line1==null) break;
		
			line2=util.getNextDataLine(br);
			if(line2==null) break;
		//	util.pr(line2);
			//util.pr("comment[ix++]="+"\""+line+"\";");
				if(ix>1 && ix<55) {
					String [] sp1=line1.split("[:;,\\* \\t]+");
					
					int ib1=0; if(sp1[0].equals("")) ib1=1;
					
					String [] sp2=line2.split("[:;, \\t]+");
					
					String comment="";
					for(int j=0;j<sp1.length;j++)
						comment=comment+sp1[j]+"* ";
					
					int ib2=0; if(sp2[0].equals("")) ib2=1;
					util.pr("comment[++ix]=\""+comment+"\";");
					for(int j=ib2;j<sp2.length;j++){
						util.pr("inputData[ix]["+(j-ib2)+"][0]=\""+sp2[j]+"\";");
						if(j<sp1.length)
						util.pr("inputData[ix]["+(j-ib2)+"][1]=\""+sp1[j]+"\";");
						else
							util.pr("inputData[ix]["+(j-ib2)+"][1]=\""+""+"\";");
					}
					
					util.pr("");
					//util.pr("ix++;");
/*			//	util.pr(sp2[1]);
					for(int j=ib2;j<sp2.length;j++){
		//util.pr(sp2[j]);
						boolean match = sp2[j].contains(".");
						//util.pr(match);
						//boolean match = sp2[j].contains(".");
					//	util.pr(match);
					if(match){
						util.pr("double "+sp1[j]+"= "+sp2[j]+";");
					}
					else
						util.pr("int "+sp1[j]+"= "+sp2[j]+";");

					}*/
						
						
						
				//	line=util.getNextDataLine(br);
				}
			ix++;

			}


			
			br.close();

	fr.close();
		
		
		
	}	catch(IOException e){System.err.println("Failed in reading input file.");
	}


		
	}
	

}
