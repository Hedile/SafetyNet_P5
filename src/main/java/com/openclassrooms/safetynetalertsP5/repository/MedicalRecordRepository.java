package com.openclassrooms.safetynetalertsP5.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;

@Repository
public class MedicalRecordRepository {

	@Autowired
	public SaveDataJSON saveDataJSON;

	public MedicalRecord findByFirstName(String fn) {
		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			if (m.getFirstName().equals(fn)) {
				return m;
			}
		}
		return null;
	}

	public MedicalRecord updateByFirstNameAndLastName(MedicalRecord medicalrecordToUpdate) {

		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			if (m.getFirstName().equals(medicalrecordToUpdate.getFirstName())
					&& m.getLastName().equals(medicalrecordToUpdate.getLastName())) {
				m.setBirthdate(medicalrecordToUpdate.getBirthdate());
				m.setMedications(medicalrecordToUpdate.getMedications());
				m.setAllergies(medicalrecordToUpdate.getAllergies());
				saveDataJSON.saveData();
				return m;
			}
		}
		return null;
	}

	public MedicalRecord deleteByFirstNameAndLastName(String fn, String ln) {
		MedicalRecord medicalrecordToDelete = new MedicalRecord();
		for (MedicalRecord m : LoadDataJSON.listMedicalrecords) {
			if (m.getFirstName().equals(fn) && m.getLastName().equals(ln)) {
				medicalrecordToDelete = m;
			}
		}
		LoadDataJSON.listMedicalrecords.remove(medicalrecordToDelete);
		saveDataJSON.saveData();
		return medicalrecordToDelete;

	}

	public List<MedicalRecord> findAll() {
		return LoadDataJSON.listMedicalrecords;

	}

	public MedicalRecord save(MedicalRecord medicalrecord) {
		LoadDataJSON.listMedicalrecords.add(medicalrecord);
		saveDataJSON.saveData();
		return medicalrecord;

	}

}
