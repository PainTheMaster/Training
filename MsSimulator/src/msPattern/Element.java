package msPattern;

import miscservice.HeapSort;

public class Element {
	
	private String symbol;
	private int atomicNo;
	
	private double atomicWeight;
	private double atomicExactMass;
	
	private Isotope[] isotopes;
	
	private int numIsotope;
	private int rangeIsotope;
	
	
	
	
	public Element(String symbol, int atomicNo, double atomicWeight, double atomicExactMass) {
		this.symbol = symbol;
		this.atomicNo = atomicNo;
		this.atomicWeight = atomicWeight;
		this.atomicExactMass = atomicExactMass;
		numIsotope = 0;
	}
	
	
	public void setIsotope(double mass, double abundance) {
		Isotope[] temp;
		
		numIsotope++;
		temp = new Isotope[numIsotope];
		
		for(int i=0; i<=numIsotope-1-1; i++) {
			temp[i] = new Isotope();
			temp[i] = isotopes[i].clone();
		}
		
		temp[numIsotope-1] = new Isotope();
		temp[numIsotope-1].mass = mass;
		temp[numIsotope-1].abundance = abundance;
		
		isotopes = temp;
		
		HeapSort.heapSort(isotopes, 0, numIsotope-1);
		
		rangeIsotope = (int) Math.round(isotopes[numIsotope-1].mass-isotopes[0].mass);
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public int getAtomicNo() {
		return atomicNo;
	}
	
	public double getAtomicWeight() {
		return atomicWeight;
	}
	
	public double getExactMass() {
		return atomicExactMass;
	}
	
	public int getNumIsotope() {
		return numIsotope;
	}
	
	public Isotope getIsotope(int idx) {
		if(idx > numIsotope-1)
			return null;
		else
			return isotopes[idx];
	}
	
	public int getRangeIsotopes() {
		return rangeIsotope;
	}
	
	
	
}
