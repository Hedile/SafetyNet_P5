package com.openclassrooms.safetynetalertsP5.dto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Family {
	private List<PersonInfoMedical> family;

	/**
	 * @param family
	 */
	public Family(List<PersonInfoMedical> family) {
		super();
		this.family = family;
	}

	/**
	 * 
	 */
	public Family() {
		super();
	}

	public List<PersonInfoMedical> getFamily() {
		return family;
	}

	public void setFamily(List<PersonInfoMedical> family) {
		this.family = family;
	}

}
