package com.openclassrooms.safetynetalertsP5.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.service.ReponseService;

@RestController
public class ReponseController {
	@Autowired
	private ReponseService reponseService;
	private static final Logger logger = LogManager.getLogger("ReponseController");

	/** Endpoint de firestation?stationNumber=<station_number **/

	@GetMapping(value = "/Firestations")
	public ResponseEntity<PersonsByStationNumber> personsByStation(@RequestParam("station") String station_number) {

		PersonsByStationNumber persons = reponseService.getPersonsByStationNumber(station_number);
		if (persons != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(persons);
		} else {
			logger.error("Persons Not found");
			throw new NotFoundException("No people covered by the fireStation n°:" + station_number);
		}
	}

	/** Endpoint de childAlert?address=<address> **/

	@GetMapping(value = "/childAlert")
	public ResponseEntity<Children> childrenByAddress(@RequestParam("address") String address) {

		Children children = reponseService.childrenByAddress(address);
		if (children != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(children);
		} else {
			logger.error("children Not found");
			throw new NotFoundException("no children at this address:" + address);
		}

	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> **/

	@GetMapping(value = "/phoneAlert")
	public ResponseEntity<List<String>> phoneByStation(@RequestParam("station") String stationNbr) {

		List<String> phoneNumber = reponseService.getPhoneNumberByStation(stationNbr);
		if (phoneNumber != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(phoneNumber);
		} else {
			logger.error("Phone number Not found");
			throw new NotFoundException("Phone number Not found");
		}

	}

	/** Endpoint de fire?address=<address> **/

	@GetMapping(value = "/fire")
	public ResponseEntity<PersonsServedByFireStation> personInfoByAddress(@RequestParam("address") String address) {

		PersonsServedByFireStation personsServedByFireStation = reponseService.getPersonInfoByAddress(address);
		if (personsServedByFireStation != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(personsServedByFireStation);
		} else {
			logger.error("personsServedByFireStation Not found");
			return ResponseEntity.notFound().build();
		}
	}

	/** Endpoint de flood/stations?stations=<a list of station_number> **/

	@GetMapping(value = "/flood/stations")
	public ResponseEntity<List<Family>> familiesByStationNumbers(@RequestParam("stations") List<String> stations) {

		List<Family> families = reponseService.getFamiliesByStationNumbers(stations);
		if (families != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(families);
		} else {
			logger.error("families Not found");
			throw new NotFoundException("Families Not found");
		}
	}

	/** Endpont de personInfo?firstName=<firstName>&lastName=<lastName> **/

	@GetMapping(value = "/personInfo")
	public ResponseEntity<List<PersonInfo>> personsInfoByName(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {

		List<PersonInfo> personInfoList = reponseService.getPersonsInfoByName(firstName, lastName);
		if (personInfoList != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(personInfoList);
		} else {
			logger.error("personInfos Not found");
			throw new NotFoundException("personInfos Not found");
		}
	}

	/** Endpoint de communityEmail?city=<city> **/

	@GetMapping(value = "/communityEmail")
	public ResponseEntity<List<String>> emailsByCity(@RequestParam("city") String city) {

		List<String> emails = reponseService.getEmailsByCity(city);
		if (emails != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(emails);
		} else {
			logger.error("emails Not found");
			throw new NotFoundException("Emails Not found");
		}
	}
}
