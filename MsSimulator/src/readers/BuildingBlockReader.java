package readers;


import chemspecies.*;
import java.util.*;


public class BuildingBlockReader implements SiBuildingBlockReader, SiTableReader {
	
	TableReader tabread;
	
	Element[] arrElement;
	int idxBufElem;
	
	public BuildingBlockReader(String tableName, int delimiter, Element[] arrElement) {
		this.tabread = new TableReader(tableName, delimiter);
		
		this.arrElement = arrElement;
	}
	
	
	public void setBuildingBlocks(ArrayList<BuildingBlock> fullProtected,
			ArrayList<BuildingBlock> deProtected, 
			ArrayList<BuildingBlock> fullProtFirstMonomer,
			ArrayList<BuildingBlock> deProtFirstMonomer,
			ArrayList<BuildingBlock> protectiveGroup) {
		int rtnUtilFunc;
		BuildingBlock tempBuilingBlock = new BuildingBlock();
		BuildingBlock[] arrTempFullProt, arrTempDeProt, arrTempProtGroup;
			arrTempFullProt = new BuildingBlock[ARR_TEMP_COMP_LEN];
			arrTempDeProt = new BuildingBlock[ARR_TEMP_COMP_LEN];
			arrTempProtGroup = new BuildingBlock[ARR_TEMP_COMP_LEN];
		int idxArrTempFullProt, idxArrTempDeProt, idxArrTempProtGroup;
		
		
		idxArrTempFullProt = -1;
		idxArrTempDeProt = -1;
		idxArrTempProtGroup = -1;
		
		rtnUtilFunc = END_OF_FILE + 1;
		do{
			rtnUtilFunc = analBlock(tempBuilingBlock);
			
			if(tempBuilingBlock.getKind() == MONOMER_FULLPROT) {
				idxArrTempFullProt++;
				arrTempFullProt[idxArrTempFullProt] = tempBuilingBlock.clone();
			}
			else if(tempBuilingBlock.getKind() == MONOMER_DEPROT) {
				idxArrTempDeProt++;
				arrTempDeProt[idxArrTempDeProt] = tempBuilingBlock.clone();
			}
			else if(tempBuilingBlock.getKind() == PROTGR) {
				idxArrTempProtGroup++;
				arrTempProtGroup[idxArrTempProtGroup] = tempBuilingBlock.clone();
			}
		}
		while(rtnUtilFunc != END_OF_FILE);
		
		for(int i = 0; i <= idxArrTempFullProt; i++)
			fullProtected.add(arrTempFullProt[i].clone());
		
		for(int i = 0; i <= idxArrTempDeProt; i++)
			deProtected.add(arrTempDeProt[i].clone());
		
		for(int i = 0; i <= idxArrTempProtGroup; i++)
			protectiveGroup.add(arrTempProtGroup[i].clone());
		
	
	}
	
	
	
