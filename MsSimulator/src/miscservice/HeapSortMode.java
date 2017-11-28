package miscservice;

public class HeapSortMode {

	
	public static <T extends ModeComparable<T>> void heapSortMode(T[] a, int idxFrom, int idxLast,String mode ,int azza) {
		
		int relIdxSet;
		T temp;
		
		heapMode(a, idxFrom ,idxLast, mode, azza);

		relIdxSet = idxLast-idxFrom;
		while(relIdxSet >= 1) {
			temp = a[relIdxSet+idxFrom];
			a[relIdxSet+idxFrom] = a[idxFrom];
			a[idxFrom] = temp;
			
			relIdxSet--;
			heapMode(a, idxFrom, relIdxSet+idxFrom, mode, azza);
		}
		
	}
	
	
	
	public static <T extends ModeComparable<T>> void heapSortMode(T[] a, int idxFrom, int idxLast,String mode ,String azzaStr) {
		
		int relIdxSet, azza;
		T temp;
		
		if(azzaStr.compareTo("AZ") == 0) {
			azza = 1;
		}
		else if (azzaStr.compareTo("ZA") == 0) {
			azza = -1;
		}
		else {
			azza = 1;
		}
		
		
		heapMode(a, idxFrom ,idxLast, mode, azza);

		relIdxSet = idxLast-idxFrom;
		while(relIdxSet >= 1) {
			temp = a[relIdxSet+idxFrom];
			a[relIdxSet+idxFrom] = a[idxFrom];
			a[idxFrom] = temp;
			
			relIdxSet--;
			heapMode(a, idxFrom, relIdxSet+idxFrom, mode, azza);
		}
		
	}
	
	
	
	
	private static <T extends ModeComparable<T>> void heapMode(T[] a,int idxFrom, int idxLast, String mode, int azza) {
			
		
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
					else if (a[relIdxDrop*2+1+idxFrom].modeCompareTo(a[relIdxDrop*2+2+idxFrom], mode)*azza > 0)
						relIdxInf = relIdxDrop*2+1;
					else
						relIdxInf = relIdxDrop*2+2;
					
					if(a[relIdxInf+idxFrom].modeCompareTo(a[relIdxDrop+idxFrom], mode)*azza > 0){
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