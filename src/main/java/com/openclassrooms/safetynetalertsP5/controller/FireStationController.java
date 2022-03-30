package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	public FireStationController(FireStationService fireStationService) {
		this.fireStationService = fireStationService;
	}

	/** Endpoint Get /firestation **/
	@GetMapping("/Firestations")
	public List<FireStation> getAllFireStations() {
		return fireStationService.getAllFireStations();
	}

	@GetMapping(value = "/Firestations/{Address}")
	public FireStation getFireStationByAddress(@PathVariable("Address") String address) {
		return fireStationService.getFireStation(address);

	}

	/** Endpoint POST /firestation **/
	@PostMapping(value = "/Firestations")
	public ResponseEntity<FireStation> createPerson(@RequestBody FireStation firestation) {
		FireStation addedFireStation = fireStationService.addFireStation(firestation);
		if (Objects.isNull(addedFireStation)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Address}/{Station}")
				.buildAndExpand(addedFireStation.getAddress(), addedFireStation.getStation()).toUri();
		return ResponseEntity.created(location).build();
	}

	/** Endpoint PUT /firestation **/
	@PutMapping(value = "/Firestations/{Address}")
	public void updateFireStation(@RequestBody FireStation f, @PathVariable("Address") String address) {
		fireStationService.updateFireStation(f);
	}

	/** Endpoint DELETE /firestations **/
	@DeleteMapping(value = "/Firestations/{Address}")
	public void deleteFireStation(@PathVariable("Address") String address) {
		fireStationService.deleteFireStation(address);
	}

}
