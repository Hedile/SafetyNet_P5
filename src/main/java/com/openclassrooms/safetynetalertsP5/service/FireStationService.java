package com.openclassrooms.safetynetalertsP5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.repository.FireStationRepository;

@Service
public class FireStationService {

	@Autowired
	private FireStationRepository fireStationRepository;

	/** Endpoint de POST /firestation **/
	public FireStation addFireStation(FireStation firestation) {
		return fireStationRepository.save(firestation);

	}

	/** Endpoint de PUT /firestation **/
	public FireStation updateFireStation(FireStation firestation) {
		return fireStationRepository.updateByAddress(firestation);

	}

	/** Endpoint de DELETE /firestation **/
	public FireStation deleteFireStation(String address) {
		return fireStationRepository.deleteByAddress(address);

	}

	public List<FireStation> getAllFireStations() {
		return fireStationRepository.findAll();
	}

	public FireStation getFireStation(String address) {
		return fireStationRepository.findByAddress(address);
	}

}
