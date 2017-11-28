package msPattern;

import static miscservice.MathService.*;


public class MsPattern {
	String formula;
	Composition[] composition;
	
	private double exactmass;
	private double molweight;
	private int numElement, numIsotopomer, idxLastIsotopomer, idxTempLastIsotopomer;
	
	Isotope[] isotopeDouble, tempIsotopeDouble;
	
	
	
	public MsPattern(String formula, Element[] arrElement) {
		this.formula = formula;
		composition = Composition.allocElement(formula, arrElement);
		numElement = composition.length;
		

	}
	
	
	public void msPeakBuildAccur() {
		
		numIsotopomer = setNumIsotopomerAccur();
		/*test*/System.out.println("NumIsotopomer: "+numIsotopomer);
		isotopeDouble = new Isotope[numIsotopomer];
		tempIsotopeDouble = new Isotope[numIsotopomer];
		for(int i = 0; i<=numIsotopomer-1; i++) {
			isotopeDouble[i] = new Isotope();
			tempIsotopeDouble[i] = new Isotope();
		}
		
		isotopeDouble[0].mass = 0.0;
		isotopeDouble[0].abundance = 1.0;
		
		idxLastIsotopomer = idxTempLastIsotopomer = 0;
		
		for(int i = 0; i <= numElement-1; i++) {
			split(composition[i]);
		}
	}
	
	public void msPeakBuildInt() {
	
		numIsotopomer = setNumIsotopomerInt();
		/*test*/System.out.println("NumIsotopomer: "+numIsotopomer);
		isotopeDouble = new Isotope[numIsotopomer];
		tempIsotopeDouble = new Isotope[numIsotopomer];
		for(int i = 0; i<=numIsotopomer-1; i++) {
			isotopeDouble[i] = new Isotope();
			tempIsotopeDouble[i] = new Isotope();
		}
		
		isotopeDouble[0].mass = 0.0;
		isotopeDouble[0].abundance = 1.0;
		
		idxLastIsotopomer = idxTempLastIsotopomer = 0;
		
		for(int i = 0; i <= numElement-1; i++) {
			split(composition[i]);
			summarize();
		}
		
	}
	
	
	
	
	
	private void split(Composition elemComposition) {
		Element element;
		int numAtom;
		int idxAtom, idxIsotope, idxIsotopomer;
		
		double tempMass, tempAbundance;
		
		element = elemComposition.getElem();
		numAtom = elemComposition.getNumAtom();
		
		
		//バッファクリア
		for(int i = 0; i <= numIsotopomer-1; i++) {
			tempIsotopeDouble[i].abundance = 0.0;
			tempIsotopeDouble[i].mass = 0.0;
		}
		idxTempLastIsotopomer = 0;
		
		
		//外側から
			//原子
			 //同位体
			  //前ステップのアイソトポマー
		//で回す
		for(idxAtom = 1; idxAtom <= numAtom; idxAtom++) {
			for(idxIsotope = 0; idxIsotope <= element.getNumIsotope()-1; idxIsotope++) {
				for(idxIsotopomer = 0; idxIsotopomer <= idxLastIsotopomer; idxIsotopomer++) {
					
					tempMass = isotopeDouble[idxIsotopomer].mass + element.getIsotope(idxIsotope).mass;
					tempAbundance = isotopeDouble[idxIsotopomer].abundance * element.getIsotope(idxIsotope).abundance;
					
					appendIsotopomer(tempMass, tempAbundance);
				}
			}
			
			//次の原子付加に備えてバッファフリア
			for(int i = 0; i <= idxTempLastIsotopomer; i++) {
				isotopeDouble[i] = tempIsotopeDouble[i].clone();
				tempIsotopeDouble[i].abundance = 0.0;
				tempIsotopeDouble[i].mass = 0.0;
			}
			
			idxLastIsotopomer = idxTempLastIsotopomer;
			idxTempLastIsotopomer = 0;
		}		
	}
		
	
	
