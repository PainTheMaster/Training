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
	
	
	public Molecule() {

		numComposingElements = 0;
		setMolWeight();
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
						
			while( bufComposition[idxMatchCheck].getElement().getAtomicNo() 
					!= that[i].getElement().getAtomicNo()
					&& idxMatchCheck <= idxLastBufComp) {
				
				idxMatchCheck++;
			}
			
			
			if(idxMatchCheck > idxLastBufComp) {
				idxLastBufComp = idxMatchCheck;
				
				bufComposition[idxLastBufComp] = new Composition();
				bufComposition[idxLastBufComp].setElement(that[i].getElement());
				bufComposition[idxLastBufComp].setNumAtom(that[i].getNumAtom());
			}
			else {
				int sumAtomNo = bufComposition[idxLastBufComp].getNumAtom() + that[i].getNumAtom();
				bufComposition[idxLastBufComp].setNumAtom(sumAtomNo);
			}
		}
			
		
		poseComposition = new Composition[idxLastBufComp+1];
		for(int i = 0; i <= idxLastBufComp; i++) {
			poseComposition[i] = bufComposition[i];
		}
		
		
		return new Molecule(poseComposition);

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
	
	
}
