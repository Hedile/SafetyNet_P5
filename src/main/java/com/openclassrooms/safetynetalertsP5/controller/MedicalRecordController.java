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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.service.MedicalRecordService;

@RestController
public class MedicalRecordController {
	@Autowired
	private MedicalRecordService medicalrecordService;

	public MedicalRecordController(MedicalRecordService medicalrecordService) {
		this.medicalrecordService = medicalrecordService;
	}

	/** Endpoint Get /medicalrecord **/
	@GetMapping("/medicalrecords")
	public List<MedicalRecord> getAllMedicalRecords() {
		return medicalrecordService.getAllMedicalRecords();
	}

	@GetMapping(value = "/medicalrecords/{FirstName}")
	public MedicalRecord getMedicalRecordByFirstName(@PathVariable("FirstName") String fn) {
		return medicalrecordService.getMedicalRecord(fn);

	}

	/** Endpoint POST /medicalrecord **/
	@PostMapping(value = "/medicalrecords")
	public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalrecord) {
		MedicalRecord addedMedicalRecord = medicalrecordService.addMedicalRecord(medicalrecord);
		if (Objects.isNull(addedMedicalRecord)) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{FirstName}/{LastName}")
				.buildAndExpand(addedMedicalRecord.getFirstName(), addedMedicalRecord.getLastName()).toUri();
		return ResponseEntity.created(location).build();

	}

	/** Endpoint PUT /medicalrecord **/

	@PutMapping(value = "/medicalrecords")
	public void updateMedicalRecord(@RequestBody MedicalRecord m, @RequestParam("FirstName") String fn,
			@RequestParam("LastName") String ln) {
		medicalrecordService.updateMedicalRecord(m);
	}

	/** Endpoint DELETE /medicalrecord **/
	@DeleteMapping(value = "/medicalrecords")
	public void deletePerson(@RequestParam("FirstName") String fn, @RequestParam("LastName") String ln) {
		medicalrecordService.deleteMedicalRecord(fn, ln);
	}

}
