package com.openclassrooms.safetynetalertsP5.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynetalertsP5.controller.FireStationController;
import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.service.FireStationService;

@RunWith(SpringRunner.class)
@WebMvcTest(FireStationController.class)
@AutoConfigureMockMvc
public class FireStationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FireStationService firestationService;

	private ObjectMapper mapper;

	@Before
	public void setUp() {

		mapper = new ObjectMapper();

	}

	@Test
	public void testCreateFireStation_success() throws Exception {
		// Given
		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.addFireStation(any(FireStation.class))).thenReturn(firestation);
		String inputJson = mapper.writeValueAsString(firestation);
		// When
		mockMvc.perform(MockMvcRequestBuilders.post("/Firestations").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/Firestations/St_Germain/5"));

	}

	@Test
	public void testCreateFireStation_Failed() throws Exception {
		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.addFireStation(any(FireStation.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(firestation);
		mockMvc.perform(MockMvcRequestBuilders.post("/Firestations").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	public void testUpdateFirestation_success() throws Exception {
		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.updateFireStation(any(FireStation.class))).thenReturn(firestation);
		String inputJson = mapper.writeValueAsString(firestation);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/Firestations/St_Germain").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(inputJson));
	}

	@Test
	public void testUpdatefirestation_firestationNotFound() throws Exception {
		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.updateFireStation(any(FireStation.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(firestation);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/Firestations/St_Germain").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals(
						"FireStation with address=" + firestation.getAddress() + " does not exist.",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void testDeleteFirestation_success() throws Exception {

		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.deleteFireStation("St_Germain")).thenReturn(firestation);
		String inputJson = mapper.writeValueAsString(firestation);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/Firestations/St_Germain").content(inputJson).characterEncoding("utf-8")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void testDeleteFirestation_firestationNotFound() throws Exception {
		FireStation firestation = new FireStation("St_Germain", "5");
		Mockito.when(firestationService.deleteFireStation("St_Germain")).thenReturn(null);
		String inputJson = mapper.writeValueAsString(firestation);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/Firestations/St_Germain").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals(
						"FireStation  with address=" + firestation.getAddress() + " does not exist.",
						result.getResolvedException().getMessage()));

	}
}
