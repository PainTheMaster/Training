package chemspecies;

public class Composition implements Cloneable {

 	private Element constituentElement;
	private int numAtom;
	
	public Composition() {
		;
	}
		
	
	public static Composition[] allocElement(String formula, Element[] arrElement) {
		
		int lenArrElement, numElem;
		String symbolTemp;
		MolecularFormula compformul;
		SymbolNumRecord[] symbolnumrec;
		Composition[] compTemp;
		
		
		compformul = new MolecularFormula(formula);
		symbolnumrec = compformul.formulaAnal();
		numElem = symbolnumrec.length;
		compTemp = new Composition[numElem];
		
		
		for(int i=0; i<= numElem -1; i++) {
			
			symbolTemp = symbolnumrec[i].symbol;
			
			lenArrElement = arrElement.length;
			int j=0;
			while(j<lenArrElement-1	&&	symbolTemp.compareTo(arrElement[j].getSymbol()) != 0)
				j++;
			
			compTemp[i] = new Composition();
			compTemp[i].constituentElement = arrElement[j];
			compTemp[i].numAtom = symbolnumrec[i].num;

		}
		return compTemp;
	}
	
	
	public void setElement(Element constituentElement) {
		this.constituentElement = constituentElement;
	}
	
	public void setNumAtom(int numAtom) {
		this.numAtom = numAtom;
	}
	
	
	public Element getElement() {
		return constituentElement;
	}
	
	public int getNumAtom() {
		return numAtom;
	}
	
	@Override
	public Composition clone() {
		Composition temp = null;
		
		try {
			temp = (Composition) super.clone();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		temp.constituentElement = this.constituentElement;
		
		return temp;
	}
	
	@Override
	public String toString() {
		StringBuilder tempSb = new StringBuilder();
		tempSb.append(constituentElement.getSymbol());
		tempSb.append(numAtom);
		return tempSb.toString();
	}
	
	
}


class MolecularFormula implements SiCompFormula{
	private String formula;
	private int idxSymbol, idxFrom, idxTo;
	
	
	public MolecularFormula(String formula) {
		this.formula = formula;
		idxFrom = idxTo = 0;		
		idxSymbol = 0;
	}
	
	
	
	public SymbolNumRecord[] formulaAnal() {

		SymbolNumRecord[] ans;
		SymbolNumRecord[] tempSymbolNum = new SymbolNumRecord[MAX_BUF_LEN];
		
		while(idxTo < formula.length()-1) {
			tempSymbolNum[idxSymbol] = new SymbolNumRecord(); 
			
			tempSymbolNum[idxSymbol].symbol = acquSymbol();
			
			if(idxTo < formula.length()-1)
				tempSymbolNum[idxSymbol].num = acquNum();
			else
				tempSymbolNum[idxSymbol].num = 1;

			idxSymbol++;
		}
		
		ans = new SymbolNumRecord[idxSymbol];
		for(int i = 0; i<= idxSymbol-1; i++) {
			ans[i] = new SymbolNumRecord();
			ans[i] = tempSymbolNum[i].clone();
		}
		
		return ans;
	}
	
	
	
	private String acquSymbol() {
		String temp = null;

		if(charAnal(formula.charAt(idxFrom)) == UPPERCASE) {
			idxTo = idxFrom+1;
			while(idxTo <= formula.length()-1	&&	charAnal(formula.charAt(idxTo)) == LOWERCASE)
				idxTo++;
			
			idxTo--;
			temp = formula.substring(idxFrom, idxTo+1);
			
			idxFrom = idxTo + 1;
		}
		
		return temp;
	}
	
	
	
	private int acquNum() {
		int ans = NOT_NUMBER;
		String temp;
		
		if(charAnal(formula.charAt(idxFrom)) == NUMBER) {
			idxTo = idxFrom+1;
			while(idxTo <= formula.length()-1	&&	charAnal(formula.charAt(idxTo)) == NUMBER)
				idxTo++;
			
			idxTo--;
			temp=formula.substring(idxFrom, idxTo+1);
			ans = Integer.parseInt(temp);
			
			idxFrom = idxTo + 1;
		}
		else if(charAnal(formula.charAt(idxFrom)) == UPPERCASE) {
			ans = 1;
			idxTo = idxFrom-1;
		}
		
		return ans;
			
	}
	
	private int charAnal(char ch) {
		int ans = OTHERS;
		
		if('A'<=ch && ch<='Z')
			ans = UPPERCASE;		
		else if ('a'<=ch && ch<='z')
			ans = LOWERCASE;
		else if('0'<=ch && ch <= '9')
			ans = NUMBER;
		
		return ans;
	}	
	
}



class SymbolNumRecord implements Cloneable{
	String symbol;
	int num;
	
	@Override
	public SymbolNumRecord clone() {
		SymbolNumRecord temp = null;
		
		try {
			temp = (SymbolNumRecord) super.clone();
		}
		catch(Exception e) {
			;
		}
		return temp;
	}
}



interface SiCompFormula{
	static public final int OTHERS = -1;
	static public final int UPPERCASE = 0;
	static public final int  LOWERCASE = 1;
	static public final int NUMBER = 2;
	
	static public final int NOT_NUMBER = -1;
	
	static public final int MAX_BUF_LEN = 99;
}