	private void appendIsotopomer(double mass, double abundance) {
		
		int idx;
		idx = 0;
		
		while(idx <= idxTempLastIsotopomer
				&& tempIsotopeDouble[idx].mass != mass
				&& tempIsotopeDouble[idx].mass != 0.0
				&& idx < numIsotopomer - 1) {
			idx++;
		}
		
		if(tempIsotopeDouble[idx].mass == 0.0) {
			tempIsotopeDouble[idx].mass = mass;
			tempIsotopeDouble[idx].abundance = abundance; 
		}
		else {
			tempIsotopeDouble[idx].abundance += abundance; 
		}
		
		if(idx > idxTempLastIsotopomer)
			idxTempLastIsotopomer = idx;
	}
	
	
	
	private int setNumIsotopomerAccur() {
		int ans;
		int numAtom, numIsotope;
		
		ans = 1;
		for(int i = 0; i<= numElement-1; i++) {
			numAtom = composition[i].getNumAtom();
			numIsotope = composition[i].getElem().getNumIsotope();
			
			ans *= factorial(numAtom+numIsotope-1) / (factorial(numAtom) * factorial(numIsotope-1));
		}
		
		return ans;
	}
	
	
	//整数化msを求めるプロセスにおいて最悪のケースを想定して必要なバッファサイズを計算する。
	private int setNumIsotopomerInt() {
		int ans;
		int idxElem;
		
		int idxWide, idxNarrow;
		int rangeWide, rangeNarrow;
		

		idxWide = 0;
		idxNarrow = 0;
		rangeNarrow = rangeWide = composition[0].getElem().getRangeIsotopes();
		
		idxElem = 0;
		do {
			
			if(composition[idxElem].getElem().getRangeIsotopes() > rangeWide) {
				rangeWide = composition[idxElem].getElem().getRangeIsotopes();
				idxWide = idxElem;
			}
			
			if(composition[idxElem].getElem().getRangeIsotopes() < rangeNarrow) {
				rangeWide = composition[idxElem].getElem().getRangeIsotopes();
				idxNarrow = idxElem;
			}
			
			idxElem++;
			
		}
		while(idxElem <= numElement-1);
		
		ans = 1;
		for(int i=0; i<= composition.length-1; i++) {
			if(i != idxNarrow) {
				ans += (composition[i].getElem().getRangeIsotopes()) * composition[i].getNumAtom();
			}
			else {
				ans += (composition[i].getElem().getRangeIsotopes()) * (composition[i].getNumAtom() - 1);
			}	
		}
		
		ans *= composition[idxWide].getElem().getRangeIsotopes()+1;

		return ans;
		
	}
	
	
	private void summarize() {
		double intmass;
		int idxSummarize;
		
		for(int i = 0; i <= numIsotopomer-1; i++) {
			tempIsotopeDouble[i].abundance = 0.0;
			tempIsotopeDouble[i].mass = 0.0;
		}
		idxTempLastIsotopomer = 0;
		
		for(idxSummarize = 0; idxSummarize <= idxLastIsotopomer; idxSummarize++ ) {
			intmass = Math.rint(isotopeDouble[idxSummarize].mass);
			appendIsotopomer(intmass, isotopeDouble[idxSummarize].abundance);
			
			isotopeDouble[idxSummarize].mass = 0.0;
			isotopeDouble[idxSummarize].abundance = 0.0;
		}
		
		for(int i = 0; i <= idxTempLastIsotopomer; i++) {
			isotopeDouble[i] = tempIsotopeDouble[i].clone();
			tempIsotopeDouble[i].abundance = 0.0;
			tempIsotopeDouble[i].mass = 0.0;
		}
		idxLastIsotopomer = idxTempLastIsotopomer;
		idxTempLastIsotopomer = 0;
		
	}
	
	
	public void dispMsPattern() {
		int i;
		for(i = 0; i <= idxLastIsotopomer; i++) {
			System.out.println("Mass:" + isotopeDouble[i].mass+", Abund:"+ isotopeDouble[i].abundance);
		}
	}
	
}


