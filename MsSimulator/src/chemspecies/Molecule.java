package chemspecies;

import msPattern.*;


public class Molecule {
	
	private Composition[] composition;
	private int numComposingElements;
	
	private double molWeight;
	private double exactMass;
	
	private MsPattern msPattern;
	
	private Isotope[] accurMsPattern, summaryMsPattern;
	
	
	
	public Molecule(String formula, Element[] arrElement) {
		
		composition = Composition.allocElement(formula, arrElement);
		numComposingElements = composition.length;
		setMolWeight();
		
		msPattern = new MsPattern(composition);
		accurMsPattern = msPattern.getMsPattern();
		summaryMsPattern = msPattern.getSummaryMsPattern();
		msPattern = null;
		
	}
	
	
	public Molecule(Composition[] composition) {
		
		this.composition = composition;
		numComposingElements = composition.length;
		setMolWeight();
		
		msPattern = new MsPattern(composition);
		accurMsPattern = msPattern.getMsPattern();
		summaryMsPattern = msPattern.getSummaryMsPattern();
		msPattern = null;
		
		
	}
	
	
	private void setMolWeight() {
		
		molWeight = 0.0;
		exactMass = 0.0;
		
		for(int i = 0; i <= numComposingElements - 1; i++) {
			molWeight += (composition[i].getElem().getAtomicWeight())*(composition[i].getNumAtom());
			exactMass += (composition[i].getElem().getExactMass())*(composition[i].getNumAtom());
		}
	}
	
	public double getMolWeight() {
		return molWeight;
	}
	
	public double getExactMass() {
		return exactMass;
	}
	
	public Isotope[] getAccurMsPattern() {
		return accurMsPattern;
	}
	
	public Isotope[] getSummaryMsPattern() {
		return summaryMsPattern;
	}
	
	
}
