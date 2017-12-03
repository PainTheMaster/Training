package msPattern;

import chemspecies.Composition;
import chemspecies.Element;
import chemspecies.Isotope;
import miscservice.*;


public class MsPattern implements SiMsPatternCutOff {

	private Composition[] composition;
	

	private int	idxLastIsotopomer, idxTempLastIsotopomer, idxLastSummaryIsotopomer; //�ǂ��܂Łi�Ӗ��̂���j�f�[�^���[�U����Ă��邩�B��������̓S�~
	private int numElement;
	
	Isotope[] isotopomer, tempIsotopomer, summaryIsotopomer;
	
	
	
	public MsPattern(String formula, Element[] arrElement) {

		composition = Composition.allocElement(formula, arrElement);
		numElement = composition.length;
		
		isotopomer = new Isotope[MAX_ISOTOPOMER];
		tempIsotopomer = new Isotope[MAX_ISOTOPOMER];

		for(int i = 0; i<=MAX_ISOTOPOMER-1; i++) {
			isotopomer[i] = new Isotope();
			tempIsotopomer[i] = new Isotope();
		}
		
		msPeakBuildAccur();
		summarize();
	}
	
	
	public MsPattern(Composition[] composition) {

		this.composition = composition;
		numElement = composition.length;
		
		isotopomer = new Isotope[MAX_ISOTOPOMER];
		tempIsotopomer = new Isotope[MAX_ISOTOPOMER];

		for(int i = 0; i<=MAX_ISOTOPOMER-1; i++) {
			isotopomer[i] = new Isotope();
			tempIsotopomer[i] = new Isotope();
		}
		
		msPeakBuildAccur();
		summarize();
	}
	
	
	
	
	
	//�}�C�i�[�s�[�N�̃J�b�g�I�t���s���Ȃ���Acomopsotion�z������Ƀs�[�N�\�z���s���B
	//���ʂ�isotopmer�z��Ɋi�[�����B
	public void msPeakBuildAccur() {
		
		double sumAbundance;
		
		//����0��100%�B�܂艽���Ȃ���Ԃ���X�^�[�g�B
		isotopomer[0].mass = 0.0;
		isotopomer[0].abundance = 1.0;
		idxLastIsotopomer = 0;
		
		//�o�b�t�@�N���A
		idxTempLastIsotopomer = -1;		
		
		
		for(int i = 0; i <= numElement-1; i++) {
			
			if(i <= numElement-1-1) {
				split(composition[i], composition[i+1].getElement().getNumIsotope()/*����*/);
			}
			else {
				split(composition[i], NO_FOLLOWING_ELEMENT);
			}
		}
		
		sumAbundance = 0.0;
		for(int i = 0; i <= idxLastIsotopomer; i++) {
			sumAbundance += isotopomer[i].abundance;
		}
		
		for(int i = 0; i <= idxLastIsotopomer; i++) {
			isotopomer[i].abundance /= sumAbundance;
		}
		
		HeapSortMode.heapSortMode(isotopomer, 0, idxLastIsotopomer, "MASS", "AZ");
		
	}
	

	
	
