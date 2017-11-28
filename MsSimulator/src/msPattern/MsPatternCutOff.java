package msPattern;

import miscservice.*;


public class MsPatternCutOff implements SiMsPatternCutOff {
	String formula;
	Composition[] composition;
	
	private double exactmass;
	private double molweight;
	private int	idxLastIsotopomer, idxTempLastIsotopomer, idxSummaryIsotopomer; //どこまで（意味のある）データが充填されているか。これより後ろはゴミ
	private int numElement;
	
	Isotope[] isotopomer, tempIsotopomer, summaryIsotopomer;
	
	
	
	public MsPatternCutOff(String formula, Element[] arrElement) {
		this.formula = formula;
		composition = Composition.allocElement(formula, arrElement);
		numElement = composition.length;
		
		isotopomer = new Isotope[MAX_ISOTOPOMER];
		tempIsotopomer = new Isotope[MAX_ISOTOPOMER];
		summaryIsotopomer = new Isotope[MAX_ISOTOPOMER];
		for(int i = 0; i<=MAX_ISOTOPOMER-1; i++) {
			isotopomer[i] = new Isotope();
			tempIsotopomer[i] = new Isotope();
			summaryIsotopomer[i] = new Isotope();
		}
	}
	
	
	public void msPeakBuildAccur() {
		
		double sumAbundance;
		
		//質量0が100%。つまり何もない状態からスタート。
		isotopomer[0].mass = 0.0;
		isotopomer[0].abundance = 1.0;
		idxLastIsotopomer = 0;
		
		//バッファクリア
		idxTempLastIsotopomer = -1;		
		
		
		for(int i = 0; i <= numElement-1; i++) {
			
			if(i <= numElement-1-1) {
				split(composition[i], composition[i+1].getElem().getNumIsotope());
			}
			else {
				split(composition[i], NO_FOLLOWING_ELEMENT);
			}
		}
		
		sumAbundance = 0.0;
		for(int i = 0; i <= idxLastIsotopomer; i++) {
			sumAbundance += isotopomer[i].abundance;
		}
		
		System.out.println("sumAbundance="+sumAbundance);
		
		try {
			Thread.sleep(10000L);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i <= idxLastIsotopomer; i++) {
			isotopomer[i].abundance /= sumAbundance;
		}
		
	}
	

	
	
	
	private void split(Composition elemComposition, int numIsotopeFollowElem) {
		Element element;
		int numAtom;
		int idxAtom, idxIsotope, idxIsotopomer;
		
		int numMaxHeldPeak;
		
		double tempMass, tempAbundance;
		
		element = elemComposition.getElem();
		numAtom = elemComposition.getNumAtom();
		
		//バッファクリア
		idxTempLastIsotopomer = -1;
		
		/*外側から
			原子
			　同位体
			 　　前ステップのアイソトポマー
		で回す。原子のインデックスを配列要素の指定等に使っているわけではないが、他との統一性を持たせるために0から(原子数-1)のインデックスで回す。*/
		for(idxAtom = 0; idxAtom <= numAtom-1; idxAtom++) {
			
			for(idxIsotope = 0; idxIsotope <= element.getNumIsotope()-1; idxIsotope++) {
				for(idxIsotopomer = 0; idxIsotopomer <= idxLastIsotopomer; idxIsotopomer++) {
					
					tempMass = isotopomer[idxIsotopomer].mass + element.getIsotope(idxIsotope).mass;
					tempAbundance = isotopomer[idxIsotopomer].abundance * element.getIsotope(idxIsotope).abundance;
					
					appendIsotopomer(tempMass, tempAbundance);
				}
			}
			

			HeapSortMode.heapSortMode(tempIsotopomer, 0, idxTempLastIsotopomer, "ABUND", "ZA");
			
			
			//カットオフ。これを実際に行う必要があるのは、idxTempLatIsomerが(保持すべきピーク数-1)を超えている時のみ。
			if(idxAtom <= (numAtom-1)-1) {
				if(idxTempLastIsotopomer > (numMaxHeldPeak = MAX_ISOTOPOMER / element.getNumIsotope()) -1)
					idxTempLastIsotopomer = numMaxHeldPeak-1;
			}
			else {
				if(idxTempLastIsotopomer > (numMaxHeldPeak = MAX_ISOTOPOMER / numIsotopeFollowElem) -1)
					idxTempLastIsotopomer = numMaxHeldPeak-1;
			}
			
			/*このメソッドではカットオフは実施しても規格化はしないこととする*/
			
			//今回の原子を付加した結果をアイソトポマー配列にストア。配列のインデックスも移管して、バッファクリア。
			for(int i = 0; i <= idxTempLastIsotopomer; i++) {
				isotopomer[i] = tempIsotopomer[i].clone();
			}			
			idxLastIsotopomer = idxTempLastIsotopomer;
			idxTempLastIsotopomer = -1; //0番以降はゴミ。つまりバッファクリア状態。
		}		
	}
		
	
	
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
	
	
	private void summarize() {
		
		int idxBuf;
		
		HeapSortMode.heapSortMode(isotopomer, 0, idxLastIsotopomer, "ABUND", "ZA");
		
		//バッファクリア
		idxTempLastIsotopomer = -1;
		
		for(int i = 0; i<=idxLastIsotopomer; i++) {
			
			idxBuf = 0;
			while( !(tempIsotopomer[idxBuf].mass - 0.5 <= isotopomer[i].mass || isotopomer[i].mass < tempIsotopomer[idxBuf].mass + 0.5)
					&& idxBuf <= idxTempLastIsotopomer) {
				idxBuf++;
			}
			
			
			if(idxBuf > idxTempLastIsotopomer) {
				tempIsotopomer[idxBuf].mass = isotopomer[i].mass;
				tempIsotopomer[idxBuf].abundance = isotopomer[i].abundance;
				idxTempLastIsotopomer = idxBuf;
			}
			else {
				tempIsotopomer[idxBuf].abundance += isotopomer[i].abundance;
			}
		}
		
	}

	
	
	
/*	private void summarize() {
		double intmass;
		int idxSummarize;
		
		for(int i = 0; i <= numIsotopomer-1; i++) {
			tempIsotopeDouble[i].abundance = 0.0;
			tempIsotopeDouble[i].mass = 0.0;
		}
		idxTempLastIsotopomer = 0;
		
		for(idxSummarize = 0; idxSummarize <= idxLastIsotopomer; idxSummarize++ ) {
			intmass = Math.rint(isotopeDouble[idxSummarize].mass);
			appendIsotopomer(intmass, isotopeDouble[idxSummarize].abundance);
			
			isotopeDouble[idxSummarize].mass = 0.0;
			isotopeDouble[idxSummarize].abundance = 0.0;
		}
		
		for(int i = 0; i <= idxTempLastIsotopomer; i++) {
			isotopeDouble[i] = tempIsotopeDouble[i].clone();
			tempIsotopeDouble[i].abundance = 0.0;
			tempIsotopeDouble[i].mass = 0.0;
		}
		idxLastIsotopomer = idxTempLastIsotopomer;
		idxTempLastIsotopomer = 0;
		
	}*/
	
	
	public void dispMsPattern() {
		int i;
		for(i = 0; i <= idxLastIsotopomer; i++) {
			System.out.println("Mass:" + isotopomer[i].mass+", Abund:"+ isotopomer[i].abundance);
		}
	}
	
}


interface SiMsPatternCutOff {
	public static final int MAX_ISOTOPOMER = 100;
	
	public static final int NO_FOLLOWING_ELEMENT = 1; //これは1であることに意味がある。続く元素のアイソトープ数として１が与えられると（次のアイソトープ数==1）x(今のピーク数)　<= MaxIsotopで打ち切りが起こらない。
}


