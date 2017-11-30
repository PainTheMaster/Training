import static java.lang.System.out;


public class MainClass{
	
	public static void main(String args[]) {

		StringBuffer sb;
		
		sb = new StringBuffer("test sb");
		out.println(sb);
		
		testfunc(sb);
		
		out.println(sb);
		//this is a comment for test.
	}
	
	
	private static void testfunc(StringBuffer strbuf) {
		strbuf.append(" appended by testfunc");
	}
	
}






