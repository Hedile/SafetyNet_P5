package com.openclassrooms.safetynetalertsP5.dto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PersonsServedByFireStation {
	private List<PersonInfoMedical> personsServedByFireStation;
	private String StationNumber;

	/**
	 * @param persons
	 * @param stationNumber
	 */
	public PersonsServedByFireStation(List<PersonInfoMedical> persons, String stationNumber) {
		super();
		this.personsServedByFireStation = persons;
		StationNumber = stationNumber;
	}

	public PersonsServedByFireStation() {
		super();
	}

	public List<PersonInfoMedical> getPersonsServedByFireStation() {
		return personsServedByFireStation;
	}

	public void setPersonsServedByFireStation(List<PersonInfoMedical> personsServedByFireStation) {
		this.personsServedByFireStation = personsServedByFireStation;
	}

	public String getStationNumber() {
		return StationNumber;
	}

	public void setStationNumber(String stationNumber) {
		StationNumber = stationNumber;
	}

}
