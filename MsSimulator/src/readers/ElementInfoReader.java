package readers;

import msPattern.*;


public class ElementInfoReader implements SiElementInfoReader, SiTableReader {
	
	TableReader tabread;
	
	Element[] bufElem, ansElem;
	int idxBufElem;
	
	int currentLine;
//	StringBuilder[] bufStrBuild;
//	boolean[] filled;
	
	
	
	public ElementInfoReader(String tableName, int delimiter) {
		this.tabread = new TableReader(tableName, delimiter);

		
/*		bufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++)
			bufStrBuild[i] = new StringBuilder();
		
		filled = new boolean[NUMBER_OF_COL]; */
		
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
		//このwhileループを抜けた時点で、idxBufElemはデータが入っているところの次のインデックスを指していることに注意（そして、それは読み込んだ元素数に等しい）
		
		idxBufElem--;
		
		ansElem = new Element[idxBufElem+1];
		
		for(int i = 0; i <= idxBufElem; i++) {
			ansElem[i] = bufElem[i].clone();
		}
		
		return ansElem;
	}
	
	
	//最初に空白行を読み飛ばした後、ブロックを解析しElementオブジェクトにデータを入れて返すメソッド.
	//ブロックを読んだ後、空白行に当たったら、その行を読んで終わる。
	//通常はTableReader.getLineElemStrの戻り値をそのまま返す。ファイル終端に達した場合END_OF_FILEを返す。
	private int analBlock(Element element) {
		
		int from, to, charac;
		int rtnUtilFunc;
		int lineInBlock;
		
//		double[][] isotope = new double[MAX_NUM_ISOTOPE][2];
		
		String symbol;											//説明変数
		double exactMass, atomicWeight, isotopeMass, abund;		//説明変数
		int atomicNo;											//説明変数
		
		StringBuilder[] bufStrBuild;
				bufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++)
			bufStrBuild[i] = new StringBuilder();
		
		boolean[] filled;
		filled = new boolean[NUMBER_OF_COL];
	
		//空白行を飛ばす。内容のある行は第0要素が必ずあるのでfilled[0]を紹介すれば空白行か否か分かる。
		do {
			rtnUtilFunc = tabread.getLineElemsStr(bufStrBuild, filled);
		}
		while(filled[0] == false && rtnUtilFunc != END_OF_FILE);
		
		if(rtnUtilFunc == END_OF_FILE)
			return END_OF_FILE;
		
		lineInBlock = 0;
		do{
			if (lineInBlock != 0)
				rtnUtilFunc = tabread.getLineElemsStr(bufStrBuild, filled);
						
			
			if(filled[0] == true) {
			
				if('0' <= (charac = bufStrBuild[0].codePointAt(0)) && charac <= '9') {		//アイソトープだった場合。
					//0列目が質量数。1列目が精密質量, 2列目がabundance
					isotopeMass = Double.parseDouble(bufStrBuild[COL_ISOTOPE_MASS].toString());
					abund = Double.parseDouble(bufStrBuild[COL_ABUND].toString()) / 100.0;
					element.setIsotope(isotopeMass, abund);
				}
				else if(bufStrBuild[0].codePointAt(0) == PAREN_L) {			//[]で囲まれた元素記号の場合。
					from = 0+1;
					to = bufStrBuild[0].toString().indexOf(PAREN_R);
					
					symbol = bufStrBuild[0].substring(from, to); //substringはto-1のインデックスまでを切り出すことに注意
					element.setSymbol(symbol);
				}
				else if(bufStrBuild[0].indexOf(ATOMIC_NO) != -1) {	//indexOf はマッチがなければ-1を返すので文字列含有判定に使える.
					atomicNo = Integer.parseInt(bufStrBuild[1].toString());
					element.setAtomicNo(atomicNo);
				}
				else if(bufStrBuild[0].indexOf(EXACT_MASS) != -1) {
					exactMass = Double.parseDouble(bufStrBuild[1].toString());
					element.setAtomicExactMass(exactMass);
				}
				else if(bufStrBuild[0].indexOf(ATOMIC_WEIGHT) != -1) {
					atomicWeight = Double.parseDouble(bufStrBuild[1].toString());
					element.setAtomicWeight(atomicWeight);
				}
			}
			
			lineInBlock++;
		}
		while(rtnUtilFunc != END_OF_FILE && filled[0]);
		
		return rtnUtilFunc;
	}
	
	
	
//このメソッドは廃止する。
/*	private int skipBlankLine() {
		
		int idxCol;
		int numElem;
		
//		boolean[] filled = new boolean[NUMBER_OF_COL];
		boolean filledChecker = false;

		
		StringBuilder[] bufBufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++) {
			bufBufStrBuild[i] = new StringBuilder();
			bufBufStrBuild[i].setLength(0);
		}
		
		
		
		do {
			
			numElem = tabread.getLineElemsStr(bufStrBuild, filled);
			
			for(idxCol = 0; idxCol <= NUMBER_OF_COL-1; idxCol++ )
				filledChecker |= filled[idxCol];			
		}
		 while(filledChecker == false && numElem != END_OF_FILE);
		
		
/		for(idxCol = 0; idxCol <= NUMBER_OF_COL-1; idxCol++) {
			bufStrBuild[idxCol].setLength(0);
			bufStrBuild[idxCol].append(bufBufStrBuild[idxCol]);
		}
		
		return numElem;
		
	}*/
	
	
	
	
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
	public static final String ATOMIC_WEIGHT = "Atomic Weight";
	public static final String EXACT_MASS = "Exact Mass";
}
