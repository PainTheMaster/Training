


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
			
			//監査を受けるべき管理職はこれ以上のもの。(別の言い方をすればこれより下は、今現在平社員)
			//len_arr-1が末尾の要素のインデックスrであることに注意して、sup = ((len_arr-1)-1)/2 == len_arr/2-1。
			relIdxLast = idxLast-idxFrom;
			relIdxSup = (relIdxLast-1)/2;
			
			while( relIdxSup >= 0 ) {
				
				relIdxDrop = relIdxSup;
				settled = false;
				
				//このwhileは比較対象の部下の選定。二人のうち一人しかいなければそれが比較対象。二人いれば当然大きい方
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
