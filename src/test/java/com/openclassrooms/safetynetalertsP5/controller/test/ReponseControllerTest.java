package com.openclassrooms.safetynetalertsP5.controller.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetynetalertsP5.controller.ReponseController;
import com.openclassrooms.safetynetalertsP5.dto.Child;
import com.openclassrooms.safetynetalertsP5.dto.Children;
import com.openclassrooms.safetynetalertsP5.dto.Family;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfo;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfoGeneral;
import com.openclassrooms.safetynetalertsP5.dto.PersonInfoMedical;
import com.openclassrooms.safetynetalertsP5.dto.PersonsByStationNumber;
import com.openclassrooms.safetynetalertsP5.dto.PersonsServedByFireStation;
import com.openclassrooms.safetynetalertsP5.exceptions.NotFoundException;
import com.openclassrooms.safetynetalertsP5.model.FireStation;
import com.openclassrooms.safetynetalertsP5.model.Person;
import com.openclassrooms.safetynetalertsP5.service.ReponseService;

@RunWith(SpringRunner.class)
@WebMvcTest(ReponseController.class)
@AutoConfigureMockMvc
public class ReponseControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReponseService reponseService;

	private ObjectMapper mapper;
	FireStation firestation = new FireStation("St_Germain", "5");

	@Before
	public void setUp() {

		mapper = new ObjectMapper();

	}

	/** Endpoint de firestation?stationNumber=<station_number **/
	@Test
	public void testGetPeopleCoveredByStationNumber_success() throws Exception {
		PersonInfoGeneral person1 = new PersonInfoGeneral("Toto", "Bentiti", "st Germain", "0666777");
		PersonInfoGeneral person2 = new PersonInfoGeneral("leo", "mini", "st Germain", "0666777");
		PersonInfoGeneral person3 = new PersonInfoGeneral("Jane ", "Doe", "st Germain", "0666777");
		List<PersonInfoGeneral> persons = Arrays.asList(person1, person2, person3);
		PersonsByStationNumber people = new PersonsByStationNumber(persons, 1, 2);
		Mockito.when(reponseService.getPersonsByStationNumber(firestation.getStation())).thenReturn(people);
		MvcResult mvcResult = mockMvc.perform(
				get("/Firestations").param("station", firestation.getStation()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		PersonsByStationNumber personlist = mapper.readValue(content, PersonsByStationNumber.class);
		assertTrue(personlist.getPersonsCoveredByFireStation().get(0).getFirstName().equals("Toto"));

	}

	@Test
	public void testGetPeopleCoveredByStationNumber_PeopleNotFound() throws Exception {
		Mockito.when(reponseService.getPersonsByStationNumber(firestation.getStation())).thenReturn(null);
		mockMvc.perform(
				get("/Firestations").param("station", firestation.getStation()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
				.andExpect(result -> assertEquals("No people covered by the fireStation n°:" + firestation.getStation(),
						result.getResolvedException().getMessage()));

	}

	/** Endpoint de childAlert?address=<address> **/
	@Test
	public void testGetChildrenByAddress_success() throws Exception {
		Person mom = new Person("Jules", "Dupont", "st Germain", "paris", "97451", "0666111", "jules@email.com");
		Person dad = new Person("Jane", "Dupont", "st Germain", "paris", "97451", "0666777", "jane@email.com");
		List<Person> childsFamily = Arrays.asList(mom, dad);
		Child child1 = new Child("leo", "Dupont", 8, childsFamily);
		Child child2 = new Child("lily", "Dupont", 12, childsFamily);
		List<Child> enfants = Arrays.asList(child1, child2);
		Children children = new Children(enfants);
		Mockito.when(reponseService.childrenByAddress(firestation.getAddress())).thenReturn(children);
		MvcResult mvcResult = mockMvc.perform(
				get("/childAlert").param("address", firestation.getAddress()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Children childlist = mapper.readValue(content, Children.class);
		assertTrue(childlist.getChidren().get(0).getFirstName().equals("leo"));

	}

	@Test
	public void testGetChildrenByAddress_childrenNotFound() throws Exception {
		Mockito.when(reponseService.childrenByAddress(firestation.getAddress())).thenReturn(null);
		mockMvc.perform(
				get("/childAlert").param("address", firestation.getAddress()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	/** Endpoint de phoneAlert?firestation=<firestation_number> **/
	@Test
	public void testGetPhoneByStation_success() throws Exception {
		List<String> phoneByStation = Arrays.asList("064567234", "0784566765", "0923445667");
		Mockito.when(reponseService.getPhoneNumberByStation(firestation.getStation())).thenReturn(phoneByStation);
		MvcResult mvcResult = mockMvc.perform(
				get("/phoneAlert").param("station", firestation.getStation()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		String[] phones = mapper.readValue(content, String[].class);
		assertTrue(phones.length == 3);

	}

	@Test
	public void testGetPhoneByStation_NotFound() throws Exception {
		Mockito.when(reponseService.getPhoneNumberByStation(firestation.getStation())).thenReturn(null);
		mockMvc.perform(
				get("/phoneAlert").param("station", firestation.getStation()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException)).andExpect(
						result -> assertEquals("Phone number Not found", result.getResolvedException().getMessage()));

	}

	/** Endpoint de fire?address=<address> **/

	@Test
	public void testGetPersonsServedByFireStationAddress_success() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
		List<String> allergies = Arrays.asList("nillacilan");
		PersonInfoMedical person1 = new PersonInfoMedical("Jules", "Dupont", "0666111", 40, medications, allergies);
		PersonInfoMedical person2 = new PersonInfoMedical("Jane", "Doe", "0666777", 30, medications, allergies);
		List<PersonInfoMedical> personList = Arrays.asList(person1, person2);

		PersonsServedByFireStation personsServedByFireStation = new PersonsServedByFireStation(personList,
				firestation.getAddress());
		Mockito.when(reponseService.getPersonInfoByAddress(firestation.getAddress()))
				.thenReturn(personsServedByFireStation);
		MvcResult mvcResult = mockMvc
				.perform(
						get("/fire").param("address", firestation.getAddress()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		PersonsServedByFireStation people = mapper.readValue(content, PersonsServedByFireStation.class);
		assertTrue(people.getPersonsServedByFireStation().get(1).getFirstName().equals("Jane"));

	}

	@Test
	public void testGetPersonsServedByFireStationAddress_NotFound() throws Exception {
		Mockito.when(reponseService.getPersonInfoByAddress(firestation.getAddress())).thenReturn(null);
		mockMvc.perform(get("/fire").param("address", firestation.getAddress()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

	/** Endpoint de flood/stations?stations=<a list of station_number> **/
	@Test
	public void testGetfamiliesByStationNumbers_success() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
		List<String> allergies = Arrays.asList("nillacilan");
		PersonInfoMedical person1 = new PersonInfoMedical("Jules", "Dupont", "0666111", 40, medications, allergies);
		PersonInfoMedical person2 = new PersonInfoMedical("Jane", "Doe", "0666777", 30, medications, allergies);
		PersonInfoMedical person3 = new PersonInfoMedical("Paul", "Tomy", "000001", 20, medications, allergies);
		PersonInfoMedical person4 = new PersonInfoMedical("lily", "Ben", "0665557", 35, medications, allergies);
		List<PersonInfoMedical> list1 = Arrays.asList(person1, person2);
		List<PersonInfoMedical> list2 = Arrays.asList(person3, person4);
		Family family1 = new Family(list1);
		Family family2 = new Family(list2);
		List<Family> families = Arrays.asList(family1, family2);
		FireStation firestation1 = new FireStation("951 LoneTree Rd", "2");
		List<String> stationNumbers = Arrays.asList(firestation.getStation(), firestation1.getStation());
		Mockito.when(reponseService.getFamiliesByStationNumbers(stationNumbers)).thenReturn(families);
		MvcResult mvcResult = mockMvc
				.perform(get("/flood/stations").param("stations", firestation.getStation(), firestation1.getStation())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Family[] list = mapper.readValue(content, Family[].class);
		assertTrue(list.length == 2);

	}

	@Test
	public void testGetfamiliesByStationNumbers_NotFound() throws Exception {

		FireStation firestation1 = new FireStation("951 LoneTree Rd", "2");
		List<String> stationNumbers = Arrays.asList(firestation.getStation(), firestation1.getStation());
		Mockito.when(reponseService.getFamiliesByStationNumbers(stationNumbers)).thenReturn(null);
		mockMvc.perform(get("/flood/stations").param("stations", firestation.getStation(), firestation1.getStation())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	/** Endpont de personInfo?firstName=<firstName>&lastName=<lastName> **/
	@Test
	public void testGetPersonsInfoByName_success() throws Exception {
		List<String> medications = Arrays.asList("aznol:350mg", "doliprane:1000mg");
		List<String> allergies = Arrays.asList("nillacilan");
		PersonInfo person1 = new PersonInfo("Jules", "Dupont", "st Germain", "jules@email.com", 40, medications,
				allergies);
		PersonInfo person2 = new PersonInfo("Jane", "Dupont", "st Germain", "jules@email.com", 30, medications,
				allergies);
		PersonInfo person3 = new PersonInfo("Paul", "Dupont", "st Germain", "jules@email.com", 20, medications,
				allergies);
		List<PersonInfo> list = Arrays.asList(person1, person2, person3);
		Mockito.when(reponseService.getPersonsInfoByName("Jules", "Dupont")).thenReturn(list);
		MvcResult mvcResult = mockMvc.perform(get("/personInfo").param("firstName", "Jules").param("lastName", "Dupont")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		PersonInfo[] persons = mapper.readValue(content, PersonInfo[].class);
		assertTrue(persons.length == 3);

	}

	@Test
	public void testGetPersonsInfoByName_NotFound() throws Exception {

		Mockito.when(reponseService.getPersonsInfoByName("Jules", "Dupont")).thenReturn(null);
		mockMvc.perform(get("/personInfo").param("firstName", "Jules").param("lastName", "Dupont")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	/** Endpoint de communityEmail?city=<city> **/
	@Test
	public void testGetEmailsByCity_success() throws Exception {
		List<String> emails = Arrays.asList("jules@email.com", "jane@email.com", "paul@email.com");
		Mockito.when(reponseService.getEmailsByCity("Chambord")).thenReturn(emails);
		MvcResult mvcResult = mockMvc
				.perform(get("/communityEmail").param("city", "Chambord").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		String[] emailList = mapper.readValue(content, String[].class);
		assertTrue(emailList.length == 3);

	}

	@Test
	public void testGetEmailsByCity_NotFound() throws Exception {

		Mockito.when(reponseService.getEmailsByCity("Chambord")).thenReturn(null);
		mockMvc.perform(get("/communityEmail").param("city", "Chambord").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}
}
