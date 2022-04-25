package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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

import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.service.FireStationService;

@RestController
public class FireStationController {

	@Autowired
	private FireStationService fireStationService;

	private static final Logger logger = LogManager.getLogger("FireStationService");

	public FireStationController() {
		super();
	}

	/** Endpoint POST /firestation **/
	@PostMapping(value = "/Firestations")
	public ResponseEntity<FireStation> createFirestation(@RequestBody FireStation firestation) {
		FireStation addedFireStation = fireStationService.addFireStation(firestation);
		if (addedFireStation == null) {
			logger.error("Failed create Firestation");
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Address}/{Station}")
				.buildAndExpand(addedFireStation.getAddress(), addedFireStation.getStation()).toUri();
		logger.info("The firestation is created successfully");
		return ResponseEntity.created(location).build();
	}

	/** Endpoint PUT /firestation **/
	@PutMapping(value = "/Firestations/{Address}")
	public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation f,
			@PathVariable("Address") String address) {
		FireStation updatedFireStation = fireStationService.updateFireStation(f);
		if (updatedFireStation != null) {
			logger.info("Person is updated successfully");

			return ResponseEntity.ok(updatedFireStation);
		} else {
			logger.error("Failed update ");
			throw new NotFoundException("FireStation with address=" + address + " does not exist.");

		}
	}

	/** Endpoint DELETE /firestations **/
	@DeleteMapping(value = "/Firestations/{Address}")
	public Map<String, Boolean> deleteFireStation(@PathVariable("Address") String address) {
		FireStation deletedFiresStation = fireStationService.deleteFireStation(address);
		Map<String, Boolean> response = new HashMap<>();
		if (deletedFiresStation != null) {
			response.put("deleted", Boolean.TRUE);
			logger.info("FireStation is deleted successfully");
			return response;
		} else {
			logger.error("Failed Deletion ");
			throw new NotFoundException("FireStation  with address=" + address + " does not exist.");

		}
	}

}
