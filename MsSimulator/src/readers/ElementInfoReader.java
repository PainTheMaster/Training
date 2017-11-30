package readers;

import msPattern.*;


public class ElementInfoReader implements SiElementInfoReader, SiTableReader {
	
	TableReader tabread;
	
	Element[] bufElem, ansElem;
	int idxBufElem;
	
	int currentLine;
	StringBuilder[] bufStrBuild;
	boolean[] filled;
	
	
	
	public ElementInfoReader(String tableName, int delimiter) {
		this.tabread = new TableReader(tableName, delimiter);

		
		bufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++)
			bufStrBuild[i] = new StringBuilder();
		
		filled = new boolean[NUMBER_OF_COL];
		
		bufElem = new Element[NUMBER_BUF_ELEM];
		idxBufElem = 0;
		
		currentLine = 0;
	}
	
	
	
	public Element[] setElements() {
		Element tempElem = new Element();
		int rtnUtilFunc;
		
		do {
			
			rtnUtilFunc = analBlock(tempElem);
			if(tempElem.getSymbol() != null) {
				bufElem[idxBufElem] = tempElem.clone();
				idxBufElem++;
			}
			
		}
		while(rtnUtilFunc != END_OF_FILE );
		
		ansElem = new Element[idxBufElem];
		
		for(int i = 0; i <= idxBufElem - 1; i++) {
			ansElem[i] = bufElem[i].clone();
		}
		
		return ansElem;
	}
	
	
	
	private int analBlock(Element element) {
		
		int from, to, charac;
		int rtnUtilFunc;
		int myLine;
		
		double[][] isotope = new double[MAX_NUM_ISOTOPE][2];
		
		String symbol;
		double exactMass, atomicWeight, isotopeMass, abund;
		int atomicNo;
		
		StringBuilder[] bufBufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i<= NUMBER_OF_COL-1; i++ )
			bufBufStrBuild[i] = new StringBuilder();
		
		
		myLine = 0;

		do{
						
			if(myLine == 0) {
				for(int i = 0; i <= NUMBER_OF_COL-1; i++) {
					bufBufStrBuild[i].setLength(0);
					bufBufStrBuild[i].append(bufStrBuild[i]);
				}
				rtnUtilFunc = END_OF_FILE +1;
			}
			else {
				rtnUtilFunc = tabread.getLineElemsStr(bufBufStrBuild, filled);
			}
						
			
			if(filled[0] == true) {
				
			
				if('0' <= (charac = bufBufStrBuild[0].codePointAt(0)) && charac <= '9') {
					//0列目が質量数。1列目が精密質量, 2列目がabundance
					isotopeMass = Double.parseDouble(bufBufStrBuild[COL_ISOTOPE_MASS].toString());
					abund = Double.parseDouble(bufBufStrBuild[COL_ABUND].toString());
					element.setIsotope(isotopeMass, abund);
				}
				else if(bufBufStrBuild[0].codePointAt(0) == PAREN_L) {
					from = 0+1;
					to = bufBufStrBuild[0].toString().indexOf(PAREN_R);
					
					symbol = bufBufStrBuild[0].substring(from, to); //substringはto-1のインデックスまでを切り出すことに注意
					element.setSymbol(symbol);
				}
				else if(bufBufStrBuild[0].indexOf(ATOMIC_NO) != -1) {	//indexOf はマッチがなければ-1を返すので文字列判定に使える.
					atomicNo = Integer.parseInt(bufBufStrBuild[1].toString());
					element.setAtomicNo(atomicNo);
				}
				else if(bufBufStrBuild[0].indexOf(EXACT_MASS) != -1) {
					exactMass = Double.parseDouble(bufBufStrBuild[1].toString());
					element.setAtomicExactMass(exactMass);
				}
				else if(bufBufStrBuild[0].indexOf(MASS_OF_ATOM) != -1) {
					atomicWeight = Double.parseDouble(bufBufStrBuild[1].toString());
					element.setAtomicWeight(atomicWeight);
				}
			}
			
			myLine++;
		}
		while(rtnUtilFunc != END_OF_FILE && filled[0]);
		
		return rtnUtilFunc;
	}
	
	
	
	
	private int skipBlankLine() {
		
		int idxCol;
		int numElem;
		
//		boolean[] filled = new boolean[NUMBER_OF_COL];
		boolean filledChecker = false;

		
/*		StringBuilder[] bufBufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++) {
			bufBufStrBuild[i] = new StringBuilder();
			bufBufStrBuild[i].setLength(0);
		}*/
		
		
		
		do {
			
			numElem = tabread.getLineElemsStr(bufStrBuild, filled);
			
			for(idxCol = 0; idxCol <= NUMBER_OF_COL-1; idxCol++ )
				filledChecker |= filled[idxCol];			
		}
		 while(filledChecker == false && numElem != END_OF_FILE);
		
		
/*		for(idxCol = 0; idxCol <= NUMBER_OF_COL-1; idxCol++) {
			bufStrBuild[idxCol].setLength(0);
			bufStrBuild[idxCol].append(bufBufStrBuild[idxCol]);
		}*/
		
		return numElem;
		
	}
	
	
	
	
}


interface SiElementInfoReader{
	
	public static final int NUMBER_BUF_ELEM = 118;
	
	public static final int NUMBER_OF_COL = 3;
	
	
	public static final int MAX_NUM_ISOTOPE = 100;
	
	
	public static final int PAREN_L = '[';
	public static final int PAREN_R = ']';
	
	
	public static final int COL_MASS_NO = 0;
	public static final int COL_ISOTOPE_MASS = 1;
	public static final int COL_ABUND = 2;
	
	
	public static final String ATOMIC_NO = "Atomic Number";
	public static final String MASS_OF_ATOM = "Mass of Atom";
	public static final String EXACT_MASS = "Exact Mass";
}
