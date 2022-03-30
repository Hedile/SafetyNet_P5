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

	public MedicalRecord addMedicalRecord(MedicalRecord medicalrecord) {
		return medicalrecordRepository.save(medicalrecord);

	}

	public MedicalRecord updateMedicalRecord(MedicalRecord medicalrecord) {
		return medicalrecordRepository.updateByFirstNameAndLastName(medicalrecord);

	}

	public void deleteMedicalRecord(String fn, String ln) {
		medicalrecordRepository.deleteByFirstNameAndLastName(fn, ln);

	}

	public List<MedicalRecord> getAllMedicalRecords() {
		return medicalrecordRepository.findAll();
	}

	public MedicalRecord getMedicalRecord(String mr) {
		return medicalrecordRepository.findByFirstName(mr);
	}
}
