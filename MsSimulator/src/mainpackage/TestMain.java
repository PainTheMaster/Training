package mainpackage;

import chemspecies.Element;
import msPattern.*;
import readers.*;

public class TestMain {

	public static void main(String[] args) {
		
		Element[] element;
		ElementInfoReader eleminforeader = new ElementInfoReader("d:\\ElementTable.csv", ',');
		
		element = eleminforeader.setElements();
		
		
		for(int i = 0; i <= element.length-1; i++) {
			System.out.println(element[i].toString());
//
		}
	}

}
