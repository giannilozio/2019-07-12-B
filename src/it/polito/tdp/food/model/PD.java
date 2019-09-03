package it.polito.tdp.food.model;

public class PD {
	
	private Food f1;
	private Food f2;
	private double media1;
	private double media2;
	
	
	public PD(Food f1, Food f2, double media1) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.media1 = media1;
		
	}
	
	public Food getF1() {
		return f1;
	}
	public void setF1(Food f1) {
		this.f1 = f1;
	}
	public Food getF2() {
		return f2;
	}
	public void setF2(Food f2) {
		this.f2 = f2;
	}
	public double getMedia1() {
		return media1;
	}

	public void setMedia1(double media1) {
		this.media1 = media1;
	}

	public double getMedia2() {
		return media2;
	}

	public void setMedia2(double media2) {
		this.media2 = media2;
	}
	
	

}
