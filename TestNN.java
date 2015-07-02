import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.StringTokenizer;


public class TestNN {

	public  static int a=0,z=0;
	public static double xorIdeal[][];
	public static double xorInput[][];
	public static double Input[][];
	public static double Output[];
	public static void main(String[] args) {
		
		String output[]=new String[50];
		try
		{
			FileInputStream fis=new FileInputStream("C:/javaprograms/BPpaper/src/initialize.txt");
			FileInputStream fis1=new FileInputStream("C:/javaprograms/BPpaper/src/traininputoutput.txt");
			FileInputStream fis2=new FileInputStream("C:/javaprograms/BPpaper/src/testinputoutput.txt");
			DataInputStream in =new DataInputStream(fis);
			DataInputStream in1 =new DataInputStream(fis1);
			DataInputStream in2 =new DataInputStream(fis2);
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			BufferedReader br1=new BufferedReader(new InputStreamReader(in1));
			BufferedReader br2=new BufferedReader(new InputStreamReader(in2));
		String string;
		while((string=br.readLine())!=null)
		{
			StringTokenizer s1=new StringTokenizer(string,"---------->");
			while(s1.hasMoreTokens()) 
			{
				s1.nextToken();
			   output[z]=s1.nextToken();
			      	z++;
			}
		}
		int row=Integer.parseInt(output[5]);
		int col=Integer.parseInt(output[0]);
		int outcol=Integer.parseInt(output[2]);
		int outrow=Integer.parseInt(output[6]);
		xorInput=new double[row][col];
		xorIdeal=new double[row][outcol];
		Input=new double[outrow][col];
		Output=new double[outrow];
		String string1;
		String[] sam1=new String[row];
		String[] sam2=new String[row];
		int p=0;
		while((string1=br1.readLine())!=null)
		{
			StringTokenizer s2=new StringTokenizer(string1,"---------->");
			while(s2.hasMoreTokens()) 
			{
				sam1[p]=s2.nextToken();
				sam2[p]=s2.nextToken();
				StringTokenizer s3=new StringTokenizer(sam1[p],",");
				StringTokenizer s4=new StringTokenizer(sam2[p],",");
				int g=0,q=0;
				while(s3.hasMoreTokens())
				{
					xorInput[p][g]=Double.parseDouble(s3.nextToken());
					g++;
				}
				while(s4.hasMoreTokens())
				{
					xorIdeal[p][q]=Double.parseDouble(s4.nextToken());
					q++;
				}
				p++;
			}
		}
		String string2;
		String[] sam3=new String[outrow];
		int k=0;
		while((string2=br2.readLine())!=null)
		{
			StringTokenizer s3=new StringTokenizer(string2,"---------->");
			while(s3.hasMoreElements())
			{
				int f=0;
				sam3[k]=s3.nextToken();
				Output[k]=Double.parseDouble(s3.nextToken());
				StringTokenizer s5=new StringTokenizer(sam3[k],",");
				while(s5.hasMoreElements())
				{
					Input[k][f]=Double.parseDouble(s5.nextToken());
					f++;
				}
				k++;
			}
		}
		}
		catch(Exception e)
		{
			System.out.println("Error *******"+e.getMessage());
		}
		  System.out.println("Learn:");

		  Network network = new Network(output[0],output[1],output[2],output[3],output[4]);

		  NumberFormat percentFormat = NumberFormat.getPercentInstance();
		  percentFormat.setMinimumFractionDigits(4);

		  for (int i=0;i<100;i++) {
		   for (int j=0;j<xorInput.length;j++) {
		    network.computeOutputs(xorInput[j]);
		    network.calcError(xorIdeal[j]);
		    network.learn();
		   }
		   System.out.println( "Trial #" + i + ",Error:" +
		             percentFormat .format(network.getError(xorInput.length)) );
		  }

		  System.out.println("Recall:");

		  for (int i=0;i<xorInput.length;i++) {

		   for (int j=0;j<xorInput[0].length;j++) {
		    System.out.print( xorInput[i][j] +":" );
		   }
		   double out[] = network.computeOutputs(xorInput[i]);
		   for(int z=0;z<3;z++)
		   System.out.println("="+out[z]);
		  }
		  System.out.println("Testing System........!!!!!!!!!");
			   
			   for (int j=0;j<Input.length;j++) {
				   double out1[] = network.learnedSystem(Input[j]);
				   System.out.println("For the "+(j+1)+" Input the Output Values Obtained are");
				   System.out.println( "Original Output: "+Output[j] +": " );
				   for(int z=0;z<3;z++)
				   System.out.print(out1[z]+",");
				   System.out.println("\n");
			   }
		  
			  
	}
}
