package chemspecies;

import java.util.*;



public class PmoSequence implements SiPmoSequence {
	
	boolean trtOn, fullProt;
	int idxNucleoside, seqCompCount;;

	String[] strSeqComponents;

	
	
	Molecule[] fullProtTrtOn, fullProtTrtOff, deProtTrtOn, deProtTrtOff;
	
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
		
		
		strSeqComponents = sequenceStr.split(delimiter);
		seqCompCount = strSeqComponents.length;		
	}
	
	
	
	private void setSequence() {
		
		boolean miscBlock = false;
		int idxSeqComp, idxArrMolecule, numSeqComp, idxMatch;
		
		
		//�܂��͂��������̍����𐔂���
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
		
		//�ŒE�̂�5'-���[�̐��f���q���Z�b�g
		idxMatch = match(HYDROGEN, miscGroup);
		deProtTrtOff[0] = new Molecule(miscGroup.get(idxMatch).getComposition());
		deProtTrtOn[0] = new Molecule(miscGroup.get(idxMatch).getComposition());
		
		//���S�ی�̂�5'-���[�ی����Z�b�g�B����TOB-suc�Ȃ̂��낤�B
		fullProtTrtOn[0] = new Molecule();
		fullProtTrtOff[0] = new Molecule();
		idxSeqComp = 0;
		while((idxMatch = match(strSeqComponents[idxSeqComp],miscGroup)) != NO_MATCH) {
			fullProtTrtOn[0] = fullProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
			fullProtTrtOff[0] = fullProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
			idxSeqComp++;
		}
		
		//Trt�̂�3'-Trt���Z�b�g�B
		idxMatch = match(TRITYL, miscGroup);
		deProtTrtOn[0] = deProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
		fullProtTrtOn[0] = fullProtTrtOn[0].add(miscGroup.get(idxMatch).getComposition());
		
		//�ETrt�̂�3'-H���Z�b�g
		idxMatch = match(HYDROGEN, miscGroup);
		deProtTrtOff[0] = deProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
		fullProtTrtOff[0] = fullProtTrtOff[0].add(miscGroup.get(idxMatch).getComposition());
		
		
		
		
		//��������k�N���I�V�h�̘A���J�n
		
		//�܂���5'���[�̃k�N���I�V�h�B
		idxArrMolecule = 0;
		idxSeqComp = -1;
		do {
			idxSeqComp++;
			
			idxMatch = match(strSeqComponents[idxSeqComp], fullProtFirstMonomer);
			
			if(idxMatch != NO_MATCH) {
				fullProtTrtOn[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtFirstMonomer.get(idxMatch).getComposition());
				fullProtTrtOff[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtFirstMonomer.get(idxMatch).getComposition());
				
				int tempIdxDeprotMatch = match(fullProtFirstMonomer.get(idxMatch).getDeprotectedForm(), deProtFirstMonomer);
				deProtTrtOn[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtFirstMonomer.get(tempIdxDeprotMatch).getComposition());
				deProtTrtOff[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtectedMonomer.get(tempIdxDeprotMatch).getComposition());
				
				idxArrMolecule++;
			}
		}
		while(idxMatch == NO_MATCH);
		//2�Ԗڈȍ~�̃k�N���I�V�h�B
		do {
			idxSeqComp++;
			
			idxMatch = match(strSeqComponents[idxSeqComp], fullProtectedMonomer);
			
			if(idxMatch != NO_MATCH);{
				fullProtTrtOn[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtectedMonomer.get(idxMatch).getComposition());
				fullProtTrtOff[idxArrMolecule+1] = fullProtTrtOff[idxArrMolecule].add(fullProtectedMonomer.get(idxMatch).getComposition());
				
				int tempIdxDeprotMatch = match(fullProtFirstMonomer.get(idxMatch).getDeprotectedForm(), deProtectedMonomer);
				deProtTrtOn[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtFirstMonomer.get(tempIdxDeprotMatch).getComposition());
				deProtTrtOff[idxArrMolecule+1] = deProtTrtOn[idxArrMolecule].add(deProtectedMonomer.get(tempIdxDeprotMatch).getComposition());
				
				idxArrMolecule++;
			}
		}
		while(idxMatch != NO_MATCH);
		
		
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
	public static final String delimiter = " ";
	
	
	public static final String TRITYL = "Trt";
	public static final String HYDROGEN = "H";
	
	public static final int NO_MATCH = -1;
	
}

