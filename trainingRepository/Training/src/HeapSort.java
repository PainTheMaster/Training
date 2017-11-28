


public class HeapSort {

	
	public static <T extends Comparable<T>> void heapSort(T[] a, int idxFrom, int idxLast) {
		
		int relIdxSet;
		T temp;
		
		heap(a, idxFrom ,idxLast);

		relIdxSet = idxLast-idxFrom;
		while(relIdxSet >= 1) {
			temp = a[relIdxSet+idxFrom];
			a[relIdxSet+idxFrom] = a[idxFrom];
			a[idxFrom] = temp;
			
			relIdxSet--;
			heap(a, idxFrom, relIdxSet+idxFrom);
		}
		
	}
	
	private static <T extends Comparable<T>> void heap(T[] a,int idxFrom, int idxLast) {
			
		
			int relIdxSup, relIdxInf, relIdxDrop, relIdxLast;
			T buf;
			boolean settled;
			
			//�č����󂯂�ׂ��Ǘ��E�͂���ȏ�̂��́B(�ʂ̌�����������΂����艺�́A�����ݕ��Ј�)
			//len_arr-1�������̗v�f�̃C���f�b�N�Xr�ł��邱�Ƃɒ��ӂ��āAsup = ((len_arr-1)-1)/2 == len_arr/2-1�B
			relIdxLast = idxLast-idxFrom;
			relIdxSup = (relIdxLast-1)/2;
			
			while( relIdxSup >= 0 ) {
				
				relIdxDrop = relIdxSup;
				settled = false;
				
				//����while�͔�r�Ώۂ̕����̑I��B��l�̂�����l�������Ȃ���΂��ꂪ��r�ΏہB��l����Γ��R�傫����
				while((relIdxDrop*2+1 <= relIdxLast) && !settled) {
					if(relIdxDrop*2 + 2 > relIdxLast)
						relIdxInf = relIdxDrop*2+1;
					else if (a[relIdxDrop*2+1+idxFrom].compareTo(a[relIdxDrop*2+2+idxFrom]) > 0)
						relIdxInf = relIdxDrop*2+1;
					else
						relIdxInf = relIdxDrop*2+2;
					
					if(a[relIdxInf+idxFrom].compareTo(a[relIdxDrop+idxFrom])> 0){
						buf = a[relIdxDrop+idxFrom];
						a[relIdxDrop+idxFrom] = a[relIdxInf+idxFrom];
						a[relIdxInf+idxFrom] = buf;
						relIdxDrop = relIdxInf;
					}
					else
						settled = true;
				}
				
				relIdxSup--;
			}
		}
	
}
