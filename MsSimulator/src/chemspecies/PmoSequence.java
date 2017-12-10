package chemspecies;

import java.util.*;



public class PmoSequence implements SiPmoSequence {
	
	boolean trtOn, fullProt;
	int idxNucleoside, seqCompCount;;

	String[] strSeqComponents;

	
	
	public Molecule[] fullProtTrtOn, fullProtTrtOff, deProtTrtOn, deProtTrtOff;
	
	ArrayList<BuildingBlock> fullProtectedMonomer,
							deProtectedMonomer,
							fullProtFirstMonomer,
							deProtFirstMonomer,
							miscGroup;
	
	
	public PmoSequence(
			String sequenceStr,
			ArrayList<BuildingBlock> fullProtectedMonomer,
			ArrayList<BuildingBlock> deProtectedMonomer, 
			ArrayList<BuildingBlock> fullProtFirstMonomer,
			ArrayList<BuildingBlock> deProtFirstMonomer,
			ArrayList<BuildingBlock> miscGroup) {
		
		
		this.fullProtectedMonomer = fullProtectedMonomer;
		this.deProtectedMonomer = deProtectedMonomer;
		this.fullProtFirstMonomer = fullProtFirstMonomer;
		this.deProtFirstMonomer = deProtFirstMonomer;
		this.miscGroup = miscGroup;
		
		
		strSeqComponents = sequenceStr.split(DELIMITER);
		seqCompCount = strSeqComponents.length;	
		
		setSequence();
	}
	
	
	
	private void setSequence() {
		
		boolean miscBlock = false;
		int idxSeqComp, idxArrMolecule, numSeqComp, idxMatch;
		
		
		//まずはそもそもの鎖長を数える
		numSeqComp = 0;
		for(idxSeqComp = 0; idxSeqComp <= seqCompCount-1; idxSeqComp++) {
			
			if(match(strSeqComponents[idxSeqComp], miscGroup) != NO_MATCH) {
				if(miscBlock == false) {
					miscBlock = true;
					numSeqComp++;
				}
			}
			else {
				miscBlock = false;
				numSeqComp++;
			}				
		}
		
		fullProtTrtOn = new Molecule[numSeqComp];
		fullProtTrtOff = new Molecule[numSeqComp];
		deProtTrtOn = new Molecule[numSeqComp];
		deProtTrtOff = new Molecule[numSeqComp];
		
		//最脱体に5'-末端の水素原子をセット
		idxMatch = match(HYDROGEN, miscGroup);
		deProtTrtOff[0] = new Molecule(miscGroup.get(idxMatch).getComposition());
		deProtTrtOn[0] = new Molecule(miscGroup.get(idxMatch).getComposition());
		
		//完全保護体に5'-末端保護基をセット。多分TOB-sucなのだろう。
		fullProtTrtOn[0] = new Molecule();
		fullProtTrtOff[0] = new Molecule();
		idxSeqComp = 0;
		while((idxMatch = match(strSeqComponents[idxSeqComp],miscGroup)) != NO_MATCH) {
			fullProtTrtOn[0] = fullProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
			fullProtTrtOff[0] = fullProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
			idxSeqComp++;
		}
		
		//Trt体に3'-Trtをセット。
		idxMatch = match(TRITYL, miscGroup);
		deProtTrtOn[0] = deProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
		fullProtTrtOn[0] = fullProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
		
		//脱Trt体に3'-Hをセット
		idxMatch = match(HYDROGEN, miscGroup);
		deProtTrtOff[0] = deProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
		fullProtTrtOff[0] = fullProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
		
		
		
		
		//ここからヌクレオシドの連結開始
		
		//まずは5'末端のヌクレオシド。
		idxArrMolecule = 0;
		idxSeqComp = -1;
		do {
			idxSeqComp++;
			
			idxMatch = match(strSeqComponents[idxSeqComp], fullProtFirstMonomer);
			
			if(idxMatch != NO_MATCH) {
				fullProtTrtOn[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtFirstMonomer.get(idxMatch).getComposition());
				fullProtTrtOff[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtFirstMonomer.get(idxMatch).getComposition());
				
				fullProtTrtOn[idxArrMolecule+1].setName(fullProtTrtOn[idxArrMolecule].getName() + strSeqComponents[idxSeqComp]);
				fullProtTrtOff[idxArrMolecule+1].setName(fullProtTrtOff[idxArrMolecule].getName() + strSeqComponents[idxSeqComp]);
				
				
				int tempIdxDeprotMatch = match(fullProtFirstMonomer.get(idxMatch).getDeprotectedForm(), deProtFirstMonomer);
				deProtTrtOn[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtFirstMonomer.get(tempIdxDeprotMatch).getComposition());
				deProtTrtOff[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtectedMonomer.get(tempIdxDeprotMatch).getComposition());
				
				deProtTrtOn[idxArrMolecule+1].setName(deProtTrtOn[idxArrMolecule].getName()+fullProtFirstMonomer.get(idxMatch).getDeprotectedForm() );
				deProtTrtOff[idxArrMolecule+1].setName(deProtTrtOff[idxArrMolecule].getName()+fullProtFirstMonomer.get(idxMatch).getDeprotectedForm() );
				
				
				idxArrMolecule++;
			}
		}
		while(idxMatch == NO_MATCH && idxSeqComp <= seqCompCount-2);
		//2番目以降のヌクレオシド。
		do {
			idxSeqComp++;
			
			idxMatch = match(strSeqComponents[idxSeqComp], fullProtectedMonomer);
			
			if(idxMatch != NO_MATCH);{
				fullProtTrtOn[idxArrMolecule+1] = fullProtTrtOn[idxArrMolecule].add(fullProtectedMonomer.get(idxMatch).getComposition());
				fullProtTrtOff[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtectedMonomer.get(idxMatch).getComposition());
				
				fullProtTrtOn[idxArrMolecule+1].setName(fullProtTrtOn[idxArrMolecule].getName() + strSeqComponents[idxSeqComp]);
				fullProtTrtOff[idxArrMolecule+1].setName(fullProtTrtOff[idxArrMolecule].getName() + strSeqComponents[idxSeqComp]);
				
				
				int tempIdxDeprotMatch = match(fullProtectedMonomer.get(idxMatch).getDeprotectedForm(), deProtectedMonomer);
				deProtTrtOn[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtectedMonomer.get(tempIdxDeprotMatch).getComposition());
				deProtTrtOff[idxArrMolecule+1] = deProtTrtOff[idxArrMolecule].add(deProtectedMonomer.get(tempIdxDeprotMatch).getComposition());
				
				deProtTrtOn[idxArrMolecule+1].setName(deProtTrtOn[idxArrMolecule].getName()+fullProtectedMonomer.get(idxMatch).getDeprotectedForm() );
				deProtTrtOff[idxArrMolecule+1].setName(deProtTrtOff[idxArrMolecule].getName()+fullProtectedMonomer.get(idxMatch).getDeprotectedForm() );
				
				idxArrMolecule++;
			}
		}
		while(idxMatch != NO_MATCH && idxSeqComp <= seqCompCount-2);
		
		
	}
	
	
	int match(String str, ArrayList<BuildingBlock> arrayList) {
		for(int i = 0; i <= arrayList.size()-1; i++) {
			if(arrayList.get(i).getName().compareTo(str) == 0)
				return i;
		}
		
		return NO_MATCH;
	}


	
	
}


interface SiPmoSequence{
	public static final String DELIMITER = " ";
	
	
	public static final String TRITYL = "Trt";
	public static final String HYDROGEN = "H";
	
	public static final int NO_MATCH = -1;
	
}

