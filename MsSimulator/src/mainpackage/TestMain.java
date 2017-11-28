package mainpackage;

import msPattern.*;

public class TestMain {

	public static void main(String[] args) {
		
		int numElem;
		
		Element[] elem = new Element[2];
		Composition[] comp;
		MsPatternCutOff molCutAcc;
		MsPattern molInt;

		
		elem[0] = new Element("H", 1, 1.0079, 1.00);
		elem[0].setIsotope(1.007825, 0.999855);
		elem[0].setIsotope(2.014102, 0.000115);
		
		elem[1] = new Element("C", 12, 12.01, 12.00);
		elem[1].setIsotope(12.00, 0.9893);
		elem[1].setIsotope(13.003355, 0.0107);
		
		comp = Composition.allocElement("C100H202", elem);
		numElem = comp.length;
		
/*		numElem = comp.getNumElem();*/
		
		
		System.out.println("number of constituting elements:"+numElem);
		System.out.println();
		
		for(int i = 0; i<= numElem-1; i++) {
			System.out.println("Element "+i+":");
			System.out.println("Symbol:"+comp[i].getElem().getSymbol());
			System.out.println("number of atoms:"+comp[i].getNumAtom());
			System.out.println();
		}
		
		molCutAcc = new MsPatternCutOff("C100H202", elem);
		
		molCutAcc.msPeakBuildAccur();
		molCutAcc.dispMsPattern();
		
		molInt = new MsPattern("C100H202", elem);
		molInt.msPeakBuildInt();
		molInt.dispMsPattern();
		
		
	}

}
