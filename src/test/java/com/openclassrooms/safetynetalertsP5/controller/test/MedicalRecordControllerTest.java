package com.openclassrooms.safetynetalertsP5.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynetalertsP5.controller.MedicalRecordController;
import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.MedicalRecord;
import com.openclassrooms.safetynetalertsP5.service.MedicalRecordService;

@RunWith(SpringRunner.class)
@WebMvcTest(MedicalRecordController.class)
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MedicalRecordService medicalrecordService;

	private List<MedicalRecord> medicalrecords;
	private ObjectMapper mapper;
	private MedicalRecord medicalrecord1;
	private MedicalRecord medicalrecord2;
	private List<String> medications;
	private List<String> allergies;

	@Before
	public void setUp() {
		medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
		allergies = Arrays.asList("nillacilan");

		medicalrecord1 = new MedicalRecord("Jules", "Dupont", "03/06/1984", medications, allergies);
		medicalrecord2 = new MedicalRecord("Jane", "castex", "01/01/1980", medications, allergies);
		mapper = new ObjectMapper();

	}

	@Test
	public void testGetAllMedicalRecord_success() throws Exception {
		medicalrecords = Arrays.asList(medicalrecord1, medicalrecord2);
		// Given
		Mockito.when(medicalrecordService.getAllMedicalRecords()).thenReturn(medicalrecords);
		// When
		MvcResult mvcResult = mockMvc.perform(get("/medicalrecords").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		// then
		String content = mvcResult.getResponse().getContentAsString();
		MedicalRecord[] medicalrecordlist = mapper.readValue(content, MedicalRecord[].class);
		assertTrue(medicalrecordlist.length == 2);
	}

	@Test
	public void testGetAllMedicalRecord_NotFound() throws Exception {

		Mockito.when(medicalrecordService.getAllMedicalRecords()).thenReturn(null);
		mockMvc.perform(get("/medicalrecords").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testGetMedicalrecord_success() throws Exception {

		// Given
		Mockito.when(medicalrecordService.getMedicalRecord("Jules", "Dupont")).thenReturn(medicalrecord1);
		// When
		MvcResult mvcResult = mockMvc
				.perform(get("/medicalrecords/Jules/Dupont").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		MedicalRecord medical = mapper.readValue(content, MedicalRecord.class);
		assertTrue(medical.getFirstName().equals("Jules"));

	}

	@Test
	public void testGetMedicalrecord_NotFound() throws Exception {

		// Given
		Mockito.when(medicalrecordService.getMedicalRecord("Jules", "Dupont")).thenReturn(null);
		// When
		mockMvc.perform(get("/medicalrecords/Jules/Dupont").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals(
						"MedicalRecord with firstname= " + medicalrecord1.getFirstName() + " and lastName="
								+ medicalrecord1.getLastName() + " does not exist.",
						result.getResolvedException().getMessage()));

	}

	@Test
	public void testCreateMedicalRecord_success() throws Exception {

		Mockito.when(medicalrecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalrecord1);
		String inputJson = mapper.writeValueAsString(medicalrecord1);
		mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/medicalrecords/Jules/Dupont"));

	}

	@Test
	public void testCreateMedicalRecord_Failed() throws Exception {

		Mockito.when(medicalrecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(medicalrecord1);
		mockMvc.perform(MockMvcRequestBuilders.post("/medicalrecords").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	public void testUpdateMedicalRecord_success() throws Exception {

		Mockito.when(medicalrecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalrecord1);
		String inputJson = mapper.writeValueAsString(medicalrecord1);
		mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords").param("FirstName", "Jules")
				.param("LastName", "Dupont").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)
				.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(inputJson));

	}

	@Test
	public void testUpdateMedicalrecord_NotFound() throws Exception {

		Mockito.when(medicalrecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(medicalrecord1);
		mockMvc.perform(MockMvcRequestBuilders.put("/medicalrecords").param("FirstName", "Jules")
				.param("LastName", "Dupont").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)
				.characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals(
						"MedicalRecord with firstname= " + medicalrecord1.getFirstName() + " and lastName="
								+ medicalrecord1.getLastName() + " does not exist.",
						result.getResolvedException().getMessage()));
	}

	@Test
	public void testDeleteMedicalrecord_success() throws Exception {

		Mockito.when(medicalrecordService.deleteMedicalRecord("Jules", "Dupont")).thenReturn(medicalrecord1);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/medicalrecords").param("FirstName", "Jules").param("LastName", "Dupont")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void testDeleteMedicalrecordn_NotFound() throws Exception {

		Mockito.when(medicalrecordService.deleteMedicalRecord("Jules", "Dupont")).thenReturn(null);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/medicalrecords").param("FirstName", "Jules").param("LastName", "Dupont")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals(
						"MedicalRecord with firstname= " + medicalrecord1.getFirstName() + " and lastName="
								+ medicalrecord1.getLastName() + " does not exist.",
						result.getResolvedException().getMessage()));
	}
}
