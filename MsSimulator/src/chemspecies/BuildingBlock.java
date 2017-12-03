package chemspecies;

import java.util.*;

public class BuildingBlock implements Cloneable{
	
	private String name;
	private Composition[] composition;
	private int kind;

	
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setComposition(Composition[] arrComposition) {
		composition = new Composition[arrComposition.length];
		for(int i = 0; i<= arrComposition.length -1; i++)
			composition[i] = arrComposition[i].clone();
	}
	
	
	public void setComposition(ArrayList<Composition> listComposition) {
		composition = new Composition[listComposition.size()];
		for(int i = 0; i<= listComposition.size() -1; i++)
			composition[i] = listComposition.get(i).clone();
	}
	
	
	public void appendCpomosition(Composition toBeAdded) {
		Composition[] temp;
		temp = new Composition[this.composition.length+1];
		
		for(int i = 0; i <= this.composition.length-1; i++) {
			temp[i] = composition[i].clone();
		}
		
		temp[composition.length] = toBeAdded.clone();
	}
		
	public void setKind(int kind) {
		this.kind = kind; 
	}
	

	
	
	public String getName() {
		return name;
	}
	
	
	public int getKind() {
		return kind; 
	}
	
	
	@Override
	public BuildingBlock clone(){
		BuildingBlock temp = null;
		
		try {
			temp = (BuildingBlock) super.clone();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i <= this.composition.length - 1; i++ ) {
			temp.composition[i] = this.composition[i].clone();
		}
		
		return temp;
	}
	
	
	@Override
	public String toString() {
		StringBuilder tempStrBuild = new StringBuilder();
		
		tempStrBuild.append("BuildingBlock [" + name + "]\n");
		tempStrBuild.append("kind: "+ kind+"\n");
		for(int i = 0; i<=composition.length-1; i++) {
			tempStrBuild.append(composition[i].getElement().getSymbol() );
			tempStrBuild.append(composition[i].getNumAtom());
		}
		tempStrBuild.append("\n");
		
		return tempStrBuild.toString();
	}
	
	
	
}