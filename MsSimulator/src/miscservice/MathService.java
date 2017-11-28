package miscservice;

public class MathService {
	public static int factorial(int n) {
		if(n >=2) {
			return n * factorial(n-1);
		}
		else
			return 1;
	}
	
	
	public static int permiation(int n, int r) {
		return (factorial(n)/factorial(n-r));
	}
	
	public static int combination(int n, int r) {
		return (factorial(n)) / (factorial(n-r) * factorial(r));
	}
}
