import static java.lang.System.out;
import java.util.*;


public class MainClass{
	
	public static void main(String args[]) {

		String str = "gewrhoid 234ere fjhaiser iaj  3232  adf";
		String[] arrStr;
		
		arrStr = str.split(" ");
		
		int count = arrStr.length;
		for(int i = 0; i <= count-1; i++) {
			out.println("Token "+i+": "+arrStr[i]);
		}
		
	}
	

}






