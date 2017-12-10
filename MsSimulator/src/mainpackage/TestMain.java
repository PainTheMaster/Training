package mainpackage;

import chemspecies.*;
import msPattern.*;
import readers.*;
import java.util.*;

public class TestMain {

	public static void main(String[] args) {
		
		Element[] element;
		ElementInfoReader eleminforeader = new ElementInfoReader("d:\\ElementTable.csv", ',');
		
		element = eleminforeader.setElements();
		
		
		eleminforeader.close();
		
		BuildingBlockReader bbr = new BuildingBlockReader("D:\\BBtest.csv", ',', element); 
		
			
		ArrayList<BuildingBlock> bbfull, bbdepro, firstfull, firstdepro, bbmisc;
		
		bbfull = new ArrayList<BuildingBlock>();
		bbdepro = new ArrayList<BuildingBlock>();
		firstfull = new ArrayList<BuildingBlock>();
		firstdepro  = new ArrayList<BuildingBlock>();
		bbmisc  = new ArrayList<BuildingBlock>();
		
		bbr.setBuildingBlocks(bbfull, bbdepro, firstfull, firstdepro, bbmisc);
		
		System.out.println("size"+bbfull.size()+", "+bbdepro.size()+", "+","+firstfull.size()+","+firstdepro.size()+","+bbmisc.size());
		
		bbr.close();
		
		for(int i = 0; i<= bbfull.size()-1; i++) {
			System.out.println(bbfull.get(i).toString());
		}
		
		String sequence = "TOBO suc A C G T";
		PmoSequence ps = new PmoSequence(sequence, bbfull, bbdepro, firstfull, firstdepro, bbmisc);
		

		for(int i = 0; i <= ps.deProtTrtOn.length-1; i++) {
			System.out.print(ps.deProtTrtOn[i].toString());
			System.out.println();
		}
		
	}

}
