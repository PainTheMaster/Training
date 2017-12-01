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
		//����while���[�v�𔲂������_�ŁAidxBufElem�̓f�[�^�������Ă���Ƃ���̎��̃C���f�b�N�X���w���Ă��邱�Ƃɒ��Ӂi�����āA����͓ǂݍ��񂾌��f���ɓ������j
		
		idxBufElem--;
		
		ansElem = new Element[idxBufElem+1];
		
		for(int i = 0; i <= idxBufElem; i++) {
			ansElem[i] = bufElem[i].clone();
		}
		
		return ansElem;
	}
	
	
	//�ŏ��ɋ󔒍s��ǂݔ�΂�����A�u���b�N����͂�Element�I�u�W�F�N�g�Ƀf�[�^�����ĕԂ����\�b�h.
	//�u���b�N��ǂ񂾌�A�󔒍s�ɓ���������A���̍s��ǂ�ŏI���B
	//�ʏ��TableReader.getLineElemStr�̖߂�l�����̂܂ܕԂ��B�t�@�C���I�[�ɒB�����ꍇEND_OF_FILE��Ԃ��B
	private int analBlock(Element element) {
		
		int from, to, charac;
		int rtnUtilFunc;
		int lineInBlock;
		
//		double[][] isotope = new double[MAX_NUM_ISOTOPE][2];
		
		String symbol;											//�����ϐ�
		double exactMass, atomicWeight, isotopeMass, abund;		//�����ϐ�
		int atomicNo;											//�����ϐ�
		
		StringBuilder[] bufStrBuild;
				bufStrBuild = new StringBuilder[NUMBER_OF_COL];
		for(int i = 0; i <= NUMBER_OF_COL -1; i++)
			bufStrBuild[i] = new StringBuilder();
		
		boolean[] filled;
		filled = new boolean[NUMBER_OF_COL];
	
		//�󔒍s���΂��B���e�̂���s�͑�0�v�f���K������̂�filled[0]���Љ��΋󔒍s���ۂ�������B
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
			
				if('0' <= (charac = bufStrBuild[0].codePointAt(0)) && charac <= '9') {		//�A�C�\�g�[�v�������ꍇ�B
					//0��ڂ����ʐ��B1��ڂ���������, 2��ڂ�abundance
					isotopeMass = Double.parseDouble(bufStrBuild[COL_ISOTOPE_MASS].toString());
					abund = Double.parseDouble(bufStrBuild[COL_ABUND].toString()) / 100.0;
					element.setIsotope(isotopeMass, abund);
				}
				else if(bufStrBuild[0].codePointAt(0) == PAREN_L) {			//[]�ň͂܂ꂽ���f�L���̏ꍇ�B
					from = 0+1;
					to = bufStrBuild[0].toString().indexOf(PAREN_R);
					
					symbol = bufStrBuild[0].substring(from, to); //substring��to-1�̃C���f�b�N�X�܂ł�؂�o�����Ƃɒ���
					element.setSymbol(symbol);
				}
				else if(bufStrBuild[0].indexOf(ATOMIC_NO) != -1) {	//indexOf �̓}�b�`���Ȃ����-1��Ԃ��̂ŕ�����ܗL����Ɏg����.
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
	
	
	
//���̃��\�b�h�͔p�~����B
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
