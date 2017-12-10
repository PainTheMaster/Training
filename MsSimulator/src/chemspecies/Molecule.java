package chemspecies;

import msPattern.*;


public class Molecule {
	private String name;
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
	
	
	public Molecule() {

		numComposingElements = 0;
		molWeight = 0.0;
		exactMass = 0.0;
	}
	
	
	private void setMolWeight() {
		
		molWeight = 0.0;
		exactMass = 0.0;
		
		for(int i = 0; i <= numComposingElements - 1; i++) {
			molWeight += (composition[i].getElement().getAtomicWeight())*(composition[i].getNumAtom());
			exactMass += (composition[i].getElement().getExactMass())*(composition[i].getNumAtom());
		}
	}
	
	
	public Molecule add(Molecule that) {
		int maxLengthBuf, thatNumCompElem;
		int idxLastBufComp, idxMatchCheck;
		Composition[] bufComposition, poseComposition;
		
		thatNumCompElem = that.composition.length;
		maxLengthBuf =  numComposingElements + thatNumCompElem;
		
		bufComposition = new Composition[maxLengthBuf];
		
		for(int i = 0; i <= numComposingElements -1; i++) {
			bufComposition[i] = new Composition();
			bufComposition[i].setElement(this.composition[i].getElement());
			bufComposition[i].setNumAtom(this.composition[i].getNumAtom());
		}
		idxLastBufComp = numComposingElements-1;
		
		
		for(int i = 0; i <= thatNumCompElem-1; i++) {
			
			idxMatchCheck = 0;
						
			while( bufComposition[idxMatchCheck].getElement().getAtomicNo() 
					!= that.composition[i].getElement().getAtomicNo()
					&& idxMatchCheck <= idxLastBufComp) {
				
				idxMatchCheck++;
			}
			
			
			if(idxMatchCheck > idxLastBufComp) {
				idxLastBufComp = idxMatchCheck;
				
				bufComposition[idxLastBufComp] = new Composition();
				bufComposition[idxLastBufComp].setElement(that.composition[i].getElement());
				bufComposition[idxLastBufComp].setNumAtom(that.composition[i].getNumAtom());
			}
			else {
				int sumAtomNo = bufComposition[idxLastBufComp].getNumAtom() + that.composition[i].getNumAtom();
				bufComposition[idxLastBufComp].setNumAtom(sumAtomNo);
			}
		}
			
		
		poseComposition = new Composition[idxLastBufComp+1];
		for(int i = 0; i <= idxLastBufComp; i++) {
			poseComposition[i] = bufComposition[i];
		}
		
		
		return new Molecule(poseComposition);

	}
	
	
	public Molecule add(Composition[] that) {
		int maxLengthBuf, thatNumCompElem;
		int idxLastBufComp, idxMatchCheck;
		Composition[] bufComposition, poseComposition;
		
		thatNumCompElem = that.length;
		maxLengthBuf =  numComposingElements + thatNumCompElem;
		
		bufComposition = new Composition[maxLengthBuf];
		
		for(int i = 0; i <= numComposingElements -1; i++) {
			bufComposition[i] = new Composition();
			bufComposition[i].setElement(this.composition[i].getElement());
			bufComposition[i].setNumAtom(this.composition[i].getNumAtom());
		}
		idxLastBufComp = numComposingElements-1;
		
		
		for(int i = 0; i <= thatNumCompElem-1; i++) {
			
			idxMatchCheck = 0;
						
			while( idxMatchCheck <= idxLastBufComp
					&& bufComposition[idxMatchCheck].getElement().getAtomicNo() != that[i].getElement().getAtomicNo() )
			{
				
				idxMatchCheck++;
			}
			
			
			if(idxMatchCheck > idxLastBufComp) {
				idxLastBufComp++;
				
				bufComposition[idxLastBufComp] = new Composition();
				bufComposition[idxLastBufComp].setElement(that[i].getElement());
				bufComposition[idxLastBufComp].setNumAtom(that[i].getNumAtom());
			}
			else {
				int sumAtomNo = bufComposition[idxMatchCheck].getNumAtom() + that[i].getNumAtom();
				bufComposition[idxMatchCheck].setNumAtom(sumAtomNo);
			}
		}
			
		
		poseComposition = new Composition[idxLastBufComp+1];
		for(int i = 0; i <= idxLastBufComp; i++) {
			poseComposition[i] = bufComposition[i].clone();
		}
		
		
		return new Molecule(poseComposition);

	}
	
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		
		if (name == null) {
			return "";
		}
		else {
			return name;
		}
	}
	
	
	public Composition[] getComposition() {
		return composition;
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
	
	
	@Override
	public String toString() {
		
		StringBuilder tempSb = new StringBuilder();
		
		if(name != null) {
			tempSb.append(name+"\n");
		}
		
		for(int i = 0; i <= numComposingElements-1; i++) {
			tempSb.append(composition[i].toString());
		}
		
		tempSb.append("\n");
		
		tempSb.append("EM="+exactMass+", MW="+molWeight+"\n");
		
		return tempSb.toString();
		
	}
	
	
}
