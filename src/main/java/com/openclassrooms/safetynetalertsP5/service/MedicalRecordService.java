package com.openclassrooms.safetynetalertsP5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {
	@Autowired
	private MedicalRecordRepository medicalrecordRepository;

	/** Endpoint de POST /medicalRecord **/
	public MedicalRecord addMedicalRecord(MedicalRecord medicalrecord) {
		return medicalrecordRepository.save(medicalrecord);

	}

	/** Endpoint de PUT /medicalRecord **/
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalrecord) {
		return medicalrecordRepository.updateByFirstNameAndLastName(medicalrecord);

	}

	/**
	 * Endpoint de DELETE /medicalRecord
	 * 
	 * @return
	 **/
	public MedicalRecord deleteMedicalRecord(String fn, String ln) {
		return medicalrecordRepository.deleteByFirstNameAndLastName(fn, ln);

	}

	/** Endpoint de GET /medicalRecord **/
	public List<MedicalRecord> getAllMedicalRecords() {
		return medicalrecordRepository.findAll();
	}

	/** Endpoint de GET /medicalRecord **/
	public MedicalRecord getMedicalRecord(String fn, String ln) {
		return medicalrecordRepository.findByFirstNameAndLastName(fn, ln);
	}
}
