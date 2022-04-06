package com.openclassrooms.safetynetalertsP5.dto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Children {
	private List<Child> chidren;

	/**
	 * @param chidren
	 */
	public Children(List<Child> chidren) {
		super();
		this.chidren = chidren;
	}

	/**
	 * 
	 */
	public Children() {
		super();
	}

	public List<Child> getChidren() {
		return chidren;
	}

	public void setChidren(List<Child> chidren) {
		this.chidren = chidren;
	}

}
