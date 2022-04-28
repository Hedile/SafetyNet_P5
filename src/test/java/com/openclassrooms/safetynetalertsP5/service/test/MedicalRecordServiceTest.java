package com.openclassrooms.safetynetalertsP5.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.repository.MedicalRecordRepository;
import com.openclassrooms.safetynetalertsP5.service.MedicalRecordService;

@WebMvcTest(MedicalRecordService.class)
@AutoConfigureMockMvc
public class MedicalRecordServiceTest {
	@Autowired
	private MedicalRecordService medicalrecordService;
	@MockBean
	private MedicalRecordRepository medicalrecordRepository;
	private MedicalRecord medicalrecord;
	private List<String> medications;
	private List<String> allergies;

	@BeforeEach
	public void setup() {
		medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
		allergies = Arrays.asList("nillacilan");
		medicalrecord = new MedicalRecord("Jules", "Dupont", "03/06/1984", medications, allergies);
	}

	@Test
	public void testFindAllMedicalRecord() throws Exception {
		List<MedicalRecord> allmedicalrecords = Arrays.asList(medicalrecord);
		Mockito.when(medicalrecordRepository.findAll()).thenReturn(allmedicalrecords);
		List<MedicalRecord> medicalrecords = medicalrecordService.getAllMedicalRecords();
		assertNotNull(medicalrecords);
		assertEquals(medicalrecords, allmedicalrecords);
		assertEquals(medicalrecords.size(), allmedicalrecords.size());
		verify(medicalrecordRepository).findAll();
	}

	@Test
	public void testFindMedicalRecordByFirstNameAndLastName() {

		Mockito.when(medicalrecordRepository.findByFirstNameAndLastName(medicalrecord.getFirstName(),
				medicalrecord.getLastName()))
				.thenReturn((medicalrecord));
		MedicalRecord medicalrecordFromDB = medicalrecordService.getMedicalRecord(medicalrecord.getFirstName(),
				medicalrecord.getLastName());
		assertNotNull(medicalrecordFromDB);
		assertEquals(medicalrecordFromDB.getFirstName(), (medicalrecord.getFirstName()));
		verify(medicalrecordRepository).findByFirstNameAndLastName(any(String.class), any(String.class));
	}

	@Test
	public void testSaveMedicalRecord() throws Exception {
		Mockito.when(medicalrecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalrecord);
		MedicalRecord medicalrecordSaved = medicalrecordService.addMedicalRecord(medicalrecord);
		assertNotNull(medicalrecordSaved);
		assertEquals(medicalrecord.getFirstName(), medicalrecordSaved.getFirstName());
		assertEquals(medicalrecord.getLastName(), medicalrecordSaved.getLastName());
		verify(medicalrecordRepository).save(any(MedicalRecord.class));
	}

	@Test
	public void testUpdateMedicalRecord() throws Exception {

		Mockito.when(medicalrecordRepository.updateMedicalRecordByFirstNameAndLastName(any(MedicalRecord.class)))
				.thenReturn(medicalrecord);
		MedicalRecord medicalrecordFromDB = medicalrecordService.updateMedicalRecord(medicalrecord);
		assertNotNull(medicalrecordFromDB);
		assertEquals(medicalrecord.getFirstName(), medicalrecordFromDB.getFirstName());
		assertEquals(medicalrecord.getLastName(), medicalrecordFromDB.getLastName());

	}

	@Test
	public void testDeletePerson() throws Exception {

		Mockito.when(
				medicalrecordRepository.deleteMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class)))
				.thenReturn(medicalrecord);
		MedicalRecord medicalrecordDeleted = medicalrecordService.deleteMedicalRecord(medicalrecord.getFirstName(),
				medicalrecord.getLastName());
		assertNotNull(medicalrecordDeleted);
		assertEquals(medicalrecord.getFirstName(), medicalrecordDeleted.getFirstName());
		assertEquals(medicalrecord.getLastName(), medicalrecordDeleted.getLastName());
		verify(medicalrecordRepository).deleteMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class));
	}
}