	//最初に空白行を読み飛ばした後、ブロックを解析しElementオブジェクトにデータを入れて返すメソッド.
	//ブロックを読んだ後、空白行に当たったら、その行を読んで終わる。
	//通常はTableReader.getLineElemStrの戻り値をそのまま返す。ファイル終端に達した場合END_OF_FILEを返す。
	private int analBlock(BuildingBlock buildingblock) {
		
		int from, to;
		int rtnUtilFunc;
		int lineInBlock;
		
		
		String name;		//説明変数
		
		ArrayList<Composition> bufListComposition = new ArrayList<Composition>();
		
		StringBuilder[] bufStrBuild;
				bufStrBuild = new StringBuilder[NUMBER_OF_COL];
				for(int i = 0; i <= NUMBER_OF_COL -1; i++)
					bufStrBuild[i] = new StringBuilder();
		
		boolean[] filled;
		filled = new boolean[NUMBER_OF_COL];
		
	
		//空白行を飛ばす。内容のある行は第0要素が必ずあるのでfilled[0]を照会すれば空白行か否か分かる。
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
			
				if(bufStrBuild[0].codePointAt(0) == PAREN_L) {			//[]で囲まれたビルディングブロック名の場合。
					from = 0+1;
					to = bufStrBuild[0].toString().indexOf(PAREN_R);
					
					name = bufStrBuild[0].substring(from, to); //substringはto-1のインデックスまでを切り出すことに注意
					
					buildingblock.setName(name);
				}
				else if(bufStrBuild[0].toString().compareTo(PLATFORM) == 0) {
					;
				}
				else if(bufStrBuild[0].toString().compareTo(CLASS) == 0) {
					if(bufStrBuild[1].toString().compareTo(STR_FULLPROTECTED) == 0) {
						buildingblock.setKind(MONOMER_FULLPROT);
					}
					else if(bufStrBuild[1].toString().compareTo(STR_DEPROTECTED) == 0)
						buildingblock.setKind(MONOMER_DEPROT);
					else if(bufStrBuild[1].toString().compareTo(STR_FULLPROTECTED_1ST) == 0)
						buildingblock.setKind(FULLPROT_1ST);
					else if(bufStrBuild[1].toString().compareTo(STR_DEPROTECTED_1ST) == 0)
						buildingblock.setKind(DEPROT_1ST);
					else if(bufStrBuild[1].toString().compareTo(STR_PROTECTING_G) == 0)
						buildingblock.setKind(PROTGR);
				}
				else if(bufStrBuild[0].toString().compareTo(COMPOSITION) == 0) {
					rtnUtilFunc = analComposition(bufListComposition);
					buildingblock.setComposition(bufListComposition);
				}
			}
			
			lineInBlock++;
		}
		while(rtnUtilFunc != END_OF_FILE && filled[0] == true);
		
		return rtnUtilFunc;
	}
	
	
	
	

	private int analComposition(ArrayList<Composition> toBeSet) {
		
		int rtnUtilFunc, idxComposition;
		int elementMatch;
		
		Composition[] arrTempComp;
			arrTempComp = new Composition[ARR_TEMP_COMP_LEN];
			for(int i = 0; i <= ARR_TEMP_COMP_LEN-1; i++)
				arrTempComp[i] = new Composition();
		
		StringBuilder[] bufStrBuild;
			bufStrBuild = new StringBuilder[NUMBER_OF_COL];
			for(int i = 0; i <= NUMBER_OF_COL -1; i++)
				bufStrBuild[i] = new StringBuilder();
		
		boolean[] filled = new boolean[NUMBER_OF_COL];
		
		
		idxComposition = -1;
		do{
			rtnUtilFunc = tabread.getLineElemsStr(bufStrBuild, filled);
			
			if(filled[0] == true) {
				
				elementMatch=0;
				while(arrElement[elementMatch].getSymbol().compareTo(bufStrBuild[0].toString()) != 0 )
					elementMatch++;
				
				idxComposition++;
				arrTempComp[idxComposition].setElement(arrElement[elementMatch]);
				arrTempComp[idxComposition].setNumAtom(Integer.parseInt(bufStrBuild[1].toString() ) );
			}
		}
		while(rtnUtilFunc != END_OF_FILE && filled[0]);
		

		
		for(int i = 0; i <= idxComposition; i++) {
			toBeSet.add(arrTempComp[i].clone());
		}
		
		return rtnUtilFunc;
	}
	
	
}





interface SiBuildingBlockReader{
	
	public static final int NUMBER_OF_COL = 3;
	
	public static final int ARR_TEMP_COMP_LEN = 20;
	
	
	public static final int MONOMER_FULLPROT=0;
	public static final int MONOMER_DEPROT=1;
	public static final int FULLPROT_1ST=2;
	public static final int DEPROT_1ST=3;
	public static final int PROTGR=4;
	
	
	
	public static final String PLATFORM = "Platform";
	public static final String CLASS = "Class";
		public static final String STR_FULLPROTECTED = "fullprotected";
		public static final String STR_DEPROTECTED = "deprotected";
		public static final String STR_FULLPROTECTED_1ST = "fullprotected 1st nucleoside";
		public static final String STR_DEPROTECTED_1ST= "deprotected 1st nucleoside";
		public static final String STR_PROTECTING_G = "protecting group";
	public static final String COMPOSITION = "<Composition>";
	public static final int PAREN_L = '[';
	public static final int PAREN_R = ']';	

}
