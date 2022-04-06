package com.openclassrooms.safetynetalertsP5.dto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PersonsByStationNumber {

	private List<PersonInfoGeneral> personsCoveredByFireStation;
	private int childrenNumber;
	private int adultsNumber;

	public PersonsByStationNumber() {
		super();
	}

	public PersonsByStationNumber(List<PersonInfoGeneral> persons, int childrenNbr, int adultsNbr) {
		super();
		this.personsCoveredByFireStation = persons;
		this.childrenNumber = childrenNbr;
		this.adultsNumber = adultsNbr;
	}

	public List<PersonInfoGeneral> getPersonsCoveredByFireStation() {
		return personsCoveredByFireStation;
	}

	public void setPersonsCoveredByFireStation(List<PersonInfoGeneral> personsCoveredByFireStation) {
		this.personsCoveredByFireStation = personsCoveredByFireStation;
	}

	public int getChildrenNumber() {
		return childrenNumber;
	}

	public void setChildrenNumber(int childrenNumber) {
		this.childrenNumber = childrenNumber;
	}

	public int getAdultsNumber() {
		return adultsNumber;
	}

	public void setAdultsNumber(int adultsNumber) {
		this.adultsNumber = adultsNumber;
	}

}
