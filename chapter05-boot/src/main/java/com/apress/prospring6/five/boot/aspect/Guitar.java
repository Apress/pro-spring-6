package com.apress.prospring6.five.boot.aspect;

/**
 * Created by iuliana.cosmina on 18/04/2022
 */
public class Guitar {
	private String brand =" Martin";

	public String play(){
		return "G C G C Am D7";
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}
