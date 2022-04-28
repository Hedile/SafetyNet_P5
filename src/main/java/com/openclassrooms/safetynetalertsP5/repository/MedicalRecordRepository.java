package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;
	private static final Logger logger = LogManager.getLogger("MedicalRecordRepository");

	/** Endpoint de POST /medicalRecord **/
	public MedicalRecord save(MedicalRecord medicalrecordToAdd) {

		MedicalRecord medicalrecord = findByFirstNameAndLastName(medicalrecordToAdd.getFirstName(),
				medicalrecordToAdd.getLastName());
		if (medicalrecord == null) {
			LoadDataJSON.listMedicalrecords.add(medicalrecordToAdd);
			saveDataJSON.saveData();
			logger.info("MedicalRecord created: {}  {}", medicalrecordToAdd.getFirstName(),
					medicalrecordToAdd.getLastName());
			return medicalrecordToAdd;

		} else {
			logger.error("A medicalRecord already exists with firstname ={} and lastname = {} ",
					medicalrecord.getFirstName(), medicalrecord.getLastName());
			return null;
		}
	}

	/** Endpoint de PUT /medicalRecord **/
	public MedicalRecord updateMedicalRecordByFirstNameAndLastName(MedicalRecord medicalrecordToUpdate) {

		MedicalRecord medicalrecord = findByFirstNameAndLastName(medicalrecordToUpdate.getFirstName(),
				medicalrecordToUpdate.getLastName());
		if (medicalrecord != null) {
			medicalrecord.setBirthdate(medicalrecordToUpdate.getBirthdate());
			medicalrecord.setMedications(medicalrecordToUpdate.getMedications());
			medicalrecord.setAllergies(medicalrecordToUpdate.getAllergies());
			saveDataJSON.saveData();
			logger.info("MedicalRecord updated: {} {}", medicalrecordToUpdate.getFirstName(),
					medicalrecordToUpdate.getLastName());
			return medicalrecord;
		} else {
			logger.error("Failed to find medicalRecord: {}  {}", medicalrecordToUpdate.getFirstName(),
					medicalrecordToUpdate.getLastName());
			return null;
		}

	}

	/** Endpoint de DELETE /medicalRecord **/
	public MedicalRecord deleteMedicalRecordByFirstNameAndLastName(String fn, String ln) {
		MedicalRecord medicalrecordToDelete = findByFirstNameAndLastName(fn, ln);
		if (medicalrecordToDelete != null) {
			LoadDataJSON.listMedicalrecords.remove(medicalrecordToDelete);
			saveDataJSON.saveData();
			logger.info("MedicalRecord deleted: {}  {}", medicalrecordToDelete.getFirstName(),
					medicalrecordToDelete.getLastName());
			return medicalrecordToDelete;
		} else {
			logger.error("Failed to find medicalRecord: {}  {}  ", fn, ln);
			return null;
		}
	}

	public List<MedicalRecord> findAll() {
		return LoadDataJSON.listMedicalrecords;

	}

	/** Find medical records by firstname and lastname */
	public MedicalRecord findByFirstNameAndLastName(String fn, String ln) {

		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			if (m.getFirstName().equals(fn) && m.getLastName().equals(ln)) {
				logger.info("MedicalRecord found: {}  {}", fn, ln);
				return m;
			}
		}

		logger.error("MedicalRecord not found: {} {} ", fn, ln);
		return null;
	}
}
