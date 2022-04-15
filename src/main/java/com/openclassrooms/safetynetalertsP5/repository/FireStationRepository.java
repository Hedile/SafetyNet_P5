package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.exceptions.FireStationNotFoundException;
import com.openclassrooms.safetynetalertsP5.model.FireStation;

@Repository
public class FireStationRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;
	private static final Logger logger = LogManager.getLogger("FireStationRepository ");

	/** Endpoint de POST /firestation **/
	public FireStation save(FireStation firestationToAdd) {
		try {
			FireStation firestation = findByAddress(firestationToAdd.getAddress());
			logger.error("A Fire Station already exists with address ={}  ", firestation.getAddress());
			return null;
		} catch (FireStationNotFoundException e) {
			LoadDataJSON.listFirestations.add(firestationToAdd);
			saveDataJSON.saveData();
			logger.info("Fire Station created: Address ={} a", firestationToAdd.getAddress());
			return firestationToAdd;
		}

	}

	/** Endpoint de PUT /firestation **/
	public FireStation updateByAddress(FireStation firestationToUpdate) {
		try {
			FireStation firestation = findByAddress(firestationToUpdate.getAddress());
			firestation.setStation(firestationToUpdate.getStation());
			saveDataJSON.saveData();
			logger.info("Fire Station updated: Address ={}", firestation.getAddress());
			return firestation;
		} catch (FireStationNotFoundException e) {
			logger.error("Failed to find fire station with address ={}  ", firestationToUpdate.getAddress());
			return null;
		}

	}

	/** Endpoint de DELETE /firestation **/
	public FireStation deleteByAddress(String address) {
		try {
			FireStation firestationToDelete = findByAddress(address);
			LoadDataJSON.listFirestations.remove(firestationToDelete);
			saveDataJSON.saveData();
			logger.info("Fire Station deleted: Address ={} a", firestationToDelete.getAddress());
			return firestationToDelete;
		} catch (FireStationNotFoundException e) {
			logger.error("Failed to find fire station with address ={}  ", address);
			return null;
		}
	}

	public List<FireStation> findAll() {
		return LoadDataJSON.listFirestations;

	}

	/** Find Fire station by address */
	public FireStation findByAddress(String address) {
		FireStation firestationFound = null;
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(address)) {
				logger.info("Fire station found: address = {} ", address);
				firestationFound = f;
			}
		}
		if (firestationFound != null) {
			logger.info("Firestation found: address ={} ", address);
			return firestationFound;
		} else {
			throw new FireStationNotFoundException("FireStation not found with address=" + address);
		}

	}

}
