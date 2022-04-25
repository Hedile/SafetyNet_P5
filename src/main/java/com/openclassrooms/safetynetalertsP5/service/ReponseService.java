package com.openclassrooms.safetynetalertsP5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.repository.ReponseRepository;

@Service
public class ReponseService {
	@Autowired
	private ReponseRepository reponseRepository;

	/** Endpoint de firestation?stationNumber=<station_number **/
	public PersonsByStationNumber getPersonsByStationNumber(String station_number) {
		return reponseRepository.listPersonsByStationNumber(station_number);
	}

	/** Endpoint de childAlert?address=<address> **/
	public Children childrenByAddress(String address) {
		return reponseRepository.childrenByAddress(address);
	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> **/
	public List<String> getPhoneNumberByStation(String stationNbr) {
		return reponseRepository.findPhoneNumberByStation(stationNbr);
	}

	/** Endpoint de fire?address=<address> **/
	public PersonsServedByFireStation getPersonInfoByAddress(String address) {
		return reponseRepository.personInfoByAddress(address);
	}

	/** Endpoint de flood/stations?stations=<a list of station_number> **/
	public List<Family> getFamiliesByStationNumbers(List<String> stationNbrs) {
		return reponseRepository.findFamiliesByStationNumbers(stationNbrs);
	}

	/** Endpont de personInfo?firstName=<firstName>&lastName=<lastName> **/
	public List<PersonInfo> getPersonsInfoByName(String firstName, String lastName) {
		return reponseRepository.findPersonsInfoByName(firstName, lastName);
	}

	/** Endpoint de communityEmail?city=<city> **/
	public List<String> getEmailsByCity(String city) {
		return reponseRepository.findEmailsByCity(city);
	}
}
