package chemspecies;

import java.util.*;



public class PmoSequence implements SiPmoSequence {
	
	boolean trtOn, fullProt;
	int idxNucleoside, seqCompCount;;

	String[] seqComponents;

	
	
	Molecule[] fullProtTrtOn, fullProtTrtOff, deProtTrtOn, deProtTrtOff;
	
	
	
	public PmoSequence(
			String sequenceStr,
			ArrayList<BuildingBlock> fullProtectedMonomer,
			ArrayList<BuildingBlock> deProtectedMonomer, 
			ArrayList<BuildingBlock> fullProtFirstMonomer,
			ArrayList<BuildingBlock> deProtFirstMonomer,
			ArrayList<BuildingBlock> protectiveGroup) {
		
		
		seqComponents = sequenceStr.split(delimiter);
		seqCompCount = seqComponents.length;
		
		
/*		seqCompCount = sequenceStrTok.countTokens();
		seqComponents = new String[seqCompCount];
		
		for(int i = 0; i <= seqCompCount -1; i++) {
			seqComponents[i] = sequenceStrTok.nextToken();
		}*/
		
	}
	


	
	
}


interface SiPmoSequence{
	public static final String delimiter = " ";
	
	
	public static final String TOB = "TOB";
	public static final String TRITYL = "Trt";
	public static final String HYDROGEN = "H";
	
}

