import java.util.*;
import java.io.*;
import java.lang.*;

public class assembler{
	public static void main(String args[])throws IOException{
		BufferedReader br=new BufferedReader(new FileReader(args[0]));
		
		String inLine[]=new String[200];
		String inS[][]=new String[200][10];
		int lineCount=0;
		int loc[]=new int[200];
		int nowLoc=0;
		int outFront[]=new int[200];
		int outBack[]=new int[200];
		
		//Input
		for(;(inLine[lineCount]=br.readLine())!=null;){
			inS[lineCount]=inLine[lineCount].replaceAll("	","").trim().split("\\s");
			lineCount++;
		}
		
		//LOC
  		loc[0]=Integer.parseInt(inS[0][2],16);
		loc[1]=loc[0];
		nowLoc=loc[1];
		for(int i=2;i<lineCount-1;i++){
			
			if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))continue;
			else if(inS[i].length>2 && inS[i][2].equals("RESB"))loc[i]=nowLoc+Integer.parseInt(inS[i][5]);
			else loc[i]=nowLoc+3;
			
			if(inS[i].length>2 && inS[i][1].equals("RESB"))nowLoc+=Integer.parseInt(inS[i][3]);
			else if(inS[i].length>2 && inS[i][0].equals("INPUT") && inS[i][1].equals("BYTE"))nowLoc+=1;
			else nowLoc=loc[i];
			
		}
 		
		//opTable
		for(int i=1;i<lineCount;i++){
			switch(inS[i].length){
				case 1:
					if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))break;
					else outFront[i]=opTable.trans(inS[i][0]);
					break;
				case 2:
					if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))break;
					else outFront[i]=opTable.trans(inS[i][0]);
					break;
				case 3:
					if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))break;
					else if(opTable.trans(inS[i][0])!=0)outFront[i]=opTable.trans(inS[i][0]);
					else if(opTable.trans(inS[i][1])!=1)outFront[i]=opTable.trans(inS[i][1]);
					else outFront[i]=opTable.trans(inS[i][2]);
					break;
				case 4:
					if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))break;
					else if(opTable.trans(inS[i][0])!=0)outFront[i]=opTable.trans(inS[i][0]);
					else if(opTable.trans(inS[i][1])!=1)outFront[i]=opTable.trans(inS[i][1]);
					else outFront[i]=opTable.trans(inS[i][2]);
					break;
				case 5:
					if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))break;
					else if(opTable.trans(inS[i][0])!=0)outFront[i]=opTable.trans(inS[i][0]);
					else if(opTable.trans(inS[i][1])!=1)outFront[i]=opTable.trans(inS[i][1]);
					else if(opTable.trans(inS[i][2])!=2)outFront[i]=opTable.trans(inS[i][2]);
					else outFront[i]=opTable.trans(inS[i][3]);
					break;
			}
		}
		
		//find address
		for(int i=1;i<lineCount-1;i++){
			for(int j=1;j<lineCount;j++){
				if(inS[i][inS[i].length-1].contains(",")){
					if(inS[i][inS[i].length-1].substring(0,inS[i][inS[i].length-1].indexOf(",")).equals(inS[j][0]))outBack[i]=loc[j]+0x8000;
				}
				else if(!inS[i][inS[i].length-1].equals("RSUB") && inS[i][inS[i].length-1].equals(inS[j][0]))outBack[i]=loc[j];
			}
		}
		
		//special code(EOF,WORD,BYTE)
		for(int i=1;i<lineCount;i++){
			if(inS[i][0].equals("EOF")){
				outFront[i]=0x45;
				outBack[i]=0x4F46;
			}
			else if(inS[i].length>=3 && (inS[i][1].equals("WORD")||inS[i][2].equals("WORD"))){
				outBack[i]=Integer.parseInt(inS[i][inS[i].length-1]);
			}
			else if(inS[i].length>=3 && (inS[i][1].equals("BYTE")||inS[i][2].equals("BYTE"))){
				outBack[i]=Integer.parseInt(inS[i][inS[i].length-1].substring(2,4),16);
			}
		}
		
		//Output
		for(int i=0;i<lineCount;i++){
			if(inS[i][0].equals(".")||inS[i][0].equals(".SUBROUTINE"))continue;
			System.out.printf("%-3d ",(i+1)*5);
			if(loc[i]==0){
				System.out.printf("     ");
				for(int j=0;j<inS[i].length;j++){
					System.out.printf("%s ",inS[i][j]);
				}
				break;
			}
			System.out.printf("%x ",loc[i]);
			for(int j=0;j<inS[i].length;j++){
				System.out.printf("%s ",inS[i][j]);
			}
			if((inS[i].length>=3) && ((inS[i][1].equals("RESW")||inS[i][2].equals("RESW"))||(inS[i][1].equals("RESB")||inS[i][2].equals("RESB"))||(inS[i][1].equals("START")||inS[i][2].equals("START"))))System.out.printf("\n");
			else if(inS[i].length>=3 && !inS[i][0].equals("EOF") &&(inS[i][1].equals("BYTE")||inS[i][2].equals("BYTE")))System.out.printf("%02x\n",outBack[i]);
			else System.out.printf("%02x%04x\n",outFront[i],outBack[i]);
		}
	}
}