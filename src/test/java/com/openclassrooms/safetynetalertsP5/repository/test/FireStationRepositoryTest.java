package com.openclassrooms.safetynetalertsP5.repository.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.repository.FireStationRepository;

@WebMvcTest(FireStationRepository.class)
@AutoConfigureMockMvc
public class FireStationRepositoryTest {
	@Autowired
	private FireStationRepository firestationRepository;
	@MockBean
	private SaveDataJSON saveDataJSON;

	private ArrayList<FireStation> firestations = new ArrayList<FireStation>();

	FireStation firestation1 = new FireStation("St_Germain", "5");
	FireStation firestation2 = new FireStation("Paris", "2");
	FireStation firestation3 = new FireStation("Chambourcy", "3");
	FireStation firestationToUpdate = new FireStation("St_Germain", "10");

	@BeforeEach
	public void setup() {
		firestations.add(firestation1);
		firestations.add(firestation2);
		LoadDataJSON.listFirestations = firestations;
	}

	@AfterEach
	public void cleanUpEach() {
		LoadDataJSON.listFirestations = null;
	}

	@Test
	public void testFindAllFireStations() throws Exception {

		List<FireStation> fires = firestationRepository.findAll();
		assertEquals(2, firestations.size());
		assertEquals(firestations, fires);

	}

	@Test
	public void testFindFirestationByAddress() throws Exception {
		FireStation f = firestationRepository.findByAddress(firestation2.getAddress());
		assertEquals(2, firestations.size());
		assertEquals(firestation2.getAddress(), f.getAddress());
		assertThat(firestations).contains(firestation2);

	}

	@Test
	public void testSaveFireStation() throws Exception {
		firestationRepository.save(firestation3);
		assertEquals(3, firestations.size());
		assertEquals(firestations.get(2).getAddress(), firestation3.getAddress());
		assertThat(firestations).contains(firestation3);

	}

	@Test
	public void testSaveFireStation_Existing() throws Exception {
		firestationRepository.save(firestation1);
		assertEquals(2, firestations.size());

	}

	@Test
	public void testUpdateFirestationByAddress() throws Exception {

		firestationRepository.updateByAddress(firestationToUpdate);
		assertEquals("St_Germain", firestations.get(0).getAddress());
		assertEquals("10", firestations.get(0).getStation());

	}

	@Test
	public void testUpdateFirestationByAddress_NotFound() throws Exception {

		firestationRepository.updateByAddress(firestation3);
		assertThat(firestations).doesNotContain(firestation3);

	}

	@Test
	public void testDeleteFireStation() throws Exception {

		firestationRepository.deleteByAddress(firestation1.getAddress());
		assertEquals(1, firestations.size());
		assertThat(firestations).doesNotContain(firestation1);
	}

	@Test
	public void testDeleteFireStation_NotFound() throws Exception {

		firestationRepository.deleteByAddress(firestation3.getAddress());
		assertEquals(2, firestations.size());
		assertThat(firestations).doesNotContain(firestation3);

	}
}
