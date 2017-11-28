package msPattern;

import miscservice.*;

public class Isotope implements ModeComparable<Isotope>, Comparable<Isotope>, Cloneable {
	public double mass = 0.0;
	public double abundance = 0.0;
	
	@Override
	public int modeCompareTo(Isotope that, String mode) {
		int ans = 0;
		
		if(mode.compareTo("MASS") == 0)
		{
			if(this.mass > that.mass)
				ans = 1;
			else if(this.mass < that.mass)
				ans = -1;
			else
				ans = 0;
		}
		else if(mode.compareTo("ABUND") == 0) {
			if(this.abundance > that.abundance)
				ans = 1;
			else if(this.abundance < that.abundance)
				ans = -1;
			else
				ans = 0;
		}
		else {
			System.out.println("Isotope illegal mode");
			try{
				Thread.sleep(10000L);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return ans;
	}
	
	
	@Override
	public int compareTo(Isotope that) {
		if(this.mass > that.mass)
			return 1;
		else if(this.mass < that.mass)
			return -1;
		else
			return 0;
	}
	
	
	@Override
	public Isotope clone() {
		
		Isotope temp = null;
		
		try {
			temp = (Isotope) super.clone();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return temp;
	}


}
