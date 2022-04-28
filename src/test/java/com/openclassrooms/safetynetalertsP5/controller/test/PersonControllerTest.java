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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynetalertsP5.controller.PersonController;
import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.service.PersonService;

@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PersonService personService;

	private List<Person> persons;
	private ObjectMapper mapper;
	private Person person;

	@BeforeEach
	public void setUp() {
		person = new Person("Jules", "Dupont", "st Germain", "paris", "97451", "0666777", "jules@email.com");
		mapper = new ObjectMapper();

	}

	@Test
	public void testGetAllPerson_success() throws Exception {
		Person person1 = new Person("Toto", "Bentiti", "st Germain", "paris", "97451", "0666777", "toto@email.com");
		Person person2 = new Person("leo", "mini", "st Germain", "paris", "97451", "0666777", "leo@email.com");
		Person person3 = new Person("Jane ", "Doe", "st Germain", "paris", "97451", "0666777", "jane@email.com");
		persons = Arrays.asList(person1, person2, person3);
		// Given
		Mockito.when(personService.getAllPersons()).thenReturn(persons);
		// When
		MvcResult mvcResult = mockMvc.perform(get("/Persons").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Person[] personlist = mapper.readValue(content, Person[].class);
		assertTrue(personlist.length == 3);

	}

	@Test
	public void testGetAllPerson_NotFound() throws Exception {
		// Given
		Mockito.when(personService.getAllPersons()).thenReturn(null);
		// When
		mockMvc.perform(get("/Persons").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals("Persons Not found", result.getResolvedException().getMessage()));

	}

	@Test
	public void testGetPerson_success() throws Exception {

		// Given
		Mockito.when(personService.getPerson("Jules", "Dupont")).thenReturn(person);
		// When
		MvcResult mvcResult = mockMvc.perform(get("/Persons/Jules/Dupont").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Person onePerson = mapper.readValue(content, Person.class);
		assertTrue(onePerson.getFirstName().equals("Jules"));

	}

	@Test
	public void testGetPerson_personNotFound() throws Exception {

		// Given
		Mockito.when(personService.getPerson("Jules", "Dupont")).thenReturn(null);
		// When
		mockMvc.perform(get("/Persons/Jules/Dupont").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException)).andExpect(
						result -> assertEquals(
								"Person with firstname= " + person.getFirstName() + " and lastName="
										+ person.getLastName() + " does not exist.",
								result.getResolvedException().getMessage()));

	}

	@Test
	public void testCreatePerson_success() throws Exception {

		Mockito.when(personService.addPerson(any(Person.class))).thenReturn(person);
		String inputJson = mapper.writeValueAsString(person);
		mockMvc.perform(MockMvcRequestBuilders.post("/Persons").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/Persons/Jules/Dupont"));

	}

	@Test
	public void testCreatePerson_Failed() throws Exception {

		Mockito.when(personService.addPerson(any(Person.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(person);
		mockMvc.perform(MockMvcRequestBuilders.post("/Persons").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson).characterEncoding("utf-8").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	@Test
	public void testUpdatePerson_success() throws Exception {

		Mockito.when(personService.updatePerson(any(Person.class))).thenReturn(person);
		String inputJson = mapper.writeValueAsString(person);
		mockMvc.perform(MockMvcRequestBuilders.put("/Persons").param("FirstName", "Jules").param("LastName", "Dupont")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string(inputJson));

	}

	@Test
	public void testUpdatePerson_personNotFound() throws Exception {

		Mockito.when(personService.updatePerson(any(Person.class))).thenReturn(null);
		String inputJson = mapper.writeValueAsString(person);
		mockMvc.perform(MockMvcRequestBuilders.put("/Persons").param("FirstName", "Jules").param("LastName", "Dupont")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).characterEncoding("utf-8")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException)).andExpect(
						result -> assertEquals(
								"Person with firstname= " + person.getFirstName() + " and lastName="
										+ person.getLastName() + " does not exist.",
								result.getResolvedException().getMessage()));
	}

	@Test
	public void testDeleteProduct_success() throws Exception {

		Mockito.when(personService.deletePerson("Jules", "Dupont")).thenReturn(person);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/Persons").param("FirstName", "Jules").param("LastName", "Dupont")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void testDeletePerson_personNotFound() throws Exception {

		Mockito.when(personService.deletePerson("Jules", "Dupont")).thenReturn(null);

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/Persons").param("FirstName", "Jules").param("LastName", "Dupont")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException)).andExpect(
						result -> assertEquals(
								"Person with firstname= " + person.getFirstName() + " and lastName="
										+ person.getLastName() + " does not exist.",
								result.getResolvedException().getMessage()));
	}
}