	//�^����ꂽComposition�C���X�^���X�ɉ����āA���f���ƂɃs�[�N�̃X�v���b�g���s���B
	//���ʂ�isotopmer�z��Ɋi�[�����B
	private void split(Composition elemComposition, int numIsotopeFollowElem) {
		
		Element element;
		int numAtom;
		int idxAtom, idxIsotope, idxIsotopomer;
		
		int numMaxHeldPeak;
		
		double tempMass, tempAbundance;
		
		element = elemComposition.getElement();
		numAtom = elemComposition.getNumAtom();
		
		//�o�b�t�@�N���A
		idxTempLastIsotopomer = -1;
		
		/*�O������
			���q
			�@���ʑ�
			 �@�@�O�X�e�b�v�̃A�C�\�g�|�}�[
		�ŉ񂷁B���q�̃C���f�b�N�X��z��v�f�̎w�蓙�Ɏg���Ă���킯�ł͂Ȃ����A���Ƃ̓��ꐫ���������邽�߂�0����(���q��-1)�̃C���f�b�N�X�ŉ񂷁B*/
		for(idxAtom = 0; idxAtom <= numAtom-1; idxAtom++) {
			
			for(idxIsotope = 0; idxIsotope <= element.getNumIsotope()-1; idxIsotope++) {
				for(idxIsotopomer = 0; idxIsotopomer <= idxLastIsotopomer; idxIsotopomer++) {
					
					tempMass = isotopomer[idxIsotopomer].mass + element.getIsotope(idxIsotope).mass;
					tempAbundance = isotopomer[idxIsotopomer].abundance * element.getIsotope(idxIsotope).abundance;
					
					appendIsotopomer(tempMass, tempAbundance);
				}
			}
			

			HeapSortMode.heapSortMode(tempIsotopomer, 0, idxTempLastIsotopomer, "ABUND", "ZA");
			
			
			//�J�b�g�I�t�B��������ۂɍs���K�v������̂́AidxTempLatIsomer��(�ێ����ׂ��s�[�N��-1)�𒴂��Ă��鎞�̂݁B
			if(idxAtom <= (numAtom-1)-1) {
				if(idxTempLastIsotopomer > (numMaxHeldPeak = MAX_ISOTOPOMER / element.getNumIsotope()) -1)
					idxTempLastIsotopomer = numMaxHeldPeak-1;
			}
			else {
				if(idxTempLastIsotopomer > (numMaxHeldPeak = MAX_ISOTOPOMER / numIsotopeFollowElem) -1)
					idxTempLastIsotopomer = numMaxHeldPeak-1;
			}
			
			/*���̃��\�b�h�ł̓J�b�g�I�t�͎��{���Ă��K�i���͂��Ȃ����ƂƂ���*/
			
			//����̌��q��t���������ʂ��A�C�\�g�|�}�[�z��ɃX�g�A�B�z��̃C���f�b�N�X���ڊǂ��āA�o�b�t�@�N���A�B
			for(int i = 0; i <= idxTempLastIsotopomer; i++) {
				isotopomer[i] = tempIsotopomer[i].clone();
			}			
			idxLastIsotopomer = idxTempLastIsotopomer;
			idxTempLastIsotopomer = -1; //0�Ԉȍ~�̓S�~�B�܂�o�b�t�@�N���A��ԁB
		}		
	}
		
	
	//tempIsotopomer��mass, abundance��ǉ�����B
	private void appendIsotopomer(double mass, double abundance) {
		
		int idx;
		
		idx = 0;
		while(idx <= idxTempLastIsotopomer	&& tempIsotopomer[idx].mass != mass) {
			idx++;
		}
		
		if(idx <= idxTempLastIsotopomer) {
			tempIsotopomer[idx].abundance += abundance;
		}
		else {
			tempIsotopomer[idx].mass = mass;
			tempIsotopomer[idx].abundance = abundance; 
			idxTempLastIsotopomer = idx;
		}
	}
	
	
	public void summarize() {
		
		int idxBuf;
		
		HeapSortMode.heapSortMode(isotopomer, 0, idxLastIsotopomer, "ABUND", "ZA");
		
		//�o�b�t�@�N���A
		idxTempLastIsotopomer = -1;
		
		for(int i = 0; i<=idxLastIsotopomer; i++) {
			
			idxBuf = 0;
			while( !(tempIsotopomer[idxBuf].mass - 0.5 <= isotopomer[i].mass && isotopomer[i].mass < tempIsotopomer[idxBuf].mass + 0.5)
					&& idxBuf <= idxTempLastIsotopomer) {
				idxBuf++;
			}
			
			//tempIsotopoer�̒��ɋߎ�����m/z�̃G���g���[�����Ȃ������ꍇ�B�V�����G���g���[��ǉ��B
			if(idxBuf > idxTempLastIsotopomer) {
				tempIsotopomer[idxBuf].mass = isotopomer[i].mass;
				tempIsotopomer[idxBuf].abundance = isotopomer[i].abundance;
				idxTempLastIsotopomer = idxBuf;
			}
			//�ߎ�����m/z�̃G���g���[��temp�̒��ɗL�����ꍇ�͓�������B
			else {
				tempIsotopomer[idxBuf].abundance += isotopomer[i].abundance;
			}
		}
		
		idxLastSummaryIsotopomer = idxTempLastIsotopomer;
		
		summaryIsotopomer = new Isotope[idxLastSummaryIsotopomer + 1];
		for(int i = 0; i <= idxLastSummaryIsotopomer; i++) {
			summaryIsotopomer[i] = tempIsotopomer[i].clone();
		}
		
	}

	
	
	public Isotope[] getMsPattern() {
		//���т�߂��Ă���
		HeapSortMode.heapSortMode(isotopomer, 0, idxLastIsotopomer, "MASS", "AZ");
		
		return isotopomer;
	}
	
	
	public Isotope[] getSummaryMsPattern() {
		//���т�߂��Ă���
		HeapSortMode.heapSortMode(summaryIsotopomer, 0, idxLastSummaryIsotopomer, "MASS", "AZ");
		return summaryIsotopomer;
	}
	
	public void dispMsPattern() {
		int i;
		for(i = 0; i <= idxLastIsotopomer; i++) {
			System.out.println("Mass:" + isotopomer[i].mass+", Abund:"+ isotopomer[i].abundance);
		}
	}
	
}


interface SiMsPatternCutOff {
	public static final int MAX_ISOTOPOMER = 100;
	
	public static final int NO_FOLLOWING_ELEMENT = 1; //�����1�ł��邱�ƂɈӖ�������B�������f�̃A�C�\�g�[�v���Ƃ��ĂP���^������Ɓi���̃A�C�\�g�[�v��==1�jx(���̃s�[�N��)�@<= MaxIsotop�őł��؂肪�N����Ȃ��B
}


