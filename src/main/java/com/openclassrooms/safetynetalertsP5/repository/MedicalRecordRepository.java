package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.exceptions.MedicalRecordNotFoundException;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;
	private static final Logger logger = LogManager.getLogger("MedicalRecordRepository");

	/** Endpoint de POST /medicalRecord **/
	public MedicalRecord save(MedicalRecord medicalrecordToAdd) {
		try {
			MedicalRecord medicalrecord = findByFirstNameAndLastName(medicalrecordToAdd.getFirstName(),
					medicalrecordToAdd.getLastName());
			logger.error("A medical record already exists with firstname ={} and lastname = {} ",
					medicalrecord.getFirstName(), medicalrecord.getLastName());
			return null;
		} catch (MedicalRecordNotFoundException e) {
			LoadDataJSON.listMedicalrecords.add(medicalrecordToAdd);
			saveDataJSON.saveData();
			logger.info("Medical record created: firstname ={} and lastname = {}", medicalrecordToAdd.getFirstName(),
					medicalrecordToAdd.getLastName());
			return medicalrecordToAdd;
		}
	}

	/** Endpoint de PUT /medicalRecord **/
	public MedicalRecord updateByFirstNameAndLastName(MedicalRecord medicalrecordToUpdate) {
		try {
			MedicalRecord medicalrecord = findByFirstNameAndLastName(medicalrecordToUpdate.getFirstName(),
					medicalrecordToUpdate.getLastName());
			medicalrecord.setBirthdate(medicalrecordToUpdate.getBirthdate());
			medicalrecord.setMedications(medicalrecordToUpdate.getMedications());
			medicalrecord.setAllergies(medicalrecordToUpdate.getAllergies());
			logger.info("Medical record updated: firstname ={} and lastname = {}", medicalrecordToUpdate.getFirstName(),
					medicalrecordToUpdate.getLastName());
			saveDataJSON.saveData();
			return medicalrecord;
		} catch (MedicalRecordNotFoundException e) {
			logger.error("A medical record with firstname ={} and lastname = {} not found ",
					medicalrecordToUpdate.getFirstName(), medicalrecordToUpdate.getLastName());
			return null;
		}

	}

	/** Endpoint de DELETE /medicalRecord **/
	public MedicalRecord deleteByFirstNameAndLastName(String fn, String ln) {
		try {
			MedicalRecord medicalrecordToDelete = findByFirstNameAndLastName(fn, ln);
			LoadDataJSON.listMedicalrecords.remove(medicalrecordToDelete);
			saveDataJSON.saveData();
			logger.info("Medical record deleted: firstname ={} and lastname = {}", medicalrecordToDelete.getFirstName(),
					medicalrecordToDelete.getLastName());
			return medicalrecordToDelete;
		} catch (MedicalRecordNotFoundException e) {
			logger.error("A medical record with firstname ={} and lastname = {} not found ", fn, ln);
			return null;
		}
	}

	public List<MedicalRecord> findAll() {
		return LoadDataJSON.listMedicalrecords;

	}

	/** Find medical records by firstname and lastname */
	public MedicalRecord findByFirstNameAndLastName(String fn, String ln) {
		MedicalRecord medicalRecordFound = null;
		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			if (m.getFirstName().equals(fn) && m.getLastName().equals(ln)) {
				logger.info("Medical record found: firstname ={} and lastname = {}", fn, ln);
				medicalRecordFound = m;
			}
		}
		if (medicalRecordFound != null) {
			logger.info("Person found: firstname ={} and lastename = {} ", fn, ln);
			return medicalRecordFound;
		} else {
			throw new MedicalRecordNotFoundException(
					"medicalRecord not found with firstname= " + fn + " and lastName=" + ln);
		}
	}
}
