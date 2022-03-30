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

	public FireStation addFireStation(FireStation firestation) {
		return fireStationRepository.save(firestation);

	}

	public FireStation updateFireStation(FireStation firestation) {
		return fireStationRepository.updateByAddress(firestation);

	}

	public void deleteFireStation(String address) {
		fireStationRepository.deleteByAddress(address);

	}

	public List<FireStation> getAllFireStations() {
		return fireStationRepository.findAll();
	}

	public FireStation getFireStation(String address) {
		return fireStationRepository.findByAddress(address);
	}

}
