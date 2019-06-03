package it.polito.tdp.ufo.model;

public class Year {
	
	private int year;
	private int numAvv;
	
	public Year(int year, int numAvv) {

		this.year = year;
		this.numAvv = numAvv;
	}

	public int getYear() {
		return year;
	}

	public int getNumAvv() {
		return numAvv;
	}

	@Override
	public String toString() {
		return year+" ("+numAvv+")";
	}
		
}
