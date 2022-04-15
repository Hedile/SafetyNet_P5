package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.service.FireStationService;

@RestController
public class FireStationController {

	@Autowired
	private FireStationService fireStationService;

	private static final Logger logger = LogManager.getLogger("FireStationService");

	public FireStationController(FireStationService fireStationService) {
		this.fireStationService = fireStationService;
	}

	/** Endpoint Get /firestation **/
	/*
	 * @GetMapping("/Firestations") public List<FireStation> getAllFireStations() {
	 * logger.info("Get/Firestations"); List<FireStation> firestations =
	 * fireStationService.getAllFireStations(); if (firestations != null) {
	 * logger.info("Get ok"); return firestations; } else {
	 * logger.error("firestations Not found"); return null; } }
	 * 
	 * 
	 * @GetMapping(value = "/Firestations/{Address}") public FireStation
	 * getFireStationByAddress(@PathVariable("Address") String address) { return
	 * fireStationService.getFireStation(address); }
	 */
	/** Endpoint POST /firestation **/
	@PostMapping(value = "/Firestations")
	public ResponseEntity<FireStation> createFirestation(@RequestBody FireStation firestation) {
		FireStation addedFireStation = fireStationService.addFireStation(firestation);
		if (Objects.isNull(addedFireStation)) {
			logger.error("Fire station is not created");
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Address}/{Station}")
				.buildAndExpand(addedFireStation.getAddress(), addedFireStation.getStation()).toUri();
		logger.info("Post OK");
		return ResponseEntity.created(location).build();
	}

	/** Endpoint PUT /firestation **/
	@PutMapping(value = "/Firestations/{Address}")
	public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation f,
			@PathVariable("Address") String address) {
		FireStation updatedFireStation = fireStationService.updateFireStation(f);
		if (updatedFireStation != null) {
			logger.info("Put OK");

			return ResponseEntity.ok(updatedFireStation);
		} else {
			logger.error("No update");
			return ResponseEntity.notFound().build();
		}
	}

	/** Endpoint DELETE /firestations **/
	@DeleteMapping(value = "/Firestations/{Address}")
	public Map<String, Boolean> deleteFireStation(@PathVariable("Address") String address) {
		FireStation deletedFiresStation = fireStationService.deleteFireStation(address);
		Map<String, Boolean> response = new HashMap<>();
		if (deletedFiresStation != null) {
			response.put("deleted", Boolean.TRUE);
			logger.info("Successful deletion");
			return response;
		} else {
			logger.error("Deletion failed");
			ResponseEntity.notFound().build();
			response.put("deleted", Boolean.FALSE);
			return response;
		}
	}

}
