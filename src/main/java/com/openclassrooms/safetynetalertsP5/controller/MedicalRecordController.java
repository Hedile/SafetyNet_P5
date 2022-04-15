package com.openclassrooms.safetynetalertsP5.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger("MedicalRecordController");

	public MedicalRecordController(MedicalRecordService medicalrecordService) {
		this.medicalrecordService = medicalrecordService;
	}

	/** Endpoint Get /medicalrecord **/
	@GetMapping("/medicalrecords")
	public List<MedicalRecord> getAllMedicalRecords() {
		logger.info("Get/medicalrecords");
		List<MedicalRecord> medicalRecords = medicalrecordService.getAllMedicalRecords();
		if (medicalRecords != null) {
			logger.info("Get ok");
			return medicalRecords;
		} else {
			logger.error("MedicalRecords Not found");
			return null;
		}
	}

	@GetMapping(value = "/medicalrecords/{FirstName}/{LastName}")
	public ResponseEntity<MedicalRecord> getMedicalRecordByFirstName(@PathVariable("FirstName") String fn,
			@PathVariable("LastName") String ln) {
		MedicalRecord medicalRecord = medicalrecordService.getMedicalRecord(fn, ln);
		if (medicalRecord != null) {
			logger.info("Get ok");
			return ResponseEntity.ok().body(medicalRecord);
		} else {
			logger.error("MedicalRecord Not found");
			return ResponseEntity.notFound().build();
		}

	}

	/** Endpoint POST /medicalrecord **/
	@PostMapping(value = "/medicalrecords")
	public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalrecord) {
		MedicalRecord addedMedicalRecord = medicalrecordService.addMedicalRecord(medicalrecord);
		if (Objects.isNull(addedMedicalRecord)) {
			logger.error("Medical record is not created");
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{FirstName}/{LastName}")
				.buildAndExpand(addedMedicalRecord.getFirstName(), addedMedicalRecord.getLastName()).toUri();
		logger.info("Post OK");
		return ResponseEntity.created(location).build();

	}

	/** Endpoint PUT /medicalrecord **/

	@PutMapping(value = "/medicalrecords")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord m,
			@RequestParam("FirstName") String fn, @RequestParam("LastName") String ln) {
		MedicalRecord updatedMedicalRecord = medicalrecordService.updateMedicalRecord(m);
		if (updatedMedicalRecord != null) {
			logger.info("Put OK");

			return ResponseEntity.ok(updatedMedicalRecord);
		} else {
			logger.error("No update");
			return ResponseEntity.notFound().build();
		}
	}

	/** Endpoint DELETE /medicalrecord **/
	@DeleteMapping(value = "/medicalrecords")
	public Map<String, Boolean> deletePerson(@RequestParam("FirstName") String fn,
			@RequestParam("LastName") String ln) {
		MedicalRecord deletedMedicalRecord = medicalrecordService.deleteMedicalRecord(fn, ln);
		Map<String, Boolean> response = new HashMap<>();
		if (deletedMedicalRecord != null) {
			response.put("deleted", Boolean.TRUE);
			logger.info("Successful deletion");
			return response;
		} else {
			logger.error("Deletion failed");
			ResponseEntity.notFound().build();
			response.put("deleted", Boolean.FALSE);
			return response;
		}
	}

}
