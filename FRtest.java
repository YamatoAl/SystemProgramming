import java.util.*;
import java.io.*;

public class FRtest{
	public static void main(String args[])throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(args[0]));
		
		String inLine;
		String inS[][]=new String[100][5];
		int lineCount=0;
		
		for(;(inLine=br.readLine())!=null;){
			inS[lineCount]=inLine.trim().split("\\s");
			lineCount++;
		}
		
		for(int i=0;i<lineCount;i++){
			System.out.println(inS[i][0]);
		}
	}
}