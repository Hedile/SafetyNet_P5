package com.openclassrooms.safetynetalertsP5.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.service.ReponseService;

@RestController
public class ReponseController {
	@Autowired
	private ReponseService reponseService;

	/** Endpoint de firestation?stationNumber=<station_number **/

	@GetMapping(value = "/Firestations")
	public PersonsByStationNumber personsByStation(@RequestParam("station") String station_number) {

		return reponseService.getPersonsByStationNumber(station_number);
	}

	/** Endpoint de childAlert?address=<address> **/

	@GetMapping(value = "childAlert")
	public Children childrenByAddress(@RequestParam("address") String address) {

		return reponseService.childrenByAddressAnotherMethod(address);
	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> **/

	@GetMapping(value = "phoneAlert")
	public List<String> phoneByStation(@RequestParam("station") String stationNbr) {

		return reponseService.getPhoneNumberByStation(stationNbr);
	}

	/** Endpoint de fire?address=<address> **/

	@GetMapping(value = "fire")
	public PersonsServedByFireStation personInfoByAddress(@RequestParam("address") String address) {

		return reponseService.getPersonInfoByAddress(address);
	}

	/** Endpoint de flood/stations?stations=<a list of station_number> **/

	@GetMapping(value = "/flood/stations")
	public List<Family> familiesByStationNumbers(@RequestParam("stations") List<String> stations) {

		return reponseService.getFamiliesByStationNumbers(stations);
	}

	/** Endpont de personInfo?firstName=<firstName>&lastName=<lastName> **/

	@GetMapping(value = "personInfo")
	public List<PersonInfo> personsInfoByName(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {

		return reponseService.getPersonsInfoByName(firstName, lastName);
	}

	/** Endpoint de communityEmail?city=<city> **/

	@GetMapping(value = "communityEmail")
	public List<String> emailsByCity(@RequestParam("city") String city) {

		return reponseService.getEmailsByCity(city);
	}
}
