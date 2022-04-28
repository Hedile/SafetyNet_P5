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

import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.repository.FireStationRepository;
import com.openclassrooms.safetynetalertsP5.service.FireStationService;

@WebMvcTest(FireStationService.class)
@AutoConfigureMockMvc
public class FireStationServiceTest {
	@Autowired
	private FireStationService firestationService;
	@MockBean
	private FireStationRepository firestationRepository;
	private FireStation firestation;

	@BeforeEach
	public void setup() {
		firestation = new FireStation("St_Germain", "5");
	}

	@Test
	public void testFindAllFireStation() throws Exception {
		List<FireStation> allfirestations = Arrays.asList(firestation);
		Mockito.when(firestationRepository.findAll()).thenReturn(allfirestations);
		List<FireStation> firestations = firestationService.getAllFireStations();
		assertNotNull(firestations);
		assertEquals(firestations, allfirestations);
		assertEquals(firestations.size(), allfirestations.size());
		verify(firestationRepository).findAll();
	}

	@Test
	public void testFindFireStationByAddress() {

		Mockito.when(firestationRepository.findByAddress(firestation.getAddress()))
				.thenReturn((firestation));
		FireStation firestationFromDB = firestationService.getFireStation(firestation.getAddress());
		assertNotNull(firestationFromDB);
		assertEquals(firestationFromDB.getAddress(), (firestation.getAddress()));
		verify(firestationRepository).findByAddress(any(String.class));
	}

	@Test
	public void testSaveFireStation() throws Exception {
		Mockito.when(firestationRepository.save(any(FireStation.class))).thenReturn(firestation);
		FireStation firestationSaved = firestationService.addFireStation(firestation);
		assertNotNull(firestationSaved);
		assertEquals(firestation.getAddress(), firestationSaved.getAddress());
		assertEquals(firestation.getStation(), firestationSaved.getStation());
		verify(firestationRepository).save(any(FireStation.class));
	}

	@Test
	public void testUpdateFireStation() throws Exception {

		Mockito.when(firestationRepository.updateByAddress(any(FireStation.class))).thenReturn(firestation);
		FireStation firestationFromDB = firestationService.updateFireStation(firestation);
		assertNotNull(firestationFromDB);
		assertEquals(firestation.getAddress(), firestationFromDB.getAddress());
		assertEquals(firestation.getStation(), firestationFromDB.getStation());

	}

	@Test
	public void testDeleteFireStation() throws Exception {

		Mockito.when(firestationRepository.deleteByAddress(any(String.class)))
				.thenReturn(firestation);
		FireStation firestationDeleted = firestationService.deleteFireStation(firestation.getAddress());
		assertNotNull(firestationDeleted);
		assertEquals(firestation.getAddress(), firestationDeleted.getAddress());
		assertEquals(firestation.getStation(), firestationDeleted.getStation());
		verify(firestationRepository).deleteByAddress(any(String.class));
	}
}
