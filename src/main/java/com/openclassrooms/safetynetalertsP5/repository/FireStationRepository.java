package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.FireStation;

@Repository
public class FireStationRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;

	public FireStation findByAddress(String address) {
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(address)) {
				return f;
			}
		}
		return null;
	}

	public FireStation updateByAddress(FireStation firestationToUpdate) {

		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(firestationToUpdate.getAddress())) {
				f.setStation(firestationToUpdate.getStation());

				saveDataJSON.saveData();
				return f;
			}

		}

		return null;
	}

	public FireStation deleteByAddress(String address) {
		FireStation firestationToDelete = new FireStation();
		for (FireStation f : LoadDataJSON.listFirestations) {
			if (f.getAddress().equals(address)) {

				firestationToDelete = f;
			}
		}
		LoadDataJSON.listFirestations.remove(firestationToDelete);
		saveDataJSON.saveData();
		return firestationToDelete;

	}

	public List<FireStation> findAll() {
		return LoadDataJSON.listFirestations;

	}

	public FireStation save(FireStation firestation) {
		LoadDataJSON.listFirestations.add(firestation);
		saveDataJSON.saveData();
		return firestation;

	}

}
