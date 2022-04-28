package com.openclassrooms.safetynetalertsP5.repository.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.safetynetalertsP5.LoadDataJSON;
import com.openclassrooms.safetynetalertsP5.SaveDataJSON;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.repository.MedicalRecordRepository;

@WebMvcTest(MedicalRecordRepository.class)
@AutoConfigureMockMvc
public class MedicalRecordRepositoryTest {
	@Autowired
	private MedicalRecordRepository medicalrecordRepository;
	@MockBean
	private SaveDataJSON saveDataJSON;

	private ArrayList<MedicalRecord> medicalrecords = new ArrayList<MedicalRecord>();
	private List<String> medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
	private List<String> allergies = Arrays.asList("nillacilan");

	private MedicalRecord medicalrecord1 = new MedicalRecord("Jules", "Dupont", "03/06/1984", medications, allergies);
	private MedicalRecord medicalrecord2 = new MedicalRecord("Jane", "castex", "01/01/1980", medications, allergies);
	private MedicalRecord medicalrecord3 = new MedicalRecord("Toto", "Bentiti", "02/06/1998", medications, allergies);
	private MedicalRecord medicalrecordToUpdate = new MedicalRecord("Jules", "Dupont", "01/01/1980", medications,
			allergies);

	@BeforeEach
	public void setup() {
		medicalrecords.add(medicalrecord1);
		medicalrecords.add(medicalrecord2);
		LoadDataJSON.listMedicalrecords = medicalrecords;
	}

	@AfterEach
	public void cleanUpEach() {
		LoadDataJSON.listMedicalrecords = null;
	}

	@Test
	public void testFindAllMedicalRecord() throws Exception {
		List<MedicalRecord> ListMedicalrecord = medicalrecordRepository.findAll();
		assertEquals(2, medicalrecords.size());
		assertEquals(medicalrecords, ListMedicalrecord);
	}

	@Test
	public void testFindMedicalRecordByFirstNameAndLastName() throws Exception {
		MedicalRecord m = medicalrecordRepository.findByFirstNameAndLastName(medicalrecord2.getFirstName(),
				medicalrecord2.getLastName());
		assertEquals(2, medicalrecords.size());
		assertEquals(medicalrecord2.getFirstName(), m.getFirstName());
		assertThat(medicalrecords).contains(medicalrecord2);
	}

	@Test
	public void testSaveMedicalRecord() throws Exception {
		medicalrecordRepository.save(medicalrecord3);
		assertEquals(3, medicalrecords.size());
		assertEquals(medicalrecords.get(2).getFirstName(), medicalrecord3.getFirstName());
		assertThat(medicalrecords).contains(medicalrecord3);
	}

	@Test
	public void testSaveMedicalRecord_Existing() throws Exception {
		medicalrecordRepository.save(medicalrecord1);
		assertEquals(2, medicalrecords.size());
	}

	@Test
	public void testUpdateMedicalRecord() throws Exception {
		medicalrecordRepository.updateMedicalRecordByFirstNameAndLastName(medicalrecordToUpdate);
		assertEquals("01/01/1980", medicalrecords.get(0).getBirthdate());
	}

	@Test
	public void testUpdateMedicalRecord_NotFound() throws Exception {
		medicalrecordRepository.updateMedicalRecordByFirstNameAndLastName(medicalrecord3);
		assertThat(medicalrecords).doesNotContain(medicalrecord3);
	}

	@Test
	public void testDeleteMedicalRecord() throws Exception {
		medicalrecordRepository.deleteMedicalRecordByFirstNameAndLastName(medicalrecord1.getFirstName(),
				medicalrecord1.getLastName());
		assertEquals(1, medicalrecords.size());
		assertThat(medicalrecords).doesNotContain(medicalrecord1);
	}

	@Test
	public void testDeleteMedicalRecord_NotFound() throws Exception {
		medicalrecordRepository.deleteMedicalRecordByFirstNameAndLastName(medicalrecord3.getFirstName(),
				medicalrecord3.getLastName());
		assertEquals(2, medicalrecords.size());
		assertThat(medicalrecords).doesNotContain(medicalrecord3);
	}
}
