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
		
		
		BuildingBlockReader bbr = new BuildingBlockReader("D:\\BBtest.csv", ',', element); 
		
		
//		BuildingBlock[] bbfull=null, bbdepro=null, bbprotgr=null;
		
		
		ArrayList<BuildingBlock> bbfull, bbdepro, bbprotgr;
		
		bbfull = new ArrayList<BuildingBlock>();
		bbdepro = new ArrayList<BuildingBlock>();
		bbprotgr  = new ArrayList<BuildingBlock>();

		
		bbr.setBuildingBlocks(bbfull, bbdepro, bbprotgr);
		
		System.out.println("size"+bbfull.size()+", "+bbdepro.size()+", "+bbprotgr.size());
		
		for(int i = 0; i<= bbfull.size()-1; i++) {
			System.out.println(bbfull.get(i).toString());
		}
	}

}
